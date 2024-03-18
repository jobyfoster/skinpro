package dev.jobyfoster.skinpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "skincare_routines")
@NoArgsConstructor
@AllArgsConstructor
public class RoutineStep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skincare_routine_id", nullable = false)
    private SkincareRoutine skincareRoutine;

    @Column(nullable = false)
    private int stepNumber;

    @Column(nullable = false, length = 1024)
    private String description; // Description can include the action and product recommendation

    @Column(nullable = true)
    private String productRecommendation; // Add to RoutineStep for specific product suggestions
}

