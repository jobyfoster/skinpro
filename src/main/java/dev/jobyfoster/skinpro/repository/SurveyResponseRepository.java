package dev.jobyfoster.skinpro.repository;

import dev.jobyfoster.skinpro.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    Optional<SurveyResponse> findByUserId(Long userId);
}