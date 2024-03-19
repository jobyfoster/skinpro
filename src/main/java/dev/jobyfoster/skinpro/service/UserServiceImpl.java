package dev.jobyfoster.skinpro.service;


import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


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
     * @param user the user to check for a streak and potentially award points to
     */
    public void checkAndAwardStreakBonus(User user) {
        if (user.getStreak() == 7) {
            addPoints(user.getId(), 50);
            user.setStreak(0); // Reset streak after awarding bonus
            // Consider saving the user here if necessary
        }
    }

    public void streakLogic(Optional<User> userOptional) {
        userOptional.ifPresent(user -> {
            LocalDate today = LocalDate.now();
            LocalDate lastLogin = user.getLastLogin();
            long daysBetween = ChronoUnit.DAYS.between(lastLogin, today);

            if (daysBetween == 1) {
                // User logged in consecutively
                user.setStreak(user.getStreak() + 1);
                user.setLastLogin(today);
                checkAndAwardStreakBonus(user); // Pass user directly to avoid redundant lookup
            } else if (daysBetween > 1) {
                // Reset streak if not consecutive days
                user.setLastLogin(today);
                user.setStreak(0);
            }


            awardDailyLoginPoints(user.getId());

            // Assuming save/update method exists
            userRepository.save(user);
        });
    }
}
