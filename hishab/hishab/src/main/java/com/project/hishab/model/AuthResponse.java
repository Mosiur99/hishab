package com.project.hishab.model;

import com.project.hishab.Enum.UserRole;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private String refreshToken;
    private UserInfo user;

    public AuthResponse() {}

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(boolean success, String message, String token, String refreshToken, UserInfo user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public static class UserInfo {
        private Long id;
        private String email;
        private String name;
        private UserRole role;
        private boolean emailVerified;

        public UserInfo() {}

        public UserInfo(Long id, String email, String name, UserRole role, boolean emailVerified) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.role = role;
            this.emailVerified = emailVerified;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }

        public boolean isEmailVerified() {
            return emailVerified;
        }

        public void setEmailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
