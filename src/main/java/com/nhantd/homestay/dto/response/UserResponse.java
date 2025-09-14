package com.nhantd.homestay.dto.response;

import com.nhantd.homestay.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;

    public UserResponse(String username, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
