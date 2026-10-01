package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.auth.LoginRequestDto;
import com.simao.perfumehub.dtos.auth.LoginResponseDto;
import com.simao.perfumehub.dtos.auth.RegisterRequestDto;
import com.simao.perfumehub.dtos.auth.UserResponseDto;
import com.simao.perfumehub.entities.User;
import com.simao.perfumehub.entities.enums.Role;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.repositories.UserRepository;
import com.simao.perfumehub.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponseDto register(RegisterRequestDto dto) {
        String email = dto.email().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("Email already registered: " + email);
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.CUSTOMER);

        User userSaved = userRepository.save(user);

        return new UserResponseDto(userSaved.getId(), userSaved.getName(), userSaved.getEmail(), userSaved.getRole());
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email().toLowerCase(), dto.password())
        );
        User user = (User) authentication.getPrincipal();
        return new LoginResponseDto(jwtService.generateToken(user), "Bearer", jwtService.getExpirationSeconds());
    }
}
