package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoReturn {
    private String email;
    private String status;
}
