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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public JwtAuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtAuthResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                userPrincipal.getFullName(),
                roles
        );
    }

    @Override
    @Transactional
    public String registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getFullName(),
                signupRequest.getPhoneNumber()
        );

        RoleName roleName = RoleName.ROLE_CUSTOMER;
        if (signupRequest.getRole() != null && !signupRequest.getRole().isBlank()) {
            try {
                roleName = RoleName.valueOf(signupRequest.getRole());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role specified: " + signupRequest.getRole());
            }
        }

        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new BadRequestException("Role not configured: " + roleName));

        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);

        return "User registered successfully with role " + roleName.name();
    }
}
