package dev.jobyfoster.skinpro.repository;

import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkincareRoutineRepository extends JpaRepository<SkincareRoutine, Long> {
    List<SkincareRoutine> findByUserId(Long userId);
    List<SkincareRoutine> findByUserIdAndRoutineType(Long userId, String routineType);
}