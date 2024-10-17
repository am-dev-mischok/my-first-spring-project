package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GreetingController {

    @Autowired
    PersonService personService;

    @GetMapping(value = "/greeting", produces = MediaType.TEXT_HTML_VALUE)
    public String greeting(
            @RequestParam(name="n", required = false, defaultValue = "Welt") String someName,
            @RequestParam(name="css", required = false, defaultValue = "") String cssString,
            Model model
    ) {
        model.addAttribute("inputName", someName);
        model.addAttribute("cssFileName", cssString);
        return "greeting";
    }

    @GetMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Person> personJson() {
        // Objekt erstellen, z. B. aus Datenbank holen, sonstige Business-Logik
        List<Person> personList = personService.createExamplePersons();
        // zB stattdessen so eine Business-Logik, die mir eine Person mit Id 7 aus der Datenbank holt:
        // Person person = personRepository.findById(7);

        return personList;
    }
}
