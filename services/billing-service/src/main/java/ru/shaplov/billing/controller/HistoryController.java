package ru.shaplov.billing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.billing.model.Payment;
import ru.shaplov.billing.model.Transaction;
import ru.shaplov.billing.service.HistoryService;
import ru.shaplov.common.util.SecurityUtil;

import java.util.List;

@RestController
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/api/v1/accounts/history/payments")
    public List<Payment> getPaymentHistory() {
        return historyService.getPaymentHistory(SecurityUtil.getCurrentUserId());
    }

    @GetMapping("/api/v1/accounts/history/transactions")
    public List<Transaction> getTransactionHistory() {
        return historyService.getTransactionHistory(SecurityUtil.getCurrentUserId());
    }
}
