package com.academy.my_first_spring_project.service;

import com.academy.my_first_spring_project.repository.PersonRepository;
import com.academy.my_first_spring_project.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final int ADULT_MIN_AGE = 18;

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getById(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (personOptional.isEmpty()) {
            throw new RuntimeException("no person found with this id");
        }

        return personOptional.get();

        // oder so:
//        return personRepository.findById(id)
//                .orElseThrow();
    }

    public Person create(Person person) throws RuntimeException {
        // wir sollten checken, dass das neu zu erstellende Objekt keine id hat, denn diese wird automatisch vergeben von JPA
        if (person.getId() != null) {
            throw new RuntimeException("new person is not allowed to have an id already; let the database assign an available id");
        }
        if (person.getAge() < ADULT_MIN_AGE) {
            throw new RuntimeException("person's age too low, is not an adult");
        }

        return personRepository.save(person);
    }

    public Person update(Person person) throws RuntimeException {
        if (person.getId() == null) {
            throw new RuntimeException("person to save has no id");
        }

        Optional<Person> existingPerson = personRepository.findById(person.getId());

        if (existingPerson.isEmpty()) {
            throw new RuntimeException("person to save has id, but person with this id cannot be found in database");
        }

        Person updatedPerson = personRepository.save(person);
        return updatedPerson;

        // oder so:
//        if (personRepository.existsById(person.getId())) { // throws IllegalArgumentException if id passed into existsById is null
//            return personRepository.save(person);
//        } else {
//            throw new RuntimeException("person to save has id, but person with this id cannot be found in database");
//        }
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    public Person createExamplePerson() {
        return Person.builder()
                .id(7L)
                .name("Paul")
                .build();
    }
}
