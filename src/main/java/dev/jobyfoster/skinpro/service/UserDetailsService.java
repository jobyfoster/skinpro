package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
    Collection<? extends GrantedAuthority> getAuthorities(Role role);
}
