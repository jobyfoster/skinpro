package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.model.User;

import java.util.Optional;

public interface UserService {
    void addPoints(Long userId, int points);
    // Other methods like authenticate, addPoints, etc.
    void awardDailyLoginPoints(Long userId);

    void checkAndAwardStreakBonus(User user);

    void streakLogic(Optional<User> user);
    void completeSkincareRoutine(Long userId);
}