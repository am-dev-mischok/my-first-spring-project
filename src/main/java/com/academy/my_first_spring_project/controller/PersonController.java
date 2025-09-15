package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing Person entities.
 * Provides endpoints for retrieving, creating, updating, and deleting persons.
 */
@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    PersonService personService;

    /**
     * Retrieves a list of all Person entities.
     *
     * @return a list of Person objects
     */
    @GetMapping
    public List<Person> getPersons() {
        return personService.getAll();
    }

    /**
     * Retrieves a Person entity based on the provided person ID.
     *
     * @param personId the ID of the person to retrieve
     * @return the Person object corresponding to the given ID
     */
    @GetMapping(value = "/{pId}")
    public Person getPerson(@PathVariable(name="pId") Long personId) {
        return personService.getById(personId);
    }

    /**
     * Creates a new Person entity.
     *
     * @param person the Person object to be created
     * @return the created Person object
     */
    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personService.create(person);
    }

    /**
     * Creates a new Person entity using data from a form submission.
     *
     * @param person the Person object populated from the form data
     * @return the created Person object
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Person createPersonFromForm(Person person) {
        return personService.create(person);
    }

    /**
     * Updates an existing Person entity.
     *
     * @param person the Person object containing updated data
     * @return the updated Person object
     */
    @PutMapping(value = "")
    public Person updatePerson(@RequestBody Person person) {
        return personService.update(person);
    }

    /**
     * Deletes a Person entity based on the provided person ID.
     *
     * @param personId the ID of the person to delete
     */
    @DeleteMapping
    public void deletePerson(@RequestParam(name="pId") Long personId) {
        personService.delete(personId);
    }
}
