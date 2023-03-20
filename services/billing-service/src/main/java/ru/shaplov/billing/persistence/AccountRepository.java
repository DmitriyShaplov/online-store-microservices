package ru.shaplov.billing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.billing.model.persistence.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
