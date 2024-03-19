package dev.jobyfoster.skinpro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoutineDTO {
    @JsonProperty("day")
    private RoutineDetailDTO day;

    @JsonProperty("night")
    private RoutineDetailDTO night;
}
