package org.example.stockifyims.dto.auth;

import lombok.Data;

@Data
public class AuthDTO {
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String password;
    private String otp;
    private String newPassword;
}
