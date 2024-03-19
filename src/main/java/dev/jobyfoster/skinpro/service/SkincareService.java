package dev.jobyfoster.skinpro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.SurveyResponse;

import java.util.List;
import java.util.Optional;

public interface SkincareService {
    boolean hasCompletedSurvey(Long userId);
    boolean hasAssignedRoutines(Long userId);

    void saveSurveyResponse(SurveyResponse surveyResponse);

    Optional<SurveyResponse> getSurveyResponseByUserId(Long userId);
    void saveRoutine(String routineJson, Long userId) throws JsonProcessingException;

}
