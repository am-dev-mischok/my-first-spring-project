package com.academy.my_first_spring_project.service;

import com.academy.my_first_spring_project.repository.PersonRepository;
import com.academy.my_first_spring_project.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        // da der Name "null" sein darf, jedoch nicht leer oder nur Leerzeichen enthalten darf,
        // prüfen wir zuerst auf "nicht null" und anschließend auf "isBlank"
        if (person.getName() != null && person.getName().isBlank()) {
            throw new RuntimeException("person may not have a blank name");
        }

        List<String> offensiveNames = new ArrayList<>();
        offensiveNames.add("arsch");
        offensiveNames.add("depp");
        offensiveNames.add("trottel");

        if (person.getName() != null && offensiveNames.contains(person.getName().toLowerCase())) {
            throw new RuntimeException("person has an offensive name");
        }

        if (person.getEmail() != null && person.getEmail().isBlank()) {
            throw new RuntimeException("person may not have a blank email");
        }

        // dieses Regex Pattern ist extra einfach gehalten. Dies ist kein gültiges regex Pattern für die Prüfung einer E-Mail-Adresse.
        // Normalerweise wird für die E-Mail-Adresse ein komplexeres regex Pattern verwendet,
        // welches dann auch die richtigen Sachen bei der E-Mail prüft.
        if (person.getEmail() != null && !person.getEmail().matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+")) {
            throw new RuntimeException("person must have a valid email");
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

        if (person.getAge() < ADULT_MIN_AGE) {
            throw new RuntimeException("person's age too low, is not an adult");
        }

        // da der Name "null" sein darf, jedoch nicht leer oder nur Leerzeichen enthalten darf,
        // prüfen wir zuerst auf "nicht null" und anschließend auf "isBlank"
        if (person.getName() != null && person.getName().isBlank()) {
            throw new RuntimeException("person may not have a blank name");
        }

        if (person.getEmail() != null && person.getEmail().isBlank()) {
            throw new RuntimeException("person may not have a blank email");
        }

        // dieses Regex Pattern ist extra einfach gehalten. Dies ist kein gültiges regex Pattern für die Prüfung einer E-Mail-Adresse.
        // Normalerweise wird für die E-Mail-Adresse ein komplexeres regex Pattern verwendet,
        // welches dann auch die richtigen Sachen bei der E-Mail prüft.
        if (person.getEmail() != null && !person.getEmail().matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+")) {
            throw new RuntimeException("person must have a valid email");
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
