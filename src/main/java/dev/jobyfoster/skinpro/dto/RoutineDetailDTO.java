package dev.jobyfoster.skinpro.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoutineDetailDTO {
    private String routine;
    private List<StepDTO> steps;
}
