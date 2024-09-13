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
            Model model
    ) {
        model.addAttribute("inputName", someName);
        return "greeting";
    }
}
