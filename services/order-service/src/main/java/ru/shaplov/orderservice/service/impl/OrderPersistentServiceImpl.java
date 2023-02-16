package ru.shaplov.orderservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.model.exception.ResponseCodeException;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.persistence.OrderEntity;
import ru.shaplov.orderservice.repository.OrderRepository;
import ru.shaplov.orderservice.service.OrderPersistentService;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional
public class OrderPersistentServiceImpl implements OrderPersistentService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public OrderPersistentServiceImpl(ModelMapper modelMapper,
                                      OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Order find(UUID id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseCodeException(String.format("Order %s not found", id), HttpStatus.NOT_FOUND));
        return modelMapper.map(orderEntity, Order.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Order saveOrUpdate(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        updateDates(orderEntity);
        OrderEntity savedEntity = orderRepository.save(orderEntity);
        return modelMapper.map(savedEntity, Order.class);
    }

    private void updateDates(OrderEntity orderEntity) {
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (orderEntity.getCreateDate() == null) {
            orderEntity.setCreateDate(now);
        }
        orderEntity.setUpdateDate(now);
    }
}
