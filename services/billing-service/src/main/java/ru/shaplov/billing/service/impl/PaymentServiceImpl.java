package ru.shaplov.billing.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.billing.model.TransactionType;
import ru.shaplov.billing.model.persistence.TransactionEntity;
import ru.shaplov.billing.persistence.TransactionRepository;
import ru.shaplov.billing.service.PaymentService;
import ru.shaplov.common.model.event.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionRepository transactionRepository;

    public PaymentServiceImpl(JdbcTemplate jdbcTemplate,
                              TransactionRepository transactionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void processPayment(OrderDto order) {
        BigDecimal productSum = order.getProducts().stream().map(product -> product.getCurrentPrice()
                        .multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new IllegalStateException("Невозможно посчитать цену заказа"));

        processAccountBalance(order, productSum);
        createTransaction(order, productSum);
    }

    @Override
    @Transactional
    public void processPaymentCancellation(UUID orderId) {
        TransactionEntity transaction = transactionRepository.getByOrderId(orderId);
        createCancelTransaction(transaction.getAccount().getUserId(), transaction.getOrderId(), transaction.getAmount());
        revertAccountBalance(transaction.getAccount().getUserId(), transaction.getAmount());
    }

    private void createTransaction(OrderDto order, BigDecimal productSum) {
        jdbcTemplate.update("""
                INSERT INTO transactions (user_id, order_id, type, amount) VALUES
                (?, ?, ?, ?)
                """, order.getUserId(), order.getId(), TransactionType.PROCESS_ORDER.name(), productSum);
    }

    private void processAccountBalance(OrderDto order, BigDecimal productSum) {
        int update = jdbcTemplate.update("""
                UPDATE accounts a SET balance = a.balance - ? WHERE user_id = ?
                """, productSum, order.getUserId());
        if (update != 1) {
            throw new IllegalStateException(String.format("Нет аккаунта у юзера %s", order.getUserId()));
        }
    }

    private void createCancelTransaction(Long userId, UUID orderId, BigDecimal productSum) {
        jdbcTemplate.update("""
                INSERT INTO transactions (user_id, order_id, type, amount) VALUES
                (?, ?, ?, ?)
                """, userId, orderId, TransactionType.CANCEL_ORDER.name(), productSum);
    }

    private void revertAccountBalance(Long userId, BigDecimal productSum) {
        int update = jdbcTemplate.update("""
                UPDATE accounts a SET balance = a.balance + ? WHERE user_id = ?
                """, productSum, userId);
        if (update != 1) {
            throw new IllegalStateException(String.format("Нет аккаунта у юзера %s", userId));
        }
    }
}
