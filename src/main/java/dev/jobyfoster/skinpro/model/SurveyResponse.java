package dev.jobyfoster.skinpro.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
// Specifies the table name in the database that this entity will be mapped to.
@Table(name = "survey_responses")
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String skinType;
    private String mainSkinConcern;
    private String reactionToNewProducts;
    private String ageRange;
    private String sunExposure;
}
