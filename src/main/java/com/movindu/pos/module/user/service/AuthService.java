package com.movindu.pos.module.user.service;

import com.movindu.pos.module.user.dto.request.LoginRequest;
import com.movindu.pos.module.user.dto.request.RegisterRequest;
import com.movindu.pos.module.user.dto.response.AuthResponse;
import com.movindu.pos.module.user.dto.response.UserResponse;

import java.util.List;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void deleteUser(Long id);
}