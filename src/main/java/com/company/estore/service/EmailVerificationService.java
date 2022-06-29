package com.company.estore.service;

import com.company.estore.model.User;

public interface EmailVerificationService {
    void scheduleEmailConfirmation(User user);
}
