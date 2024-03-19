package dev.jobyfoster.skinpro.controller;

import jakarta.persistence.Entity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path="api/v1/routine")
public class RoutineController {
    @GetMapping(path="/complete/{routine_type}")
    public void completeRoutine(
            @PathVariable("routine_type") String type
    ){
        
    }
}
