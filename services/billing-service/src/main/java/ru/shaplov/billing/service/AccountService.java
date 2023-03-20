package ru.shaplov.billing.service;

import ru.shaplov.billing.model.Account;
import ru.shaplov.billing.model.PaymentData;

public interface AccountService {

    Account getUserBalance(Long userId);

    void topUpBalance(PaymentData paymentData, Long userId);
}
