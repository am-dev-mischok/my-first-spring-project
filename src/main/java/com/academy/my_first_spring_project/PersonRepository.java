package com.academy.my_first_spring_project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
//
//    // returns all Persons with name exactly equal to exactName
//    List<Person> findByName(String exactName);
//
//    // returns all Persons with name equal to one of the names passed in the List of Strings manyNames
//    List<Person> findByNameIn(Collection<String> manyNames);
//
//    // returns all Persons with email not included in one of the emails passed in the List of Strings manyEmails
//    List<Person> findByEmailNotIn(Collection<String> manyEmails);
//
//    // returns all Persons with name exactly equal to exactName or email exactly equal to exactEmail
//    List<Person> findByNameOrEmail(String exactName, String exactEmail);
//
//    // returns all Persons with an email that ends with the String passed in the variable "suffix"
//    List<Person> findByEmailEndingWith(String suffix);
//
//    // returns all Persons that are exactly as old as "exactAge". Then examples for finding all persons that are older than minAgeExcluded, and then all that are at most maxAgeIncluded years old:
//    List<Person> findByAge(Integer exactAge);
//    List<Person> findByAgeGreaterThan(Integer minAgeExcluded);
//    List<Person> findByAgeLessThanEqual(Integer maxAgeIncluded);
//
//    // here we combine previous conditions
//    List<Person> findByAgeLessThanEqualAndEmailEndingWith(Integer maxAgeIncluded, String suffix);
//
//    // returns all Persons that are not married and who are older than minAgeExcluded years old. Afterwards almost the same Query, but with Sorting by email.
//    List<Person> findByMarriedFalseAndAgeGreaterThan(Integer minAgeExcluded);
//    List<Person> findByMarriedFalseAndAgeGreaterThanOrderByEmail(Integer minAgeExcluded);
//
//    // this returns true, if there is a Person whose married Boolean is the same as isMarried, and is exactAge years old. Else returns false:
//    Boolean existsByMarriedAndAge(Boolean isMarried, Integer exactAge);
//
//    // delete all persons with a specific condition and return the ids of the deleted entities. Here we delete all Persons with age less than or equal maxAgeIncluded years old, since we decided that they cannot use our website.
//    List<Long> deleteByAgeLessThanEqual(Integer maxAgeIncluded);
//
//    // this time we also delete everyone who has no age saved, just to be sure
//    List<Long> deleteByAgeIsNullOrAgeLessThanEqual(Integer maxAgeIncluded);
}
