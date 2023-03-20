package com.example.deliveryservice.service.impl;

import com.example.deliveryservice.model.DeliveryStatus;
import com.example.deliveryservice.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.model.event.order.OrderDto;
import ru.shaplov.common.model.event.order.OrderEvent;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplov.common.outbox.EventPublisher;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
public class CourierServiceImpl implements CourierService {

    private final EventPublisher eventPublisher;
    private final JdbcTemplate jdbcTemplate;

    public CourierServiceImpl(EventPublisher eventPublisher,
                              JdbcTemplate jdbcTemplate) {
        this.eventPublisher = eventPublisher;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void finishDelivery(UUID orderId) {
        jdbcTemplate.update("""
                UPDATE delivery_slots ds SET status = ? WHERE order_id = ?
                """, DeliveryStatus.FINISHED.name(), orderId);

        eventPublisher.publishEvent(OrderEvent.of(orderId, OrderEventType.FINISHED));
        log.debug("Успешно завершили доставку заказа {}", orderId);
    }

    @Override
    @Transactional
    public OrderDto attemptToReserveDeliverySlot(OrderDto order) {
        OffsetDateTime startInterval = order.getDeliveryDate();
        OffsetDateTime endInterval = startInterval.plusHours(1);

        //попытка зарезервировать курьера
        Integer courierId = jdbcTemplate.queryForObject("""
                INSERT INTO delivery_slots (courier_id, status, start_time, end_time, order_id)
                VALUES ((SELECT courier_id
                         FROM couriers
                         WHERE NOT EXISTS (
                                 SELECT 1
                                 FROM delivery_slots
                                 WHERE couriers.courier_id = delivery_slots.courier_id
                                   AND (start_time, end_time) OVERLAPS (?, ?)
                             )
                         LIMIT 1), ?, ?, ?, ?) RETURNING courier_id;
                """, Integer.class, startInterval, endInterval, DeliveryStatus.IN_DELIVERY.name(), startInterval, endInterval, order.getId());
        String courierInfo = jdbcTemplate.queryForObject("""
                SELECT name || ' ' || phone from couriers WHERE courier_id = ?
                """, String.class, courierId);
        order.setCourierInfo(courierInfo);
        log.debug("Назначен курьер {} на слот {}-{}", courierInfo, startInterval, endInterval);
        return order;
    }
}
