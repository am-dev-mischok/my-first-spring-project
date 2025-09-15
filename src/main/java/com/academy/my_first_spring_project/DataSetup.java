package com.academy.my_first_spring_project;

import com.academy.my_first_spring_project.entity.Privilege;
import com.academy.my_first_spring_project.entity.User;
import com.academy.my_first_spring_project.repository.PrivilegeRepository;
import com.academy.my_first_spring_project.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataSetup {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        initPrivilege();
        initUsers();
    }

    private void initPrivilege() {
        savePrivilegeIfNotExists("USER");
        savePrivilegeIfNotExists("ADMIN");
        savePrivilegeIfNotExists("READ_ONLY");
        savePrivilegeIfNotExists("READ");
        savePrivilegeIfNotExists("WRITE");
        savePrivilegeIfNotExists("EDIT");
        savePrivilegeIfNotExists("DELETE");
    }

    private void savePrivilegeIfNotExists(String privilegeName) {
        if (privilegeRepository.findByName(privilegeName) == null) {
            privilegeRepository.save(new Privilege(privilegeName));
        }
    }

    private void initUsers() {
        final Privilege userPrivilege = privilegeRepository.findByName("USER");
        final Privilege adminPrivilege = privilegeRepository.findByName("ADMIN");
        final Privilege readOnlyPrivilege = privilegeRepository.findByName("READ_ONLY");
        final Privilege readPrivilege = privilegeRepository.findByName("READ");
        final Privilege writePrivilege = privilegeRepository.findByName("WRITE");
        final Privilege editPrivilege = privilegeRepository.findByName("EDIT");
        final Privilege deletePrivilege = privilegeRepository.findByName("DELETE");

        createUserIfNotExist("admin", "admin", new HashSet<>(Arrays.asList(adminPrivilege,
                userPrivilege)));
        createUserIfNotExist("userR", "userR", new HashSet<>(Arrays.asList(userPrivilege,
                readOnlyPrivilege)));
        createUserIfNotExist("userCR", "userCR", new HashSet<>(Arrays.asList(userPrivilege,
                writePrivilege, readPrivilege)));
        createUserIfNotExist("userCRU", "userCRU", new HashSet<>(Arrays.asList(userPrivilege,
                readPrivilege, writePrivilege, editPrivilege)));
        createUserIfNotExist("userCRUD", "userCRUD", new HashSet<>(Arrays.asList(userPrivilege,
                readPrivilege, writePrivilege, editPrivilege, deletePrivilege)));
    }

    private void createUserIfNotExist(String username, String password, HashSet<Privilege> privileges) {
        if (userRepository.findByUsername(username) == null) {
            final User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setPrivileges(privileges);
            userRepository.save(user);
        }
    }
}
