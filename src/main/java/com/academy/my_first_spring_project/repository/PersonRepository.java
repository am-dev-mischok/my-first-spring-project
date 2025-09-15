package com.academy.my_first_spring_project.repository;

import com.academy.my_first_spring_project.entity.Person;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
