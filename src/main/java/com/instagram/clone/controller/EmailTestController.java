package com.instagram.clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instagram.clone.service.EmailService;
import org.springframework.http.ResponseEntity;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String email) {
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("Test email sent to: " + email);
    }
}
