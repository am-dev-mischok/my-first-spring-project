package com.academy.my_first_spring_project.service;


import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Person entities.
 * Provides methods to perform CRUD operations on Person objects.
 * Utilizes a PersonRepository for database interactions.
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /**
     * Retrieves all Person entities from the repository.
     *
     * @return a list of all Person entities.
     */
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    /**
     * Retrieves a Person entity by its ID.
     *
     * @param id the ID of the Person to retrieve
     * @return the Person entity with the specified ID
     * @throws RuntimeException if the Person with the specified ID is not found
     */
    public Person getById(Long id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isEmpty()) {
            throw new RuntimeException("person not found");
        }

        return person.get();
    }

    /**
     * Creates and saves a new Person entity.
     * If the Person entity already has an id, a RuntimeException is thrown.
     *
     * @param person The Person entity to be created. It must not have an id.
     * @return The saved Person entity with its new id assigned.
     * @throws RuntimeException if the Person entity to be saved already has an id.
     */
    public Person create(Person person) throws RuntimeException {
        if (person.getId() == null) {
            return personRepository.save(person);
        } else {
            throw new RuntimeException("person to save has an id");
        }
    }

    /**
     * Updates an existing Person entity in the repository.
     * Throws a RuntimeException if the Person does not have an ID or if the Person does not exist in the repository.
     *
     * @param person the Person entity to be updated. Must contain a valid ID.
     * @return the updated Person entity
     * @throws RuntimeException if the Person does not have an ID or does not exist in the repository
     */
    public Person update(Person person) throws RuntimeException {
        if (person.getId() == null) {
            throw new RuntimeException("person to save has no id");
        }
        Optional<Person> existingPerson = personRepository.findById(person.getId());

        if (existingPerson.isEmpty()) {
            throw new RuntimeException("person to update does not exist");
        }

        return personRepository.save(person);
    }

    /**
     * Deletes a person entity with the given ID from the repository.
     *
     * @param id the ID of the person entity to be deleted
     */
    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
