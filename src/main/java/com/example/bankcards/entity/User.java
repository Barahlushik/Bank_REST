package com.example.bankcards.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name="users")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name="username", unique=true, nullable = false)
    String username;
    @Column(name="password", nullable = false)
    String passwordHash;
    @Column(name="name", nullable = false)
    String name;
    @Column(name="surname", nullable = false)
    String surname;
    @Column(name = "birth_date", nullable = false)
    LocalDate birthDate;
    @Column(name="created_at", nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return User.class.hashCode();
    }

    @Override
    public String toString() {
        return "User{id=" + id + "}";
    }

}
