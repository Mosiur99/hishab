package com.project.hishab.service;

import com.project.hishab.model.AuthResponse;
import com.project.hishab.model.GoogleAuthRequest;
import com.project.hishab.model.LoginRequest;
import com.project.hishab.model.SignupRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse signup(SignupRequest request);
    AuthResponse googleAuth(GoogleAuthRequest request);
    AuthResponse verifyEmail(String token);
    AuthResponse refreshToken(String refreshToken);
    AuthResponse forgotPassword(String email);
    AuthResponse resetPassword(String token, String newPassword);
    AuthResponse logout(String token);
}
