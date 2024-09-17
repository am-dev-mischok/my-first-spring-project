package com.academy.my_first_spring_project;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {

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

    @PostMapping(value = "/person")
    @ResponseBody
    public Person createPersonFromJson(@RequestBody Person person) {
        // hier sollten wir die Person speichern, aber wir haben noch keine Datenbank
        System.out.println(person.getId());
        System.out.println(person.getName());
        System.out.println(person.getEmail());
        System.out.println(person.getAge());
        System.out.println(person.getMarried());

        // wir geben die Person als JSON zurück
        return person;
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
