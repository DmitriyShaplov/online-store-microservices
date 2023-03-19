package ru.shaplov.account.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.model.exception.ResponseCodeException;
import ru.shaplov.account.model.Account;
import ru.shaplov.account.model.persistence.ProfileEntity;
import ru.shaplov.account.persistence.AccountRepository;
import ru.shaplov.account.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository,
                              ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Account create(Account account) {
        if (account.getId() != null) {
            throw new IllegalStateException("Id should be null");
        }
        ProfileEntity profileEntity = modelMapper.map(account, ProfileEntity.class);
        profileEntity = accountRepository.save(profileEntity);
        return find(profileEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Account find(Long id) {
        ProfileEntity profileEntity = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseCodeException("User not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(profileEntity, Account.class);
    }

    @Override
    public Account findByUsername(String username) {
        ProfileEntity profileEntity = accountRepository.findByUsername(username);
        return modelMapper.map(profileEntity, Account.class);
    }

    @Override
    @Transactional
    public void update(Account account) {
        if (account.getId() == null) {
            throw new IllegalStateException("Id should present");
        }
        ProfileEntity profileEntity = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ResponseCodeException("User not found", HttpStatus.NOT_FOUND));
        ProfileEntity entityToSave = modelMapper.map(account, ProfileEntity.class);
        entityToSave.setId(profileEntity.getId());
        accountRepository.save(entityToSave);
    }
}
