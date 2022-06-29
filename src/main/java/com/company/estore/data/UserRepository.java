package com.company.estore.data;

import com.company.estore.model.User;

public interface UserRepository {
    boolean save(User user);
}
