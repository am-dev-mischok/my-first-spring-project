package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.service.PersonService;
import com.academy.my_first_spring_project.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping
    public List<Person> getPersons() {
        return personService.getAll();
    }

    @GetMapping(value = "/{pId}")
    public Person getPersonById(
            @PathVariable(name="pId") Long personId
    ) {
        return personService.getById(personId);
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personService.create(person);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Person createPersonFromForm(Person person) {
        return personService.create(person);
    }

    @PutMapping
    public Person updatePerson(@RequestBody Person person) {
        return personService.update(person);
    }

    @DeleteMapping
    public void deletePerson(@RequestParam(name="pId") Long personId) {
        personService.delete(personId);
    }
}
