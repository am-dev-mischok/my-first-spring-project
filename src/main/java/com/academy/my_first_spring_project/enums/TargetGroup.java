package com.academy.my_first_spring_project.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Specifies different target groups based on age ranges.
 * Each target group is defined by a label, a minimum age, and a maximum age.
 *
 * Annotations:
 * - @Getter: Generates getter methods for all fields.
 * - @AllArgsConstructor: Generates a constructor with one argument for each field in the enum.
 */
@Getter
@AllArgsConstructor
public enum TargetGroup {
    /**
     * Represents the 'Everyone' target group which includes all ages.
     * The label for this group is "Alle".
     * No minimum or maximum age constraints are specified.
     */
    EVERYONE("Alle", null, null),
    /**
     * Represents the target group for babies, characterized by
     * the label "Babys" and a maximum age of 3 years.
     */
    BABYS("Babys", null, 3),
    /**
     * Represents the target group for children.
     * This group is defined by the age range of 4 to 12 years.
     */
    CHILDREN("Kinder", 4, 12),
    /**
     * Target group for teenagers.
     * Label: "Jugendliche"
     * Minimum age: 13
     * Maximum age: 17
     */
    TEENS("Jugendliche", 13, 17),
    /**
     * Represents the target group of adults aged from 18 to 59.
     * The label for this group is "Erwachsene".
     */
    ADULTS("Erwachsene", 18, 59),
    /**
     * Represents the target group for older individuals.
     * This group is labeled as "Alte Leute" and includes individuals starting from the age of 60.
     * There is no upper age limit defined for this group.
     */
    OLD("Alte Leute", 60, null);

    /**
     * Label representing the name of the target group.
     */
    private final String label;
    /**
     * The minimum age required to be classified under this target group.
     * This value is part of the age range that defines various target groups.
     */
    private final Integer minAge;
    /**
     * Represents the maximum age (inclusive) for the target group.
     *
     * Used within TargetGroup enum to define the upper age limit for each age-based group.
     * A value of {@code null} implies no upper age limit for the group.
     */
    private final Integer maxAge;
}
