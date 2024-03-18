package dev.jobyfoster.skinpro.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        // You can add more detailed error handling here based on different exception types
        return "error"; // This is the name of the Thymeleaf template we will create
    }
}
