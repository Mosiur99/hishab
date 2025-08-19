package com.project.hishab.service;

import com.project.hishab.Enum.UserRole;
import com.project.hishab.entity.User;
import com.project.hishab.model.*;
import com.project.hishab.repository.UserRepository;
import com.project.hishab.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                return new AuthResponse(false, "Invalid email or password");
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse(false, "Invalid email or password");
            }

            if (!user.getEmailVerified()) {
                return new AuthResponse(false, "Please verify your email before logging in");
            }

            // Update last login
            user.setLastLogin(new Date());
            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(), user.getEmail(), user.getName(), user.getRole(), user.getEmailVerified()
            );

            return new AuthResponse(true, "Login successful", token, refreshToken, userInfo);
        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                return new AuthResponse(false, "Email already registered");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.USER);
            user.setEmailVerified(false);
            user.setVerificationToken(UUID.randomUUID().toString());
            user.setCreated(new Date());
            user.setUpdated(new Date());

            userRepository.save(user);

            // TODO: Send verification email
            // For now, we'll auto-verify the email
            user.setEmailVerified(true);
            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(), user.getEmail(), user.getName(), user.getRole(), user.getEmailVerified()
            );

            return new AuthResponse(true, "Registration successful", token, refreshToken, userInfo);
        } catch (Exception e) {
            return new AuthResponse(false, "Registration failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse googleAuth(GoogleAuthRequest request) {
        try {
            // TODO: Verify Google ID token
            // For now, we'll trust the request data

            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                if (user.getGoogleId() == null) {
                    // Link Google account to existing email
                    user.setGoogleId(request.getGoogleId());
                    user.setEmailVerified(true);
                    user.setUpdated(new Date());
                }
            } else {
                // Create new user
                user = new User();
                user.setEmail(request.getEmail());
                user.setName(request.getName());
                user.setGoogleId(request.getGoogleId());
                user.setRole(UserRole.USER);
                user.setEmailVerified(true);
                user.setCreated(new Date());
                user.setUpdated(new Date());
            }

            user.setLastLogin(new Date());
            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(), user.getEmail(), user.getName(), user.getRole(), user.getEmailVerified()
            );

            return new AuthResponse(true, "Google authentication successful", token, refreshToken, userInfo);
        } catch (Exception e) {
            return new AuthResponse(false, "Google authentication failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse verifyEmail(String token) {
        try {
            Optional<User> userOpt = userRepository.findByVerificationToken(token);
            if (userOpt.isEmpty()) {
                return new AuthResponse(false, "Invalid verification token");
            }

            User user = userOpt.get();
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            user.setUpdated(new Date());
            userRepository.save(user);

            return new AuthResponse(true, "Email verified successfully");
        } catch (Exception e) {
            return new AuthResponse(false, "Email verification failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                return new AuthResponse(false, "Invalid refresh token");
            }

            String email = jwtUtil.extractEmail(refreshToken);
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return new AuthResponse(false, "User not found");
            }

            User user = userOpt.get();
            String newToken = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(), user.getEmail(), user.getName(), user.getRole(), user.getEmailVerified()
            );

            return new AuthResponse(true, "Token refreshed successfully", newToken, newRefreshToken, userInfo);
        } catch (Exception e) {
            return new AuthResponse(false, "Token refresh failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse forgotPassword(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return new AuthResponse(false, "Email not found");
            }

            User user = userOpt.get();
            user.setResetToken(UUID.randomUUID().toString());
            user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // 1 hour
            user.setUpdated(new Date());
            userRepository.save(user);

            // TODO: Send password reset email
            return new AuthResponse(true, "Password reset instructions sent to your email");
        } catch (Exception e) {
            return new AuthResponse(false, "Password reset failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse resetPassword(String token, String newPassword) {
        try {
            Optional<User> userOpt = userRepository.findByResetToken(token);
            if (userOpt.isEmpty()) {
                return new AuthResponse(false, "Invalid reset token");
            }

            User user = userOpt.get();
            if (user.getResetTokenExpiry().before(new Date())) {
                return new AuthResponse(false, "Reset token has expired");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            user.setUpdated(new Date());
            userRepository.save(user);

            return new AuthResponse(true, "Password reset successfully");
        } catch (Exception e) {
            return new AuthResponse(false, "Password reset failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse logout(String token) {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return success
        return new AuthResponse(true, "Logged out successfully");
    }
}
