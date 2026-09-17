package com.generation.planificadortareasbackend.controller;

import com.generation.planificadortareasbackend.dto.LoginRequest;
import com.generation.planificadortareasbackend.dto.RegisterRequest;
import com.generation.planificadortareasbackend.dto.ResetPasswordRequest;
import com.generation.planificadortareasbackend.dto.UpdateProfileRequest;
import com.generation.planificadortareasbackend.dto.UserResponse;
import com.generation.planificadortareasbackend.exception.EmailAlreadyExistsException;
import com.generation.planificadortareasbackend.exception.InvalidCredentialsException;
import com.generation.planificadortareasbackend.exception.ResourceNotFoundException;
import com.generation.planificadortareasbackend.model.User;
import com.generation.planificadortareasbackend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException("Ya existe una cuenta registrada con este correo.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthDate())
                .build();

        return toResponse(userRepository.save(user));
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new InvalidCredentialsException("Correo o contraseña incorrectos."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Correo o contraseña incorrectos.");
        }

        return toResponse(user);
    }

    @GetMapping("/profile/{id}")
    public UserResponse getProfile(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return toResponse(user);
    }

    @PutMapping("/reset-password")
    public UserResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new ResourceNotFoundException("No existe una cuenta con este correo."));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return toResponse(userRepository.save(user));
    }

    @PutMapping("/profile/{id}")
    public UserResponse updateProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        String newEmail = request.getEmail().trim().toLowerCase();
        if (!newEmail.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new EmailAlreadyExistsException("Ya existe una cuenta registrada con este correo.");
        }

        user.setName(request.getName());
        user.setEmail(newEmail);
        user.setBirthDate(request.getBirthDate());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(userRepository.save(user));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .build();
    }
}
