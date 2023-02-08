package ru.shaplov.authservice.model.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id")
    private Long profileId;

    private String login;

    private String password;

    @Column(name = "reg_date", insertable = false, updatable = false)
    private OffsetDateTime regDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
