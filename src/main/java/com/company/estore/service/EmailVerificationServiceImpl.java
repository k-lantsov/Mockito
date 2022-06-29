package com.company.estore.service;

import com.company.estore.model.User;

public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Override
    public void scheduleEmailConfirmation(User user) {
        //Put user details into email queue
        System.out.println("scheduleEmailConfirmation is called");
    }
}
