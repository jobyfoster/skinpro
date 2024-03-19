package dev.jobyfoster.skinpro.dto;

import lombok.Data;

@Data
public class StepDTO {
    private int stepNumber;
    private String description;
    private ProductRecommendationDTO productRecommendation;
}
