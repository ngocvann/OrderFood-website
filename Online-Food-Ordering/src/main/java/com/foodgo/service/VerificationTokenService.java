package com.foodgo.service;

import com.foodgo.model.User;
import com.foodgo.model.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createVerificationToken(User user);
    VerificationToken findByToken(String token);
    void delete(VerificationToken token);
}
