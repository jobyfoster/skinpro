package dev.jobyfoster.skinpro.dto;

import lombok.Data;

@Data
public class SurveyRequest {
    private String skinType;
    private String mainSkinConcern;
    private String reactionToNewProducts;
    private String ageRange;
    private String sunExposure;
}
