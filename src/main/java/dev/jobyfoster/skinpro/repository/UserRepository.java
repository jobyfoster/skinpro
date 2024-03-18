package dev.jobyfoster.skinpro.repository;

import dev.jobyfoster.skinpro.model.Role;
import dev.jobyfoster.skinpro.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User findByRole(Role role);
}