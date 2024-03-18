package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    void addPoints(Long userId, int points);
    // Other methods like authenticate, addPoints, etc.
}