package com.academy.my_first_spring_project;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.repository.PersonRepository;
import com.academy.my_first_spring_project.service.PersonService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat; // import static, damit wir direkt assertThat benutzen können, ohne immer Assertions.assertThat schreiben zu müssen
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    PersonService personService;

    @Autowired
    PersonRepository personRepository;

    @BeforeEach
    public void setup() {
        // things to do before each single test here

        personRepository.deleteAll();
        // instead we should make sure that the test environment uses an in-memory db that is empty for every test anyway
    }

    @Test
    public void createPerson_allInfo_ok() {
        Person personAllInfo = Person.builder()
                .name("Tommy")
                .email("tommy@tim.de")
                .age(27)
                .married(false)
                .build();

        personService.create(personAllInfo);

        List<Person> existingPersons = personService.getAll();

        assertThat(existingPersons).hasSize(1);

        Person existingPerson = existingPersons.get(0);

        assertThat(existingPerson.getId()).isNotNull();
        assertThat(existingPerson.getName()).isEqualTo("Tommy");
        assertThat(existingPerson.getEmail()).isEqualTo("tommy@tim.de");
        assertThat(existingPerson.getAge()).isEqualTo(27);
        assertThat(existingPerson.getMarried()).isFalse();
    }

    @Test
    public void createPerson_someInfoNull_ok() {
        // ...
    }
    @Test
    public void createPerson_noName_badRequest() {
        // ...
    }
    @Test
    public void createPerson_noEmail_badRequest() {
        // ...
    }
    @Test
    public void createPerson_malformedEmail_badRequest() {
        // ...
    }

    @Test
    public void createPerson_notAdult_error() {
        Person personAllInfo = Person.builder()
                .name("Tommy")
                .email("tommy@tim.de")
                .age(17) // we assume that one is adult from the age of 18
                .married(false)
                .build();

        try {
            personService.create(personAllInfo);
            fail("person is underage, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person's age too low, is not an adult");
        }
    }

    @Test
    public void createPerson_freshAdult_ok() {
        Person personAllInfo = Person.builder()
                .name("Tommy")
                .email("tommy@tim.de")
                .age(18) // we assume that one is adult from the age of 18
                .married(false)
                .build();

        try {
            personService.create(personAllInfo);
        } catch (RuntimeException e) {
            fail("person is adult, but there was an exception");
        }
    }

    @Test
    public void createPerson_offensiveName_forbidden() {
        // ...
    }

    @Test
    public void updatePerson_allesWasWirObenBereitsHatten_forbidden() {
        // ...
    }

    @Test
    public void deletePerson_ok() {
        // ...
    }
}
