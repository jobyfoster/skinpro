package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.model.SurveyResponse;

public interface SkincareService {
    boolean hasCompletedSurvey(Long userId);
    boolean hasAssignedRoutines(Long userId);

    void saveSurveyResponse(SurveyResponse surveyResponse);
}
