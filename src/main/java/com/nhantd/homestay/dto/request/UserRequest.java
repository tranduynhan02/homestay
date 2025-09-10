package com.nhantd.homestay.dto.request;

import com.nhantd.homestay.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRequest {
    private String username;
    private String password;
    private String email;
    private Role role;
}
