package ru.shaplov.profile.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.shaplov.profile.model.persistence.ProfileEntity;

import java.util.List;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    @Query(value = "select id from users", nativeQuery = true)
    List<Long> findAllIds();

    ProfileEntity findByUsername(String username);

    @Modifying
    @Query(value = "delete from users where id = ?1", nativeQuery = true)
    int deleteByIdNative(Long id);
}
