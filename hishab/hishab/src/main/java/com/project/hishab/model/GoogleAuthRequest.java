package com.project.hishab.model;

public class GoogleAuthRequest {
    private String idToken;
    private String email;
    private String name;
    private String googleId;

    public GoogleAuthRequest() {}

    public GoogleAuthRequest(String idToken, String email, String name, String googleId) {
        this.idToken = idToken;
        this.email = email;
        this.name = name;
        this.googleId = googleId;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
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

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
