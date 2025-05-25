package com.foodgo.service;

import com.foodgo.model.User;
import com.foodgo.model.VerificationToken;
import com.foodgo.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenServiceImplementation implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        return verificationTokenRepository.save(token);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }
}
