package com.instagram.clone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.clone.dto.ChangeEmailDTO;
import com.instagram.clone.dto.ChangePasswordDTO;
import com.instagram.clone.dto.LoginRequestDTO;
import com.instagram.clone.dto.RegisterRequestDTO;
import com.instagram.clone.dto.ResponseDTO;
import com.instagram.clone.infra.security.TokenService;
import com.instagram.clone.model.User;
import com.instagram.clone.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        User user = this.repository.findByUsername(body.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = this.repository.findByEmail(body.email());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setUsername(body.username());
            newUser.setFullName(body.fullName());
            newUser.setBio("Ainda não informado");
            newUser.setProfilePicture("https://via.placeholder.com/150");
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        User user = repository.findByUsername(changePasswordDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(null); // Senha antiga incorreta
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        repository.save(user);

        // Invalida o token antigo e gera um novo token
        String token = tokenService.generateToken(user);

        // Retorna o novo token no header da resposta
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }
    @PutMapping("/change-email")
    public ResponseEntity<Void> changeEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
        User user = repository.findByUsername(changeEmailDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(changeEmailDTO.getEmail());
        repository.save(user);

        return ResponseEntity.ok().build();
    }
}