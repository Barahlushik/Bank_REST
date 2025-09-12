package com.example.bankcards.entity;


import com.example.bankcards.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="users")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

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
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRole().name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    public boolean isAdmin() {
        return role.getRole().equals(RoleType.ADMIN);
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
