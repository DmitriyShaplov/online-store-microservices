package ru.shaplov.billing.service;

import ru.shaplov.billing.model.Payment;
import ru.shaplov.billing.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface HistoryService {

    List<Payment> getPaymentHistory(Long userId);

    List<Transaction> getTransactionHistory(Long userId);

    List<Transaction> getTransactionHistory(UUID orderId);
}
