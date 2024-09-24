package com.academy.my_first_spring_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyFirstSpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFirstSpringProjectApplication.class, args);
	}


	//
	@Bean
	public CommandLineRunner demo(PersonRepository personRepository) {
		return (args) -> {
			System.out.println("Hallöchen");
			System.out.println(personRepository.count());
//			personRepository.save(Person.builder().name("Mäxchen").build());
//
//			personRepository.deleteById(1L);
//
//			personRepository.save(Person.builder().name("Paul").build());
//			System.out.println(personRepository.count());
//
//			List<Person> persons = personRepository.findAll();
//			for (Person person : persons) {
//				System.out.println(person.getId() + ", " + person.getName());
//			}
		};
	}
}
