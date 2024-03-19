package dev.jobyfoster.skinpro.service;


import dev.jobyfoster.skinpro.config.PasswordConfig;
import dev.jobyfoster.skinpro.dto.SigninRequest;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


// Marks this class as a service component in the Spring context,
// indicating it's a business service.
@Service
public class UserServiceImpl implements UserService {
    // Dependency on UserRepository for database operations.
    private final UserRepository userRepository;
    // Autowired constructor for dependency injection of UserRepository,
    // ensuring an instance of UserRepository is supplied by Spring at runtime.
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a specific number of points to a user's account.
     * If the user is found, their points are updated in the database.
     *
     * @param userId      the ID of the user to add points to
     * @param pointsToAdd the number of points to add
     */
    @Override
    public void addPoints(Long userId, int pointsToAdd) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPoints(user.getPoints() + pointsToAdd);
            userRepository.save(user);
        });
    }


    /**
     * Awards 5 points to a user for their daily login activity.
     *
     * @param userId the ID of the user to award points to
     */
    public void awardDailyLoginPoints(Long userId) {
        addPoints(userId, 5);
    }

    /**
     * Awards 20 points to a user for completing their daily skincare routine.
     *
     * @param userId the ID of the user to award points to
     */
    public void completeSkincareRoutine(Long userId) {
        addPoints(userId, 20);
    }

    /**
     * Currently, it awards 50 points by checking if the user has a 7 day streak
     *
     * @param userId the ID of the user to check for a streak and potentially award points to
     */
    public void checkAndAwardStreakBonus(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
        if (user.getStreak() == 7){
            addPoints(userId, 50);
            user.setStreak(0);
        }
    }

    public void streakLogic(User user){
        LocalDate date = LocalDate.now();
        if (date.getDayOfMonth() - user.getLastLogin().getDayOfMonth() == 1){
            user.setLastLogin(LocalDate.now());
            user.setStreak(user.getStreak()+1);
            this.awardDailyLoginPoints(user.getId());
            this.checkAndAwardStreakBonus(user.getId());
        } else if (date.getDayOfMonth() - user.getLastLogin().getDayOfMonth() > 1) {
            user.setStreak(0);
            this.awardDailyLoginPoints(user.getId());
        }
    }
}
