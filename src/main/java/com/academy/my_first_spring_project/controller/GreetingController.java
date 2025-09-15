package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.entity.Person;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

/**
 * GreetingController is responsible for handling HTTP GET requests to the /greeting endpoint.
 * It supports two types of responses: HTML page as well as JSON object.
 */
@Controller
public class GreetingController {

    /**
     * Handles HTTP GET requests to the /greeting endpoint and returns an HTML page with a greeting message.
     *
     * @param someName The name to be included in the greeting message. If not provided, "World" is used as the default.
     * @param cssFileName The CSS file name to be included for styling the greeting message. If not provided, an empty string is used as the default.
     * @param model The Model object to which the name and cssFileName attributes are added.
     * @return The name of the view template to be rendered, which is "greeting".
     */
    @GetMapping(value = "/greeting", produces = MediaType.TEXT_HTML_VALUE)
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String someName,
                           @RequestParam(name = "css", required = false, defaultValue = "") String cssFileName, Model model) {
        model.addAttribute("name", someName);
        model.addAttribute("cssFileName", cssFileName);
        return "greeting";
    }

    /**
     * Handles HTTP GET requests to the /greeting endpoint and returns a JSON object with a greeting message.
     *
     * @param someName The name to be included in the greeting message. If not provided, "Max" is used as the default.
     * @return A Person object with the specified name and a default id of 7.
     */
    @GetMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person greetingJson(@RequestParam(name = "name", required = false, defaultValue = "Max") String someName) {

        return Person.builder().name(someName).id(7L).build();
    }
}
