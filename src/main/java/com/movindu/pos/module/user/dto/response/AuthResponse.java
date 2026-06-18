package com.movindu.pos.module.user.dto.response;

import com.movindu.pos.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String username;
    private String fullName;
    private UserRole role;
    private Long userId;
}