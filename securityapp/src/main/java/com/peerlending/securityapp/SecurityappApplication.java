package com.peerlending.securityapp;

import com.peerlending.securityapp.user.model.Repository.UserRepository;
import com.peerlending.securityapp.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityappApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository ;

    public static void main(String[] args) {
        SpringApplication.run(SecurityappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User("John" ,"12345" ));
        userRepository.save(new User("Peter" ,"12345" ));
        userRepository.save(new User("Henry" ,"12345" ));

    }
}
