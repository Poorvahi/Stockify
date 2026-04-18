package org.example.stockifyims.controller.auth;

import org.example.stockifyims.config.jwt.JwtUtil;
import org.example.stockifyims.dto.auth.AuthDTO;
import org.example.stockifyims.entity.PasswordResetOtpVo;
import org.example.stockifyims.entity.UserVo;
import org.example.stockifyims.repository.auth.PasswordResetOtpRepository;
import org.example.stockifyims.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired(required = false)
    private JavaMailSender mailSender;
    @Autowired
    private PasswordResetOtpRepository passwordResetOtpRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            UserVo user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(request.getUsername(), user.getRole());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTO request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Username is required");
            return ResponseEntity.badRequest().body(error);
        }
        if (request.getEmail() == null || !EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Valid email is required");
            return ResponseEntity.badRequest().body(error);
        }
        if (request.getPassword() == null || !PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Password must be at least 8 characters and include letters and numbers");
            return ResponseEntity.badRequest().body(error);
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Username already exists");
            return ResponseEntity.badRequest().body(error);
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email already exists");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            UserVo newUser = new UserVo();
            newUser.setUsername(request.getUsername());
            newUser.setFullName(request.getFullName());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole(request.getRole() != null && request.getRole().equalsIgnoreCase("ROLE_ADMIN")
                    ? "ROLE_ADMIN" : "ROLE_USER");
            userRepository.save(newUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        // New OTP flow entrypoint; does not expose OTP in response.
        String email = request.get("email");
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Valid email is required"));
        }
        Optional<UserVo> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "If email exists, OTP has been sent"));
        }

        String otp = String.valueOf(100000 + SECURE_RANDOM.nextInt(900000));
        PasswordResetOtpVo otpVo = new PasswordResetOtpVo();
        otpVo.setEmail(email);
        otpVo.setOtpHash(passwordEncoder.encode(otp));
        otpVo.setCreatedAt(LocalDateTime.now());
        otpVo.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpVo.setVerified(false);
        passwordResetOtpRepository.save(otpVo);

        if (mailSender != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Stockify Password Reset OTP");
            message.setText("Your OTP is " + otp + ". It will expire in 10 minutes.");
            mailSender.send(message);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "If email exists, OTP has been sent");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody AuthDTO request) {
        if (request.getEmail() == null || request.getOtp() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and OTP are required"));
        }
        Optional<PasswordResetOtpVo> otpOptional = passwordResetOtpRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail());
        if (otpOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP not found"));
        }

        PasswordResetOtpVo otpVo = otpOptional.get();
        if (otpVo.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP expired"));
        }
        if (!passwordEncoder.matches(request.getOtp(), otpVo.getOtpHash())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP"));
        }
        otpVo.setVerified(true);
        passwordResetOtpRepository.save(otpVo);
        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody AuthDTO request) {
        if (request.getEmail() == null || request.getNewPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and new password are required"));
        }
        if (!PASSWORD_PATTERN.matcher(request.getNewPassword()).matches()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password must be at least 8 characters and include letters and numbers"));
        }
        Optional<PasswordResetOtpVo> otpOptional = passwordResetOtpRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail());
        if (otpOptional.isEmpty() || !otpOptional.get().isVerified()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP verification required"));
        }
        if (otpOptional.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP expired"));
        }

        UserVo user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    @GetMapping("/test")
    public String test() {
        return "Test";
    }
}
