package com.academy.my_first_spring_project.service;

import com.academy.my_first_spring_project.entity.Recommendation;
import com.academy.my_first_spring_project.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Recommendation entities.
 * Provides methods to perform CRUD operations on Recommendation objects.
 * Utilizes a RecommendationRepository for database interactions.
 */
@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    /**
     * Retrieves all Recommendation entities from the repository.
     *
     * @return A list of Recommendation entities.
     */
    public List<Recommendation> getAll() {
        return recommendationRepository.findAll();
    }

    /**
     * Retrieves a Recommendation entity by its unique identifier.
     *
     * @param id the unique identifier of the Recommendation entity to be retrieved.
     * @return the Recommendation entity identified by the given id.
     * @throws RuntimeException if no Recommendation entity with the given id is found.
     */
    public Recommendation getById(Long id) {
        return recommendationRepository.findById(id)
                .orElseThrow();
    }

    /**
     * Creates a new Recommendation entity. Ensures that the entity does not already have an ID, as IDs are
     * automatically assigned by JPA upon saving. Throws a RuntimeException if the entity already has an ID.
     *
     * @param recommendation The Recommendation entity to be created.
     * @return The newly created and saved Recommendation entity.
     * @throws RuntimeException If the recommendation object already has an ID.
     */
    public Recommendation create(Recommendation recommendation) throws RuntimeException {
        // wir sollten checken, dass das neu zu erstellende Objekt keine id hat, denn diese wird automatisch vergeben von JPA
        if (recommendation.getId() == null) {
            return recommendationRepository.save(recommendation);
        } else {
            throw new RuntimeException("new recommendation is not allowed to have an id already; let the database assign an available id");
        }
    }

    /**
     * Updates an existing Recommendation entity in the repository.
     *
     * @param recommendation The Recommendation object containing updated information.
     *                       This object must have its id set to an existing recommendation's id.
     * @return The updated Recommendation entity after it has been saved to the repository.
     * @throws RuntimeException If the provided recommendation's id does not correspond to an existing entity in the database.
     */
    public Recommendation update(Recommendation recommendation) throws RuntimeException {
        if (recommendationRepository.existsById(recommendation.getId())) {
            return recommendationRepository.save(recommendation);
        } else {
            throw new RuntimeException("recommendation to save has id, but recommendation with this id cannot be found in database");
        }
    }

    /**
     * Deletes a recommendation entity with the specified ID from the repository.
     * If the entity with the given ID does not exist, a RuntimeException is thrown.
     *
     * @param id the ID of the recommendation entity to be deleted
     * @throws RuntimeException if no recommendation with the specified ID is found in the database
     */
    public void delete(Long id) {
        if (recommendationRepository.existsById(id)) {
            recommendationRepository.deleteById(id);
        } else {
            throw new RuntimeException("no recommendation with id " + id + " found in database");
        }
    }
}
