package ru.shaplov.account.controller.internal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.account.model.Account;
import ru.shaplov.account.service.AccountService;

@RestController
@RequestMapping("/api/v1/internal/accounts")
public class RegistrationController {

    private final AccountService profileService;

    public RegistrationController(AccountService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public Account registration(@RequestBody Account account) {
        return profileService.create(account);
    }
}
