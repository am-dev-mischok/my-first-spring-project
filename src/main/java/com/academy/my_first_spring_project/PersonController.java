package com.academy.my_first_spring_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping(value = "/person")
    @ResponseBody
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @GetMapping(value = "/person/{pId}")
    @ResponseBody
    public Person getPersonById(
            @PathVariable(name="pId") Long personId
    ) {
        return personRepository.findById(personId)
                .orElseThrow();
    }

    @PostMapping(value = "/person", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public Person createPersonFromForm(Person person) {
        if (person.getId() != null) {
            throw new RuntimeException("new person is not allowed to have an id already; let the database give an available id");
        }
        Person personWithId = personRepository.save(person);
        return personWithId;
    }
}
