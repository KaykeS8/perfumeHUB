package com.simao.perfumehub.controllers;

import com.simao.perfumehub.dtos.auth.LoginRequestDto;
import com.simao.perfumehub.dtos.auth.LoginResponseDto;
import com.simao.perfumehub.dtos.auth.RegisterRequestDto;
import com.simao.perfumehub.dtos.auth.UserResponseDto;
import com.simao.perfumehub.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(dto));
    }
}
