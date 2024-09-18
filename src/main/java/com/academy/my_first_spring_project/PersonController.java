package com.academy.my_first_spring_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping(value = "/person")
    @ResponseBody
    public List<Person> getPersons() {
        List<Person> persons = personRepository.findAll();
        return persons;
    }

    @PostMapping(value = "/person", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person createPerson(@RequestBody Person person) throws RuntimeException {
        // wir sollten checken, dass das neu zu erstellende Objekt keine id hat, denn diese wird automatisch vergeben von JPA
        if (person.getId() != null) {
            throw new RuntimeException("person is not allowed to have an id");
        }

        Person savedPerson = personRepository.save(person);
        return savedPerson;
    }

    @PostMapping(value = "/person", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createPersonFromForm(Person person) {
        // hier sollten wir die Person speichern, aber wir haben noch keine Datenbank
        System.out.println(person.getId());
        System.out.println(person.getName());
        System.out.println(person.getEmail());
        System.out.println(person.getAge());
        System.out.println(person.getMarried());

        // wir leiten den User als Antwort einfach auf die Hauptseite zurück
        return "redirect:/";

        // stattdessen könnten wir den Namen der Person auch mit model.addAttribute("inputName", person.name()); für Thymeleaf bereitstellen und dann unser greeting.html Template zurückgeben
    }
}
