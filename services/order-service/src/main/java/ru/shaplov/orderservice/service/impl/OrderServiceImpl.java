package ru.shaplov.orderservice.service.impl;

import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.shaplov.common.model.exception.ResponseCodeException;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.OrderStatus;
import ru.shaplov.orderservice.model.PaymentMethod;
import ru.shaplov.orderservice.service.OrderPersistentService;
import ru.shaplov.orderservice.service.OrderService;
import ru.shaplov.orderservice.service.ProductService;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderPersistentService persistentService;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    public OrderServiceImpl(OrderPersistentService persistentService,
                            ModelMapper modelMapper,
                            ProductService productService) {
        this.persistentService = persistentService;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @Override
    public Order get(UUID id) {
        return persistentService.find(id);
    }

    @Autowired
    EntityManager em;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Override
    public Order create(Order order) {
        if (persistentService.exists(order.getId())) {
            throw new ResponseCodeException(String.format("Order %s already exists", order.getId()), HttpStatus.CONFLICT);
        }
        order.setOrderStatus(OrderStatus.DRAFT);
        //устанавливаем текущую цену продукта
        order.setCurrentItemPrice(productService.getProductPrice(order.getProductId()));
        return persistentService.saveOrUpdate(order);
    }

    @Override
    public Order update(UUID id, Order order) {
        Order savedOrder = persistentService.find(id);

        checkIdempotency(order.getOrderHash(), savedOrder);
        if (savedOrder.getOrderStatus().getState() > OrderStatus.DRAFT.getState()) {
            throw new ResponseCodeException(String.format("Cannot update order in status: %s", savedOrder.getOrderStatus()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        order.setId(id);
        //устанавливаем текущую цену продукта
        order.setCurrentItemPrice(productService.getProductPrice(order.getProductId()));
        modelMapper.map(order, savedOrder);
        return persistentService.saveOrUpdate(savedOrder);
    }

    //todo change for PaymentInfo
    @Override
    public Order process(UUID id, PaymentMethod paymentMethod, int orderHash) {
        Order savedOrder = persistentService.find(id);

        checkIdempotency(orderHash, savedOrder);
        savedOrder.setPaymentMethod(paymentMethod);
        savedOrder.setOrderStatus(OrderStatus.PROCESSED);
        //todo add paymentInformation for billing service
        //todo add information for delivery service
        return persistentService.saveOrUpdate(savedOrder);
    }

    @Override
    public Order cancel(UUID id, int orderHash) {
        Order savedOrder = persistentService.find(id);

        checkIdempotency(orderHash, savedOrder);
        if (savedOrder.getOrderStatus().getState() > OrderStatus.PROCESSED.getState()) {
            throw new ResponseCodeException(String.format("Cannot delete order in status: %s", savedOrder.getOrderStatus()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //todo cancel delivery service
        if (savedOrder.getOrderStatus() == OrderStatus.PROCESSED) {
            //todo payback in billing service
        }
        savedOrder.setOrderStatus(OrderStatus.CANCELLED);

        return persistentService.saveOrUpdate(savedOrder);
    }

    private void checkIdempotency(int orderHash, Order savedOrder) {
        if (savedOrder.hashCode() != orderHash) {
            throw new ResponseCodeException(String.format("Idempotency error. Expected %d but was %d",
                    savedOrder.hashCode(), orderHash),
                    HttpStatus.CONFLICT);
        }
    }
}
