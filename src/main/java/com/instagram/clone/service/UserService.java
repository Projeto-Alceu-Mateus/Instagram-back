package com.instagram.clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.clone.model.User;
import com.instagram.clone.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String username, String password, String fullName) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Criptografar a senha antes de salvar
        newUser.setFullName(fullName);
        return userRepository.save(newUser);
    }
}