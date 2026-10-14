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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth service - test unit")
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDto registerDto;
    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterRequestDto("kayke", "kayke.simao@gmail.com", "12345678");
        loginRequestDto = new LoginRequestDto("kayke.simao@gmail.com", "12345678");
    }

    @Nested
    @DisplayName("register()")
    class Register {

        @Test
        @DisplayName("Should register user successfully with encode password and CUSTOMER role")
        void shouldRegisterSuccessfully() {
            when(userRepository.existsByEmail("kayke.simao@gmail.com")).thenReturn(false);
            when(passwordEncoder.encode("12345678")).thenReturn("HASHED_PASSWORD");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UserResponseDto result = authService.register(registerDto);

            assertThat(result.name()).isEqualTo("kayke");
            assertThat(result.email()).isEqualTo("kayke.simao@gmail.com");
            assertThat(result.role()).isEqualTo(Role.CUSTOMER);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());
            verify(passwordEncoder).encode("12345678");

            User userCaptured = captor.getValue();
            assertThat(userCaptured.getEmail()).isEqualTo("kayke.simao@gmail.com");
            assertSame(Role.CUSTOMER, userCaptured.getRole());
            assertThat(userCaptured.getPassword()).isEqualTo("HASHED_PASSWORD");
            assertThat(userCaptured.getPassword()).isNotEqualTo("12345678");
        }

        @Test
        @DisplayName("Should throw exception when user already exists by email")
        void shouldThrowExceptionWhenUserAlreadyExistsByEmail() {
            when(userRepository.existsByEmail("kayke.simao@gmail.com")).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> authService.register(registerDto));

            verify(userRepository).existsByEmail("kayke.simao@gmail.com");
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("login()")
    class Login {

        @Test
        @DisplayName("Should return token when credentials are valid")
        void shouldLoginSuccessfully() {
            User user = new User(1L, "kayke", "kayke.simao@gmail.com", "HASHED_PASSWORD", Role.CUSTOMER);

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(user);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
            when(jwtService.generateToken(user)).thenReturn("fake.jwt.token");
            when(jwtService.getExpirationSeconds()).thenReturn(1800L);

            LoginResponseDto result = authService.login(loginRequestDto);

            assertThat(result.accessToken()).isEqualTo("fake.jwt.token");
            assertThat(result.tokenType()).isEqualTo("Bearer");
        }

        @Test
        @DisplayName("Should throw exception when credentials are invalid")
        void shouldThrowExceptionWhenCredentialsInvalid() {
            loginRequestDto = new LoginRequestDto("kayke.simao@gmail.com", "1234");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid email or password"));

            assertThatThrownBy(() -> authService.login(loginRequestDto))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Invalid email or password");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, never()).generateToken(any());

        }
    }


}
