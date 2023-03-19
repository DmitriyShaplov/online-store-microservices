package ru.shaplov.orderservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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


    @Override
    public Order create(Order order) {
        if (persistentService.exists(order.getId())) {
            throw new ResponseCodeException(String.format("Order %s already exists", order.getId()), HttpStatus.CONFLICT);
        }
        order.setOrderStatus(OrderStatus.CREATED);
        return persistentService.saveOrUpdate(order);
    }

    @Override
    public Order update(UUID id, Order order) {
        Order savedOrder = persistentService.find(id);
        order.setId(id);
        modelMapper.map(order, savedOrder);
        return persistentService.saveOrUpdate(savedOrder);
    }

    //todo change for PaymentInfo
    @Override
    public Order process(UUID id, PaymentMethod paymentMethod, int orderHash) {
        Order savedOrder = persistentService.find(id);

        checkIdempotency(orderHash, savedOrder);
        //todo add paymentInformation for billing service
        //todo add information for delivery service
        return persistentService.saveOrUpdate(savedOrder);
    }

    @Override
    public Order cancel(UUID id, int orderHash) {
        Order savedOrder = persistentService.find(id);

        checkIdempotency(orderHash, savedOrder);
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
