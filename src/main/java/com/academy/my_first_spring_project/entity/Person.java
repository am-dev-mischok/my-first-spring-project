package com.academy.my_first_spring_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Represents a person entity with basic attributes including id, name, email, age, and marital status.
 * Utilizes various Lombok annotations to reduce boilerplate code.
 *
 * Annotations:
 * - @Getter, @Setter: Generate getter and setter methods for all fields.
 * - @Builder: Provides a builder pattern for object creation.
 * - @AllArgsConstructor: Generates a constructor with one argument for each field in the class.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {

    /**
     * Unique identifier for the Person entity.
     * This field is automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the name of the person.
     */
    private String name;
    /**
     * The email address of the person.
     */
    private String email;
    /**
     * The age of the person. This field stores the person's age and is represented as an Integer.
     */
    private Integer age;
    /**
     * Indicates the marital status of the person.
     * True if the person is married, false otherwise.
     */
    private Boolean married;
}
