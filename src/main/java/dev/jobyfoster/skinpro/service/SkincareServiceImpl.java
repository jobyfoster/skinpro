package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.model.SurveyResponse;
import dev.jobyfoster.skinpro.repository.SkincareRoutineRepository;
import dev.jobyfoster.skinpro.repository.SurveyResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkincareServiceImpl implements SkincareService {
    private final SurveyResponseRepository surveyResponseRepository;
    private final SkincareRoutineRepository skincareRoutineRepository;

    @Autowired
    public SkincareServiceImpl(SurveyResponseRepository surveyResponseRepository, SkincareRoutineRepository skincareRoutineRepository) {
        this.surveyResponseRepository = surveyResponseRepository;
        this.skincareRoutineRepository = skincareRoutineRepository;
    }

    public boolean hasCompletedSurvey(Long userId) {
        return surveyResponseRepository.findByUserId(userId).isPresent();
    }

    public boolean hasAssignedRoutines(Long userId) {
        return skincareRoutineRepository.findByUserId(userId).isPresent();
    }

    @Override
    public void saveSurveyResponse(SurveyResponse surveyResponse) {
        surveyResponseRepository.save(surveyResponse);
    }
}
