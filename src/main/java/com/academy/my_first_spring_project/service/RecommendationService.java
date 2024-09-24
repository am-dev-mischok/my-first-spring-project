package com.academy.my_first_spring_project.service;

import com.academy.my_first_spring_project.entity.Recommendation;
import com.academy.my_first_spring_project.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    public List<Recommendation> getAll() {
        return recommendationRepository.findAll();
    }

    public Recommendation getById(Long id) {
        return recommendationRepository.findById(id)
                .orElseThrow();
    }

    public Recommendation create(Recommendation recommendation) throws RuntimeException {
        // wir sollten checken, dass das neu zu erstellende Objekt keine id hat, denn diese wird automatisch vergeben von JPA
        if (recommendation.getId() == null) {
            return recommendationRepository.save(recommendation);
        } else {
            throw new RuntimeException("new recommendation is not allowed to have an id already; let the database assign an available id");
        }
    }

    public Recommendation update(Recommendation recommendation) throws RuntimeException {
        if (recommendationRepository.existsById(recommendation.getId())) {
            return recommendationRepository.save(recommendation);
        } else {
            throw new RuntimeException("recommendation to save has id, but recommendation with this id cannot be found in database");
        }
    }

    public void delete(Long id) {
        recommendationRepository.deleteById(id);
    }
}
