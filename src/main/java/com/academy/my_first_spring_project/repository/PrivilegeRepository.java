package com.academy.my_first_spring_project.repository;

import com.academy.my_first_spring_project.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository  extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);
}
