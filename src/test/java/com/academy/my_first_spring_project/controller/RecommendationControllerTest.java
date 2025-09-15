package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.entity.Recommendation;
import com.academy.my_first_spring_project.enums.TargetGroup;
import com.academy.my_first_spring_project.repository.PersonRepository;
import com.academy.my_first_spring_project.repository.RecommendationRepository;
import com.academy.my_first_spring_project.service.PersonService;
import com.academy.my_first_spring_project.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RecommendationControllerTest {

//    @Autowired
    @SpyBean
    private RecommendationService recommendationService;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    private Recommendation recommendation;
    private Recommendation recommendation1;
    private List<Recommendation> allRecommendations;
    private long expectedSizeRecommendations;

    private Person person;
    private List<Person> allPersons;
    private int expectedSizePersons;

    @BeforeEach
    public void setup() {
        person = Person.builder().name("John Doe").email("john@example.com").age(20).married(false).build();
        person = personService.create(person);
        allPersons = personService.getAll();
        expectedSizePersons = allPersons.size();

        recommendation = Recommendation.builder().url("example.com").name("rec1").rating(9).description("lorem ipsum").submitted(LocalDateTime.of(2024, 11, 1, 12, 0)).targetGroup(TargetGroup.EVERYONE).discoverer(person).build();
        recommendation = recommendationService.create(recommendation);
        recommendation1 = Recommendation.builder().url("example.com").name("rec2").rating(6).description("lorem ipsum")
                .submitted(LocalDateTime.of(2024, 11, 5, 12, 0))
                .targetGroup(TargetGroup.EVERYONE).discoverer(person)
                .build();
        recommendation1 = recommendationService.create(recommendation1);
        allRecommendations = recommendationService.getAll();
        expectedSizeRecommendations = recommendationRepository.count();
    }


    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetRecommendations() throws Exception {

        mockMvc.perform(get("/recommendation"))
                .andExpect(status().isOk())
                .andExpect(view().name("recommendations"))
                .andExpect(model().attributeExists("allRecommendations"))
                .andExpect(model().attributeExists("recommendation"))
                .andExpect(model().attributeExists("targetGroups"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attributeExists("mode"))

                .andExpect(model().attribute("allRecommendations", everyItem(hasProperty("id", is(
                        in(allRecommendations.stream().map(Recommendation::getId).toList()))))))
                .andExpect(model().attribute("recommendation", hasProperty("id", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("url", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("name", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("rating", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("description", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("submitted", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("targetGroup", is(nullValue()))))
                .andExpect(model().attribute("recommendation", hasProperty("discoverer", hasProperty("id", is(nullValue())))))
                .andExpect(model().attribute("targetGroups", TargetGroup.values()))
                .andExpect(model().attribute("persons", everyItem(hasProperty("id", is(in(allPersons.stream().map(Person::getId).toList()))))))
                .andExpect(model().attribute("persons", hasSize(expectedSizePersons)))
                .andExpect(model().attribute("mode", "create"))
        ;
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetRecommendation() throws Exception {

        mockMvc.perform(get("/recommendation/" + recommendation.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("recommendation"))
                .andExpect(model().attributeExists("recommendation"))
                .andExpect(model().attributeExists("targetGroups"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attributeExists("mode"))

                .andExpect(model().attribute("recommendation", hasProperty("id", is(recommendation.getId()))))
                .andExpect(model().attribute("recommendation", hasProperty("url", is("example.com"))))
                .andExpect(model().attribute("recommendation", hasProperty("name", is("rec1"))))
                .andExpect(model().attribute("recommendation", hasProperty("rating", is(9))))
                .andExpect(model().attribute("recommendation", hasProperty("description", is("lorem ipsum"))))
                .andExpect(model().attribute("recommendation", hasProperty("submitted", is(LocalDateTime.of(2024, 11, 1, 12, 0)))))
                .andExpect(model().attribute("recommendation", hasProperty("targetGroup", is(TargetGroup.EVERYONE))))
                .andExpect(model().attribute("recommendation", hasProperty("discoverer", hasProperty("id", is(person.getId())))))
                .andExpect(model().attribute("targetGroups", TargetGroup.values()))
                .andExpect(model().attribute("persons", everyItem(hasProperty("id", in(allPersons.stream().map(Person::getId).toList())))))
                .andExpect(model().attribute("persons", hasSize(expectedSizePersons)))
                .andExpect(model().attribute("mode", "view"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetEditRecommendation() throws Exception {

        mockMvc.perform(get("/recommendation/edit/" + recommendation.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("recommendation"))
                .andExpect(model().attributeExists("recommendation"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attributeExists("mode"))

                .andExpect(model().attribute("recommendation", hasProperty("id", is(recommendation.getId()))))
                .andExpect(model().attribute("recommendation", hasProperty("url", is("example.com"))))
                .andExpect(model().attribute("recommendation", hasProperty("name", is("rec1"))))
                .andExpect(model().attribute("recommendation", hasProperty("rating", is(9))))
                .andExpect(model().attribute("recommendation", hasProperty("description", is("lorem ipsum"))))
                .andExpect(model().attribute("recommendation", hasProperty("submitted", is(LocalDateTime.of(2024, 11, 1, 12, 0)))))
                .andExpect(model().attribute("recommendation", hasProperty("targetGroup", is(TargetGroup.EVERYONE))))
                .andExpect(model().attribute("recommendation", hasProperty("discoverer", hasProperty("id", is(person.getId())))))
                .andExpect(model().attribute("persons", everyItem(hasProperty("id", in(allPersons.stream().map(Person::getId).toList())))))
                .andExpect(model().attribute("persons", hasSize(expectedSizePersons)))
                .andExpect(model().attribute("mode", "edit"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testCreateRecommendation() throws Exception {

        mockMvc.perform(post("/recommendation/create")
                        .param("url", "example.de")
                        .param("name", "rec2")
                        .param("rating", "4")
                        .param("description", "lorem ipsum dolor sit amet")
                        .param("targetGroup", "EVERYONE")
                        .param("discoverer.id", person.getId().toString()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recommendation"))
                .andExpect(result -> assertEquals(expectedSizeRecommendations + 1, recommendationRepository.count()));

        verify(recommendationService, times(3)).create(ArgumentMatchers.any(Recommendation.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testUpdateRecommendation() throws Exception {

        mockMvc.perform(put("/recommendation/update/" + recommendation.getId())
                        .param("url", "example.de")
                        .param("name", "rec1 updated")
                        .param("rating", "4")
                        .param("description", "lorem ipsum dolor sit amet")
                        .param("targetGroup", "TEENS")
                        .param("discoverer.id", person.getId().toString()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recommendation"));

        verify(recommendationService, times(1)).update(ArgumentMatchers.any(Recommendation.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testDeleteRecommendation() throws Exception {

        mockMvc.perform(delete("/recommendation/delete").param("id", recommendation1.getId().toString()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recommendation"))
                .andExpect(result -> assertEquals(expectedSizeRecommendations - 1, recommendationRepository.count()));

        verify(recommendationService, times(1)).delete(recommendation1.getId());
    }
}