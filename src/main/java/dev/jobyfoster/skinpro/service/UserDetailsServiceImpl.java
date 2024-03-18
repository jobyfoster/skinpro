package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.model.Role;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
// Defines a service class in the Spring framework that implements the UserDetailsService interface.
public class UserDetailsServiceImpl implements UserDetailsService {

    // Declares a UserRepository instance for database operations.
    private final UserRepository userRepository;

    @Autowired
    // Constructor-based dependency injection to initialize userRepository with a concrete implementation provided by Spring.
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    // Custom implementation of the loadUserByUsername method defined in UserDetailsService.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieves a User entity by username. Throws UsernameNotFoundException if the user is not found.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Constructs a Spring Security User object with user details and authorities based on the user's role.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRole()));
    }

    // Helper method to convert a Role entity to a Spring Security GrantedAuthority.
    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        // Wraps the role name with "ROLE_" prefix and converts it into a GrantedAuthority object.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}

