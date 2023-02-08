package ru.shaplov.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.authservice.model.persistence.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
    boolean removeByProfileId(Long profileId);
}
