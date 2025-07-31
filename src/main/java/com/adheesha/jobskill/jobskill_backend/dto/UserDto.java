package com.adheesha.jobskill.jobskill_backend.dto;

import com.adheesha.jobskill.jobskill_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer userId;
    private String userName;
    private String email;
    private String password;
    private Role role;
}
