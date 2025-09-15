package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.entity.Recommendation;
import com.academy.my_first_spring_project.enums.TargetGroup;
import com.academy.my_first_spring_project.service.PersonService;
import com.academy.my_first_spring_project.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller for handling recommendations.
 * Provides endpoints for CRUD operations on recommendations.
 */
@Controller
@RequestMapping("/recommendation")
public class RecommendationController {

    @Autowired
    RecommendationService recommendationService;

    @Autowired
    PersonService personService;

    /**
     * Handles the GET request to fetch and display the recommendations list.
     *
     * @param model The model object used to pass data to the view template.
     * @return The name of the view template to be rendered.
     */
    @GetMapping
    public String getRecommendations(Model model) {
        model.addAttribute("allRecommendations", recommendationService.getAll());
        model.addAttribute("recommendation", new Recommendation());
        model.addAttribute("targetGroups", TargetGroup.values());
        model.addAttribute("persons", personService.getAll());
        model.addAttribute("mode", "create");
        return "recommendation_list";
    }

    /**
     * Handles the GET request to fetch and display a specific recommendation by its ID.
     *
     * @param recommendationId The ID of the recommendation to retrieve.
     * @param model The model object used to pass data to the view template.
     * @return The name of the view template to be rendered.
     */
    @GetMapping(value = "/{id}")
    public String getRecommendationById(
            @PathVariable(name = "id") Long recommendationId,
            Model model
    ) {
        model.addAttribute("recommendation", recommendationService.getById(recommendationId));
        model.addAttribute("targetGroups", TargetGroup.values());
        model.addAttribute("persons", personService.getAll());
        model.addAttribute("mode", "view");
        return "recommendation";
    }

    /**
     * Handles the GET request to edit a specific recommendation by its ID.
     *
     * @param recommendationId The ID of the recommendation to be edited.
     * @param model The model object used to pass data to the view template.
     * @return The name of the view template to be rendered.
     */
    @GetMapping(value = "/edit/{id}")
    public String editRecommendationById(
            @PathVariable(name = "id") Long recommendationId,
            Model model
    ) {
        model.addAttribute("recommendation", recommendationService.getById(recommendationId));
        model.addAttribute("persons", personService.getAll());
        model.addAttribute("mode", "edit");
        return "recommendation";
    }

    /**
     * Handles the POST request to create a new recommendation.
     *
     * @param newRecommendation The recommendation object to be created.
     * @param discovererId The ID of the discoverer associated with the recommendation.
     * @return A redirection to the list of recommendations.
     */
    @PostMapping(value = "/create")
    public String createRecommendation(@ModelAttribute("recommendation") Recommendation newRecommendation,
                                       @RequestParam("discoverer.id") String discovererId) {
        newRecommendation.setSubmitted(LocalDateTime.now());
        newRecommendation.setDiscoverer(personService.getById(Long.parseLong(discovererId)));
        recommendationService.create(newRecommendation);
        return "redirect:/recommendation";
    }

    /**
     * Handles the PUT request to update an existing recommendation.
     *
     * @param recommendationId The ID of the recommendation to be updated.
     * @param updatedRecommendation The updated recommendation object.
     * @param model The model object used to pass data to the view template.
     * @return A redirection to the list of recommendations.
     */
    @PutMapping(value = "/update/{id}")
    public String updateRecommendation(@PathVariable(name = "id") Long recommendationId,
                                       @ModelAttribute Recommendation updatedRecommendation,
                                       Model model
    ) {
        updatedRecommendation.setId(recommendationId);
        updatedRecommendation.setSubmitted(LocalDateTime.now());
        recommendationService.update(updatedRecommendation);
        return "redirect:/recommendation";
    }

    /**
     * Handles the DELETE request to delete a specific recommendation by its ID.
     *
     * @param recommendationId The ID of the recommendation to be deleted.
     * @return A redirection to the list of recommendations.
     */
    @DeleteMapping(value = "/delete")
    public String deleteRecommendation(@RequestParam(name = "id") Long recommendationId) {

        recommendationService.delete(recommendationId);
        return "redirect:/recommendation";
    }
}


