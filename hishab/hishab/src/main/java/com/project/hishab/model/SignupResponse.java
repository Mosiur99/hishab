package com.project.hishab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    private String message;
    private String email;
    private String firstName;
    private String lastName;
} 