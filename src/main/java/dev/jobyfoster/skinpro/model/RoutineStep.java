package dev.jobyfoster.skinpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "routine_steps")
@NoArgsConstructor
@AllArgsConstructor
public class RoutineStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skincare_routine_id", nullable = false)
    private SkincareRoutine skincareRoutine;

    @Column(nullable = false)
    private int stepNumber;

    @Column(nullable = false, length = 1024)
    private String description;

    @Column(nullable = true)
    private String productRecommendation;
}

