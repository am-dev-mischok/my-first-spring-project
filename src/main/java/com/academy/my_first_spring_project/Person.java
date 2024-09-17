package com.academy.my_first_spring_project;

import lombok.*;

@Getter
@Setter
@Builder
public class Person {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private Boolean married;
}
