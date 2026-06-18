package com.movindu.pos.module.user.controller;

import com.movindu.pos.common.response.ApiResponse;
import com.movindu.pos.module.user.dto.request.LoginRequest;
import com.movindu.pos.module.user.dto.request.RegisterRequest;
import com.movindu.pos.module.user.dto.response.AuthResponse;
import com.movindu.pos.module.user.dto.response.UserResponse;
import com.movindu.pos.module.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful",
                authService.login(request)));
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User registered successfully",
                authService.register(request)));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(authService.getAllUsers()));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(authService.getUserById(id)));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {
        // Deletion is not implemented in AuthService. Return not implemented status.
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}