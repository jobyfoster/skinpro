package dev.jobyfoster.skinpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "skincare_routines")
@NoArgsConstructor
@AllArgsConstructor
public class SkincareRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String routineType;

    @Temporal(TemporalType.DATE)
    private LocalDate lastCompleted;

    @OneToMany(mappedBy = "skincareRoutine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineStep> steps = new ArrayList<>();

}
