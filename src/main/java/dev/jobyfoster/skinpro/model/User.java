package dev.jobyfoster.skinpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


@Data
@Entity
// Specifies the table name in the database that this entity will be mapped to.
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fields for username, password, and email without specific annotations, implying they are mapped to columns directly.
    private String username;
    private String password;
    private String email;

    // Specifies that the 'role' field should be stored as a String. This is useful for enums.
    @Enumerated(EnumType.STRING)
    private Role role;

    // Simple integer fields to store user points and streak.
    private int points;

    private int streak;

    // Stores the last login date.
    @Temporal(TemporalType.DATE)
    private LocalDate lastLogin;

    // Indicates whether the user is enabled or not. Defaults to true.
    private boolean enabled = true;

    // Method override from UserDetails, returning user authorities based on their role.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // Indicates that the account is never expired. Override from UserDetails.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indicates that the account is never locked. Override from UserDetails.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indicates that the credentials never expire. Override from UserDetails.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
