package ru.shaplov.userscrud.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.shaplov.userscrud.model.persistence.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "select id from users", nativeQuery = true)
    List<Long> findAllIds();

    @Modifying
    @Query(value = "delete from users where id = ?1", nativeQuery = true)
    int deleteByIdNative(Long id);
}
