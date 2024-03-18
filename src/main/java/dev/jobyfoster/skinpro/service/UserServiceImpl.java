package dev.jobyfoster.skinpro.service;


import dev.jobyfoster.skinpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addPoints(Long userId, int pointsToAdd) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPoints(user.getPoints() + pointsToAdd);
            userRepository.save(user);
        });
    }



    public void awardDailyLoginPoints(Long userId) {
        addPoints(userId, 5);
    }

    // Method to handle completing the daily skincare routine
    public void completeSkincareRoutine(Long userId) {
        addPoints(userId, 20);
    }

    // Method to check and award 7-day streak bonus
    public void checkAndAwardStreakBonus(Long userId) {
        // This requires storing and checking login dates or routine completion dates.
        // Implement logic to check if the user has a 7-day streak
        // If yes, award 50 bonus points
        addPoints(userId, 50); // Example, implement checking logic
    }

    public void login(String username, String password) {
        // Implement your login logic with Spring Security
        // After successful login, call the awardDailyLoginPoints method
        // userRepository.findByUsername(username).ifPresent(user -> awardDailyLoginPoints(user.getId()));
    }
}
