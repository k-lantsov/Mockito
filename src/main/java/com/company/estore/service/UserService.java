package com.company.estore.service;

import com.company.estore.model.User;

public interface UserService {
    User createUser(String firstname,
                    String lastname,
                    String email,
                    String password,
                    String repeatPassword);
}
