package com.academy.my_first_spring_project;

import jakarta.persistence.*;
import lombok.*;

@Getter // die ersten 5 Annotations sind nur Lombok (Getter, Setter, Builder, ...ArgsConstructor)
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // damit weiß JPA, dass diese Java-Klasse einer Datenbanktabelle entspricht
@Table(name = "person") // so heißt die zur Klasse gehörende Datenbanktabelle
public class Person {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "married")
    private Boolean married;
}
