package com.instagram.clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.instagram.clone.model.User;
import com.instagram.clone.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationRequest request) {
        User user = userService.registerUser(
                request.email,
                request.username,
                request.password,
                request.fullName);
        return ResponseEntity.ok(user);
    }

    // Classe auxiliar para receber os dados de registro
    static class RegistrationRequest {
        private String email;
        private String username;
        private String password;
        private String fullName;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

    }
}
