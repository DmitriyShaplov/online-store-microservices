package ru.shaplov.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.shaplov.common.client.product.ProductClient;
import ru.shaplov.common.client.product.model.Product;
import ru.shaplov.common.client.product.model.ProductParam;
import ru.shaplov.common.model.event.notification.NotificationEvent;
import ru.shaplov.common.model.event.notification.NotificationPayload;
import ru.shaplov.common.model.event.order.OrderDto;
import ru.shaplov.common.model.event.order.OrderEvent;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplov.common.model.exception.ResponseCodeException;
import ru.shaplov.common.outbox.EventPublisher;
import ru.shaplov.orderservice.model.CartItem;
import ru.shaplov.orderservice.model.ErrorInfo;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.OrderStatus;
import ru.shaplov.orderservice.model.persistence.OrderEntity;
import ru.shaplov.orderservice.model.persistence.OrderProductEntity;
import ru.shaplov.orderservice.repository.OrderRepository;
import ru.shaplov.orderservice.service.CartItemService;
import ru.shaplov.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final CartItemService cartItemService;
    private final TransactionTemplate transactionTemplate;
    private final EventPublisher eventPublisher;

    public OrderServiceImpl(ModelMapper modelMapper,
                            OrderRepository orderRepository,
                            ProductClient productClient,
                            CartItemService cartItemService,
                            TransactionTemplate transactionTemplate,
                            EventPublisher eventPublisher) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.cartItemService = cartItemService;
        this.transactionTemplate = transactionTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional()
    public Order get(UUID id) {
        OrderEntity orderEntity = orderRepository.findOrderEntityById(id)
                .orElseThrow(() -> new ResponseCodeException(String.format("Order %s not found", id), HttpStatus.NOT_FOUND));
        return modelMapper.map(orderEntity, Order.class);
    }

    @Override
    public List<Order> findAll(Long userId) {
        return modelMapper.map(orderRepository.findAllByUserId(userId), new TypeToken<List<Order>>() {
        }.getType());
    }

    @Override
    public Order create(Order order) {
        validateOrder(order);
        OrderEntity orderEntity = prepareOrderForSave(order);
        return transactionTemplate.execute(status -> {
            //сохраняем заказ, наполняя его товарами из корзины
            OrderEntity savedOrder = orderRepository.save(orderEntity);
            //очищаем корзину для последующих заказов
            cartItemService.clearCart(savedOrder.getUserId());
            //публикуем событие для сохранения в outbox таблицу и отправки дальше в кафку
            OrderDto orderDto = modelMapper.map(savedOrder, OrderDto.class);
            OrderEvent orderEvent = OrderEvent.of(orderDto, OrderEventType.ORDER_CREATED);
            eventPublisher.publishEvent(orderEvent);
            eventPublisher.publishEvent(buildNotification(order, savedOrder, orderDto));
            return modelMapper.map(savedOrder, Order.class);
        });
    }

    private NotificationEvent buildNotification(Order order, OrderEntity savedOrder, OrderDto orderDto) {
        return NotificationEvent.of(new NotificationPayload()
                .setEmail(order.getEmail())
                .setOrderId(orderDto.getId())
                .setUserId(order.getUserId())
                .setOrderStatus(savedOrder.getOrderStatus().name())
                .setShippingAddress(order.getShippingAddress())
                .setDeliveryDate(order.getDeliveryDate()));
    }

    @Override
    public Order cancelOrder(UUID orderId, ErrorInfo errorInfo) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
        if (orderInFinalStatus(orderEntity)) {
            log.warn("Order was already {}", orderEntity.getOrderStatus());
            return modelMapper.map(orderEntity, Order.class);
        }
        orderEntity.setOrderStatus(OrderStatus.CANCELLED);
        orderEntity.setErrorInfo(errorInfo);
        orderEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));
        log.info("Order {} cancelled.", orderId);
        return modelMapper.map(orderEntity, Order.class);
    }

    private boolean orderInFinalStatus(OrderEntity orderEntity) {
        return orderEntity.getOrderStatus() == OrderStatus.CANCELLED ||
                orderEntity.getOrderStatus() == OrderStatus.FINISHED;
    }

    @Override
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
        orderEntity.setOrderStatus(status);
        orderEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));
        log.info("Order {} status changed to {}.", orderId, status);
        return modelMapper.map(orderEntity, Order.class);
    }

    private void validateOrder(Order order) {
        if (order.getDeliveryDate().atZoneSameInstant(ZoneId.of("UTC")).getHour() < 10 ||
                order.getDeliveryDate().atZoneSameInstant(ZoneId.of("UTC")).getHour() > 21) {
            throw new ResponseCodeException("Доставка допускается только с 10 до 21 часу", HttpStatus.BAD_REQUEST);
        }
        if (order.getDeliveryDate().isBefore(OffsetDateTime.now(ZoneId.of("UTC")).plusHours(2))) {
            throw new ResponseCodeException("Доставку можно назначить только через 2 часа от текущего времени", HttpStatus.BAD_REQUEST);
        }
        if (order.getDeliveryDate().minusMonths(1).compareTo(OffsetDateTime.now(ZoneId.of("UTC"))) >= 0) {
            throw new ResponseCodeException("Доставку можно назначить только в пределах 1-го месяца", HttpStatus.BAD_REQUEST);
        }
    }

    private OrderEntity prepareOrderForSave(Order order) {
        List<CartItem> cartItems = cartItemService.getList(order.getUserId());
        if (cartItems.isEmpty()) {
            throw new ResponseCodeException("Корзина пуста!", HttpStatus.PRECONDITION_REQUIRED);
        }
        List<Product> productList = productClient.getProductList(new ProductParam().setIdList(
                cartItems.stream().map(CartItem::getProductId).toList()
        ));
        List<OrderProductEntity> orderProductEntities = modelMapper.map(cartItems, new TypeToken<List<OrderProductEntity>>() {
        }.getType());
        HashMap<UUID, BigDecimal> productPricesMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice, (a, b) -> b, HashMap::new));
        orderProductEntities.forEach(orderProductEntity -> orderProductEntity
                .setCurrentPrice(productPricesMap.get(orderProductEntity.getProductId())));

        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderEntity.setOrderStatus(OrderStatus.CREATED);
        orderEntity.setProducts(orderProductEntities);
        orderEntity.setCreateDate(OffsetDateTime.now(ZoneId.of("UTC")));
        orderEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));
        return orderEntity;
    }
}
