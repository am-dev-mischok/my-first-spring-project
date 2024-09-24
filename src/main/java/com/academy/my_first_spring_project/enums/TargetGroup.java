package com.academy.my_first_spring_project.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TargetGroup {
    EVERYONE("Alle", null, null),
    BABYS("Babys", null, 3),
    CHILDREN("Kinder", 4, 12),
    TEENS("Jugendliche", 13, 17),
    ADULTS("Erwachsene", 18, 59),
    OLD("Alte Leute", 60, null);

    private final String label;
    private final Integer minAge;
    private final Integer maxAge;
}
