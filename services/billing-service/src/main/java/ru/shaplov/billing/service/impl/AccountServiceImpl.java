package ru.shaplov.billing.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.billing.model.Account;
import ru.shaplov.billing.model.PaymentData;
import ru.shaplov.billing.persistence.AccountRepository;
import ru.shaplov.billing.service.AccountService;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final JdbcTemplate jdbcTemplate;

    public AccountServiceImpl(ModelMapper modelMapper,
                              AccountRepository accountRepository,
                              JdbcTemplate jdbcTemplate) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * либо получаем баланс пользователя, либо считаем, что он нулевой
     */
    @Override
    @Transactional(readOnly = true)
    public Account getUserBalance(Long userId) {
        return accountRepository.findById(userId)
                .map(entity -> modelMapper.map(entity, Account.class))
                .orElse(new Account().setUserId(userId).setBalance(BigDecimal.ZERO));
    }

    /**
     * Атомарная, идемпотентная операция:
     * - создает запись в accounts, если он еще не создан, иначе пополняет баланс на размер пополнения;
     * - создает запись в payments, связывает ее с аккаунтом, если такой id пополнения (генерируется на клиенте)
     * уже был, операция завершается с ошибкой, данные не добавляются.
     */
    @Override
    @Transactional
    public void topUpBalance(PaymentData paymentData, Long userId) {
        jdbcTemplate.update("""
                            WITH acc_res AS (INSERT INTO accounts (user_id, balance) VALUES (?, ?)
                                ON CONFLICT (user_id) DO UPDATE SET balance = accounts.balance + ?
                                RETURNING *)
                            INSERT INTO payments (id, user_id, amount, prefix, suffix)
                            VALUES (?, (SELECT user_id from acc_res), ?, ?, ?)
                        """,
                userId, paymentData.getAmount(), paymentData.getAmount(),
                paymentData.getId(), paymentData.getAmount(), paymentData.getPrefix(), paymentData.getSuffix());
    }
}
