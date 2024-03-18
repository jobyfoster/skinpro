package dev.jobyfoster.skinpro.repository;

import dev.jobyfoster.skinpro.model.RoutineStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineStepRepository extends JpaRepository<RoutineStep, Long> {
}

