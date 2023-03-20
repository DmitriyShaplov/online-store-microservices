package ru.shaplov.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.billing.model.Account;
import ru.shaplov.billing.model.PaymentData;
import ru.shaplov.billing.service.AccountService;
import ru.shaplov.common.util.SecurityUtil;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Получение текущего состояния счета пользователя")
    @GetMapping("/api/v1/accounts/current")
    public Account getCurrentBalance() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return accountService.getUserBalance(currentUserId);
    }

    @Operation(summary = "Создание или пополнение счета пользователя")
    @PostMapping("/api/v1/accounts/topup")
    public void topUpBalance(@RequestBody PaymentData paymentData) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        accountService.topUpBalance(paymentData, currentUserId);
    }

}
