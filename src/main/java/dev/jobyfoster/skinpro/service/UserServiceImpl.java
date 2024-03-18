package dev.jobyfoster.skinpro.service;


import dev.jobyfoster.skinpro.config.PasswordConfig;
import dev.jobyfoster.skinpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
     * TODO: Implement the logic to check a user's 7-day streak and award 50 bonus points if applicable.
     * Currently, it awards 50 points without checking the 7-day streak condition.
     *
     * @param userId the ID of the user to check for a streak and potentially award points to
     */
    public void checkAndAwardStreakBonus(Long userId) {
        // This method requires implementation of logic to check for a 7-day streak.
        // If the user has logged in or completed routines for 7 consecutive days, award them 50 bonus points.
        addPoints(userId, 50); // Example, implement checking logic
    }

}
