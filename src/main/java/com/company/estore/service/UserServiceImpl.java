package com.company.estore.service;

import com.company.estore.data.UserRepository;
import com.company.estore.model.User;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    EmailVerificationService emailVerificationService;

    public UserServiceImpl(UserRepository userRepository, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public User createUser(String firstname,
                           String lastname,
                           String email,
                           String password,
                           String repeatPassword) {

        String id = UUID.randomUUID().toString();

        if (firstname == null || firstname.trim().isEmpty()) {
            throw new IllegalArgumentException("User's firstname is empty");
        }
        if (lastname == null || lastname.trim().isEmpty()) {
            throw new IllegalArgumentException("User's lastname is empty");
        }

        User user = new User(id, firstname, lastname, email);
        boolean isUserCreated;
        try {
            isUserCreated = userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new UserServiceException(ex.getMessage());
        }

        if (!isUserCreated) {
            throw new UserServiceException("Could not create user");
        }

        try {
            emailVerificationService.scheduleEmailConfirmation(user);
        } catch (RuntimeException ex) {
            throw new UserServiceException(ex.getMessage());
        }

        return user;
    }
}
