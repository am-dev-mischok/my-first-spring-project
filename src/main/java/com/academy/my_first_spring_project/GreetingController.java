package com.academy.my_first_spring_project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name="n", required = false, defaultValue = "Welt") String someName,
            @RequestParam(name="css", required = false, defaultValue = "") String cssString,
            Model model
    ) {
        model.addAttribute("inputName", someName);
        model.addAttribute("cssFileName", cssString);
        return "greeting";
    }

    @GetMapping("/person")
    @ResponseBody
    public Person personJson() {
        // Objekt erstellen, zB aus Datenbank holen, sonstige Business-Logik
        Person person = Person.builder()
                .id(7L)
                .name("Paul")
                .build();
        // zB stattdessen so eine Business-Logik, die mir eine Person mit Id 7 aus der Datenbank holt:
//        Person person = personRepository.findById(7);

        return person;
    }
}
