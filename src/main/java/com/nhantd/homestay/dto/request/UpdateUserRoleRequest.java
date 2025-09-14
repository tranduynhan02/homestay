package com.nhantd.homestay.dto.request;

import com.nhantd.homestay.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRoleRequest {
    private String username;
    private Role role;
}
