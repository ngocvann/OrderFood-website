package com.foodgo.service;

import com.foodgo.model.User;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
}

