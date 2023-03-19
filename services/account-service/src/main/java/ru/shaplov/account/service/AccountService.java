package ru.shaplov.account.service;

import ru.shaplov.account.model.Account;

public interface AccountService {
    Account create(Account account);
    Account find(Long id);
    Account findByUsername(String username);
    void update(Account account);

}
