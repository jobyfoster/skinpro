package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.dto.SigninRequest;
import dev.jobyfoster.skinpro.model.User;

public interface UserService {
    void addPoints(Long userId, int points);
    // Other methods like authenticate, addPoints, etc.
    void awardDailyLoginPoints(Long userId);

    void checkAndAwardStreakBonus(Long userId);

    void streakLogic(SigninRequest signinRequest);
}