package com.academy.my_first_spring_project;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setup() {
        personRepository.deleteAll();
    }

    @Test
    public void createPersonTest_noError() throws Exception {
        Person person = Person.builder()
                .name("Anna")
                .email("anna@web.de")
                .age(28)
                .married(true)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJsonBody = mapper.writeValueAsString(person);

        assertThat(personRepository.findAll()).hasSize(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJsonBody))
                .andExpect(status().isOk());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(1);

        Person createdPerson = personList.get(0);

        assertThat(createdPerson.getId()).isNotNull();
        assertThat(createdPerson.getName()).isEqualTo("Anna");
        assertThat(createdPerson.getEmail()).isEqualTo("anna@web.de");
        assertThat(createdPerson.getAge()).isEqualTo(28);
        assertThat(createdPerson.getMarried()).isEqualTo(true);
    }

    @Test
    public void updatePersonTest_noError() throws Exception {
        assertThat(personRepository.findAll()).hasSize(0);

        personRepository.save(Person.builder()
                .name("Jochen")
                .email("jo@jo.de")
                .age(35)
                .married(false)
                .build());

        assertThat(personRepository.findAll()).hasSize(1);
        Person person = personRepository.findAll().get(0);

        assertThat(person.getName()).isEqualTo("Jochen");
        assertThat(person.getEmail()).isEqualTo("jo@jo.de");
        assertThat(person.getAge()).isEqualTo(35);
        assertThat(person.getMarried()).isEqualTo(false);

        person.setName("Marie");
        person.setEmail("marie@marie.de");
        person.setAge(20);
        person.setMarried(true);

        ObjectMapper mapper = new ObjectMapper();
        String requestJsonBody = mapper.writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJsonBody))
                .andExpect(status().isOk());

        assertThat(personRepository.findAll()).hasSize(1);
        Person updatedPerson = personRepository.findAll().get(0);

        assertThat(updatedPerson.getName()).isEqualTo("Marie");
        assertThat(updatedPerson.getEmail()).isEqualTo("marie@marie.de");
        assertThat(updatedPerson.getAge()).isEqualTo(20);
        assertThat(updatedPerson.getMarried()).isEqualTo(true);
    }
}
