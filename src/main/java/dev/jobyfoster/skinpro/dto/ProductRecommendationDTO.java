package dev.jobyfoster.skinpro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRecommendationDTO {
    @JsonProperty("productName")
    private String name;
    private String descriptor;
}
