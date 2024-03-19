package dev.jobyfoster.skinpro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jobyfoster.skinpro.dto.RoutineDTO;
import dev.jobyfoster.skinpro.dto.RoutineDetailDTO;
import dev.jobyfoster.skinpro.model.RoutineStep;
import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.SurveyResponse;
import dev.jobyfoster.skinpro.repository.SkincareRoutineRepository;
import dev.jobyfoster.skinpro.repository.SurveyResponseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return !skincareRoutineRepository.findByUserId(userId).isEmpty();
    }

    @Transactional
    public void saveRoutine(String routineJson, Long userId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RoutineDTO routineDTO = objectMapper.readValue(routineJson, RoutineDTO.class);

        saveRoutineDetails(routineDTO.getDay(), userId, "day");
        saveRoutineDetails(routineDTO.getNight(), userId, "night");
    }

    @Override
    public List<SkincareRoutine> getDayRoutine(Long userId) {
        return skincareRoutineRepository.findByUserIdAndRoutineType(userId, "day");
    }

    @Override
    public List<SkincareRoutine> getNightRoutine(Long userId) {
        // Assuming 'night' is the identifier for night routines
        return skincareRoutineRepository.findByUserIdAndRoutineType(userId, "night");
    }

    private void saveRoutineDetails(RoutineDetailDTO routineDetailDTO, Long userId, String routineType) {
        SkincareRoutine skincareRoutine = new SkincareRoutine();
        skincareRoutine.setUserId(userId);
        skincareRoutine.setRoutineType(routineType);

        List<RoutineStep> steps = routineDetailDTO.getSteps().stream().map(stepDTO -> {
            RoutineStep step = new RoutineStep();
            step.setStepNumber(stepDTO.getStepNumber());
            step.setDescription(stepDTO.getDescription());
            if (stepDTO.getProductRecommendation() != null) {
                step.setProductRecommendation(stepDTO.getProductRecommendation().getName() + ": " + stepDTO.getProductRecommendation().getDescriptor());
            }
            step.setSkincareRoutine(skincareRoutine);
            return step;
        }).collect(Collectors.toList());

        skincareRoutine.setSteps(steps);
        LocalDate date = LocalDate.now().minus(Period.ofDays(2));
        skincareRoutine.setLastCompleted(date);

        skincareRoutineRepository.save(skincareRoutine);
    }


    @Override
    public void saveSurveyResponse(SurveyResponse surveyResponse) {
        surveyResponseRepository.save(surveyResponse);
    }

    @Override
    public Optional<SurveyResponse> getSurveyResponseByUserId(Long userId) {
        return surveyResponseRepository.findByUserId(userId);
    }

}
