package com.academy.my_first_spring_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void greetingTest_withParameter_noError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/greeting")
                        .param("n", "Tim")
                        .param("css", "style-alex")
                        .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"))
                .andExpect(model().attributeExists("inputName"))
                .andExpect(model().attribute("inputName", "Tim"))
                .andExpect(model().attributeExists("cssFileName"))
                .andExpect(model().attribute("cssFileName", "style-alex"));
    }

    @Test
    public void greetingTest_noParameter_noError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/greeting")
                        .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"))
                .andExpect(model().attributeExists("inputName"))
                .andExpect(model().attribute("inputName", "Welt"))
                .andExpect(model().attributeExists("cssFileName"))
                .andExpect(model().attribute("cssFileName", ""));
    }

    @Test
    public void personJsonTest_noError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/greeting")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].*", hasSize(6)))
                .andExpect(jsonPath("$.[0].id").value(7))
                .andExpect(jsonPath("$.[0].name").value("Paul"))
                .andExpect(jsonPath("$.[0].email").isEmpty())
                .andExpect(jsonPath("$.[0].age").isEmpty())
                .andExpect(jsonPath("$.[0].married").isEmpty())
                .andExpect(jsonPath("$.[0].discoveredRecommendations").isEmpty())
                .andExpect(jsonPath("$.[1].*", hasSize(6)))
                .andExpect(jsonPath("$.[1].id").value(8))
                .andExpect(jsonPath("$.[1].name").value("Anna"))
                .andExpect(jsonPath("$.[1].email").isEmpty())
                .andExpect(jsonPath("$.[1].age").isEmpty())
                .andExpect(jsonPath("$.[1].married").isEmpty())
                .andExpect(jsonPath("$.[1].discoveredRecommendations").isEmpty());
    }
}
