package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recommendation")
public class RecommendationController {

    @Autowired
    RecommendationService recommendationService;

    @GetMapping
    public String getRecommendations(Model model) {
        model.addAttribute("allRecommendations", recommendationService.getAll());
        return "recommendations";
    }

    @GetMapping(value = "/{id}")
    public String getRecommendationById(
            @PathVariable(name="id") Long recommendationId,
            Model model
    ) {
        model.addAttribute("recommendation", recommendationService.getById(recommendationId));
        return "recommendation";
    }

//    @PostMapping
//    public String createRecommendation(
//            @RequestBody Recommendation recommendation,
//            Model model
//    ) {
//        return recommendationService.create(recommendation);
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public String createRecommendationFromForm(
//            Recommendation recommendation,
//            Model model
//    ) {
//        return recommendationService.create(recommendation);
//    }
//
//    @PutMapping
//    public String updateRecommendation(
//            @RequestBody Recommendation recommendation,
//            Model model
//    ) {
//        return recommendationService.update(recommendation);
//    }
//
//    @DeleteMapping
//    public String deleteRecommendation(
//            @RequestParam(name="id") Long recommendationId,
//            Model model
//    ) {
//        recommendationService.delete(recommendationId);
//    }
}
