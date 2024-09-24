package com.academy.my_first_spring_project.repository;

import com.academy.my_first_spring_project.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
