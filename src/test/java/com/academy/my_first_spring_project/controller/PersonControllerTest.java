package com.academy.my_first_spring_project.controller;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.repository.PersonRepository;
import com.academy.my_first_spring_project.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PersonControllerTest {

    @SpyBean
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonController personController;

    private MockMvc mockMvc;

    private Person person;
    private List<Person> allPersons;
    private long expectedSizePersons;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        person = Person.builder().name("John Doe").email("john@example.com").age(20).married(false).build();
        person = personService.create(person);
        allPersons = personService.getAll();
        expectedSizePersons = personRepository.count();
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetPersons() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedSizePersons));
        verify(personService, times(2)).getAll();
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetPerson() throws Exception {
        mockMvc.perform(get("/person/" + person.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.name").value(person.getName()))
                .andExpect(jsonPath("$.email").value(person.getEmail()))
                .andExpect(jsonPath("$.age").value(person.getAge()))
                .andExpect(jsonPath("$.married").value(person.getMarried()));
        verify(personService, times(1)).getById(person.getId());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testCreatePerson() throws Exception {
        Person newPerson = Person.builder().name("Jane Doe").email("jane@example.com").age(22).married(true).build();

        ObjectMapper mapper = new ObjectMapper();
        String personJson = mapper.writeValueAsString(newPerson);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testCreatePersonFromForm() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", person.getName())
                        .param("email", person.getEmail())
                        .param("age", person.getAge().toString())
                        .param("married", person.getMarried().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(personService, times(2)).create(any(Person.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testUpdatePerson() throws Exception {
        person.setName("Updated name");;
        ObjectMapper mapper = new ObjectMapper();
        String updatedPersonJson = mapper.writeValueAsString(person);

        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(updatedPersonJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"));

        verify(personService, times(1)).update(any(Person.class));
    }

    @Test
    @WithMockUser
    public void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/person")
                .param("pId", person.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedSizePersons - 1, personRepository.count()));

        verify(personService, times(1)).delete(person.getId());
    }
}