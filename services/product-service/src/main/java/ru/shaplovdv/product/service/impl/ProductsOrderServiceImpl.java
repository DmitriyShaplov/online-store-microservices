package ru.shaplovdv.product.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.model.event.order.OrderDto;
import ru.shaplovdv.product.model.OrderStatus;
import ru.shaplovdv.product.model.persistence.OrderEntity;
import ru.shaplovdv.product.model.persistence.OrderProduct;
import ru.shaplovdv.product.repository.OrderRepository;
import ru.shaplovdv.product.repository.ProductRepository;
import ru.shaplovdv.product.service.ProductsOrderService;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductsOrderServiceImpl implements ProductsOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;

    public ProductsOrderServiceImpl(OrderRepository orderRepository,
                                    ProductRepository productRepository,
                                    JdbcTemplate jdbcTemplate) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void processOrderReservation(OrderDto orderDto) {
        reserveProductsForIncomingOrder(orderDto);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderDto.getId());
        orderEntity.setStatus(OrderStatus.RESERVED);
        orderEntity.setReservedDate(OffsetDateTime.now(ZoneId.of("UTC")));
        List<OrderProduct> orderProducts = orderDto.getProducts().stream().map(orderProduct -> new OrderProduct()
                        .setProduct(productRepository.getReferenceById(orderProduct.getProductId()))
                        .setQuantity(orderProduct.getQuantity()))
                .toList();
        orderEntity.setProductList(orderProducts);
        orderRepository.save(orderEntity);
    }

    @Override
    public void finishOrderProcessing(UUID orderId) {
        OrderEntity orderEntity = orderRepository.getByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("No order"));
        orderEntity.setStatus(OrderStatus.PROCESSED);

        jdbcTemplate.batchUpdate("""
                        UPDATE products p SET reserved = p.reserved - ?,
                                              quantity = p.quantity - ?
                        WHERE p.id = ?
                        """,
                orderEntity.getProductList(),
                50,
                (ps, argument) -> {
                    ps.setInt(1, argument.getQuantity());
                    ps.setInt(2, argument.getQuantity());
                    ps.setObject(3, argument.getProduct().getId());
                });
    }

    @Override
    public void processOrderCancellation(UUID orderId) {
        OrderEntity orderEntity = orderRepository.getByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("No order"));
        orderEntity.setStatus(OrderStatus.CANCELLED);

        jdbcTemplate.batchUpdate("""
                        UPDATE products p SET reserved = p.reserved - ?
                        WHERE p.id = ?
                        """,
                orderEntity.getProductList(),
                50,
                (ps, argument) -> {
                    ps.setInt(1, argument.getQuantity());
                    ps.setObject(2, argument.getProduct().getId());
                });
    }

    private void reserveProductsForIncomingOrder(OrderDto orderDto) {
        jdbcTemplate.batchUpdate("UPDATE products p SET reserved = p.reserved + ? WHERE p.id = ?",
                orderDto.getProducts(),
                50,
                (ps, argument) -> {
                    ps.setInt(1, argument.getQuantity());
                    ps.setObject(2, argument.getProductId());
                });
    }
}
