package com.academy.my_first_spring_project;

import com.academy.my_first_spring_project.entity.Person;
import com.academy.my_first_spring_project.repository.PersonRepository;
import com.academy.my_first_spring_project.service.PersonService;
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

    private Person createPerson() {
        Person person = Person.builder()
                .name("Joseph")
                .email("jo@jo.de")
                .age(56)
                .married(false)
                .build();
        return personRepository.save(person);
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
        Person personAllInfo = Person.builder()
                .name(null)
                .email(null)
                .age(20)
                .married(null)
                .build();

        personService.create(personAllInfo);

        List<Person> existingPersons = personService.getAll();

        assertThat(existingPersons).hasSize(1);

        Person existingPerson = existingPersons.get(0);

        assertThat(existingPerson.getId()).isNotNull();
        assertThat(existingPerson.getName()).isNull();
        assertThat(existingPerson.getEmail()).isNull();
        assertThat(existingPerson.getAge()).isEqualTo(20);
        assertThat(existingPerson.getMarried()).isNull();
    }

    @Test
    public void createPerson_noName_badRequest() {
        Person personAllInfo = Person.builder()
                .name("")
                .email("e@mail.com")
                .age(20)
                .married(false)
                .build();

        try {
            personService.create(personAllInfo);
            fail("person has a blank name, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person may not have a blank name");
        }
    }

    @Test
    public void createPerson_noEmail_badRequest() {
        Person personAllInfo = Person.builder()
                .name("Michael")
                .email(" ")
                .age(20)
                .married(true)
                .build();

        try {
            personService.create(personAllInfo);
            fail("person has a blank email, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person may not have a blank email");
        }
    }

    @Test
    public void createPerson_malformedEmail_badRequest() {
        Person person = Person.builder()
                .name("Hans")
                .email("memaildas.de")
                .age(25)
                .married(false)
                .build();

        try {
            personService.create(person);
            fail("person has no valid email, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person must have a valid email");
        }
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
        Person personAllInfo = Person.builder()
                .name("Arsch")
                .email("tommy@tim.de")
                .age(18) // we assume that one is adult from the age of 18
                .married(false)
                .build();

        try {
            personService.create(personAllInfo);
            fail("person has an offensive name, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person has an offensive name");
        }
    }

    @Test
    public void updatePerson_allInfo_ok() {
        Person person = createPerson();

        assertThat(person.getName()).isEqualTo("Joseph");
        assertThat(person.getEmail()).isEqualTo("jo@jo.de");
        assertThat(person.getAge()).isEqualTo(56);
        assertThat(person.getMarried()).isFalse();

        Person personAllInfo = Person.builder()
                .id(person.getId())
                .name("Klaus")
                .email("kl@kl.de")
                .age(25)
                .married(true)
                .build();

        Person updatedPerson = personService.update(personAllInfo);

        assertThat(updatedPerson.getName()).isEqualTo("Klaus");
        assertThat(updatedPerson.getEmail()).isEqualTo("kl@kl.de");
        assertThat(updatedPerson.getAge()).isEqualTo(25);
        assertThat(updatedPerson.getMarried()).isTrue();
    }

    @Test
    public void updatePerson_someInfoNull_ok() {
        Person person = createPerson();

        assertThat(person.getName()).isEqualTo("Joseph");
        assertThat(person.getEmail()).isEqualTo("jo@jo.de");
        assertThat(person.getAge()).isEqualTo(56);
        assertThat(person.getMarried()).isFalse();

        Person personAllInfo = Person.builder()
                .id(person.getId())
                .name(null)
                .email(null)
                .age(56)
                .married(null)
                .build();

        Person updatedPerson = personService.update(personAllInfo);

        assertThat(updatedPerson.getId()).isEqualTo(person.getId());
        assertThat(updatedPerson.getName()).isNull();
        assertThat(updatedPerson.getEmail()).isNull();
        assertThat(updatedPerson.getAge()).isEqualTo(56);
        assertThat(updatedPerson.getMarried()).isNull();
    }

    @Test
    public void updatePerson_noName_badRequest() {
        Person person = createPerson();
        Person personAllInfo = Person.builder()
                .id(person.getId())
                .name("")
                .email("e@mail.com")
                .age(20)
                .married(false)
                .build();

        try {
            personService.update(personAllInfo);
            fail("person has a blank name, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person may not have a blank name");
        }
    }

    @Test
    public void updatePerson_noEmail_badRequest() {
        Person person = createPerson();
        Person personAllInfo = Person.builder()
                .id(person.getId())
                .name("Michael")
                .email(" ")
                .age(20)
                .married(true)
                .build();

        try {
            personService.update(personAllInfo);
            fail("person has a blank email, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person may not have a blank email");
        }
    }

    @Test
    public void updatePerson_malformedEmail_badRequest() {
        Person person = createPerson();
        Person personNew = Person.builder()
                .id(person.getId())
                .name("Hans")
                .email("me@mail@dasdde")
                .age(25)
                .married(false)
                .build();

        try {
            personService.update(personNew);
            fail("person has no valid email, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person must have a valid email");
        }
    }

    @Test
    public void updatePerson_notAdult_error() {
        Person person = createPerson();
        Person personAllInfo = Person.builder()
                .id(person.getId())
                .name("Tommy")
                .email("tommy@tim.de")
                .age(17) // we assume that one is adult from the age of 18
                .married(false)
                .build();

        try {
            personService.update(personAllInfo);
            fail("person is underage, but there was no exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("person's age too low, is not an adult");
        }
    }

    @Test
    public void updatePerson_freshAdult_ok() {
        Person person = createPerson();
        Person personAllInfo = Person.builder()
                .id(person.getId())
                .name("Tommy")
                .email("tommy@tim.de")
                .age(18) // we assume that one is adult from the age of 18
                .married(false)
                .build();

        try {
            personService.update(personAllInfo);
        } catch (RuntimeException e) {
            fail("person is adult, but there was an exception");
        }
    }

    @Test
    public void deletePerson_ok() {
        Person person = createPerson();
        Person person2 = createPerson();
        Person person3 = createPerson();

        assertThat(personRepository.findAll().size()).isEqualTo(3);

        personService.delete(person2.getId());

        assertThat(personRepository.findAll().size()).isEqualTo(2);
        assertThat(personRepository.findById(person2.getId())).isEmpty();
    }
}
