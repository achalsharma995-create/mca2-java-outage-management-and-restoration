package com.omrs.service;

import com.omrs.dto.auth.JwtAuthResponse;
import com.omrs.dto.auth.LoginRequest;
import com.omrs.dto.auth.SignupRequest;
import com.omrs.entity.Role;
import com.omrs.entity.RoleName;
import com.omrs.entity.User;
import com.omrs.exception.BadRequestException;
import com.omrs.repository.RoleRepository;
import com.omrs.repository.UserRepository;
import com.omrs.security.JwtTokenProvider;
import com.omrs.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private Role customerRole;

    @BeforeEach
    void setUp() {
        customerRole = new Role(RoleName.ROLE_CUSTOMER, "Customer role");
        customerRole.setId(4L);

        sampleUser = new User("johndoe", "john@example.com", "encodedPass", "John Doe", "+1-555-0100");
        sampleUser.setId(10L);
        sampleUser.setRoles(Collections.singleton(customerRole));
    }

    @Test
    @DisplayName("Test 1: Authenticate user successfully and generate JWT token")
    void testAuthenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest("johndoe", "password123");
        UserPrincipal userPrincipal = UserPrincipal.create(sampleUser);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("mocked.jwt.token");

        JwtAuthResponse response = authService.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getAccessToken());
        assertEquals("johndoe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());
        assertTrue(response.getRoles().contains("ROLE_CUSTOMER"));
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("Test 2: Authenticate user with invalid credentials throws BadCredentialsException")
    void testAuthenticateUser_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("johndoe", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(loginRequest));
        verify(tokenProvider, never()).generateToken(any());
    }

    @Test
    @DisplayName("Test 3: Register user fails when username already taken")
    void testRegisterUser_DuplicateUsername() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("johndoe");
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("secret123");
        signupRequest.setFullName("John Doe");

        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> authService.registerUser(signupRequest));
        assertEquals("Username is already taken!", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}
