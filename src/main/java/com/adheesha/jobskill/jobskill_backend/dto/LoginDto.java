package com.adheesha.jobskill.jobskill_backend.dto;

import com.adheesha.jobskill.jobskill_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String email;
    private String token;
    private Role role;
}
