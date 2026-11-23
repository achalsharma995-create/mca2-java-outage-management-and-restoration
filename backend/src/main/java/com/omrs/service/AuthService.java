package com.omrs.service;

import com.omrs.dto.auth.JwtAuthResponse;
import com.omrs.dto.auth.LoginRequest;
import com.omrs.dto.auth.SignupRequest;

public interface AuthService {
    JwtAuthResponse authenticateUser(LoginRequest loginRequest);
    String registerUser(SignupRequest signupRequest);
}
