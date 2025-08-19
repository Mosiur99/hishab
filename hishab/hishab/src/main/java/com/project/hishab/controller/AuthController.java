package com.project.hishab.controller;

import com.project.hishab.model.*;
import com.project.hishab.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@RequestBody GoogleAuthRequest request) {
        AuthResponse response = authService.googleAuth(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestParam String token) {
        AuthResponse response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        AuthResponse response = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        AuthResponse response = authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        AuthResponse response = authService.logout(jwtToken);
        return ResponseEntity.ok(response);
    }

    // Helper classes for request bodies
    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class ForgotPasswordRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
