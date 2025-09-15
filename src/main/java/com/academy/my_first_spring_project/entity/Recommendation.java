package com.academy.my_first_spring_project.entity;

import com.academy.my_first_spring_project.enums.TargetGroup;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a recommendation entity containing information about recommended content or items.
 *
 * Annotations:
 * - @Getter, @Setter: Generate getter and setter methods for all fields.
 * - @Builder: Provides a builder pattern for object creation.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @AllArgsConstructor: Generates a constructor with one argument for each field in the class.
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the table that this entity is mapped to.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recommendation")
public class Recommendation {

    /**
     * Unique identifier for the Recommendation entity.
     * This field is automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The URL of the recommended content or item.
     * This field maps to the "url" column in the database.
     */
    @Column(name = "url")
    private String url;

    /**
     * Represents the name associated with a recommendation.
     * This field is mapped to the "name" column in the database table.
     */
    @Column(name = "name")
    private String name;

    /**
     * The rating of the recommendation.
     * This field represents an integer value that indicates the rating given to the recommendation.
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * Describes the content or items recommended in the recommendation entity.
     */
    @Column(name = "description")
    private String description;

    /**
     * Represents the date and time when the recommendation was submitted.
     * Mapped to the "submitted" column in the database.
     */
    @Column(name = "submitted")
    private LocalDateTime submitted;

    /**
     * Specifies the target group for the recommendation.
     *
     * This field is mapped to the "target_group" column in the database
     * as an enumerated type, using the string representation of the enum value.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_group")
    private TargetGroup targetGroup;

    /**
     * Represents the Person who discovered or recommended the item.
     *
     * Annotations:
     * - @ManyToOne: Specifies a many-to-one relationship between Recommendation and Person.
     * - @JoinColumn: Specifies the foreign key column, in this case, "person_id".
     * - fetch = FetchType.LAZY: Indicates that the associated Person entity should be lazily loaded, meaning it will be fetched only when accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY) // LAZY fetching is the default, but for tutorial purposes this is written
    // here explicitly
    @JoinColumn(name = "person_id")
    private Person discoverer;
}
