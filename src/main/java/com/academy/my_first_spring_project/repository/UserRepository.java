package com.academy.my_first_spring_project.repository;

import com.academy.my_first_spring_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
