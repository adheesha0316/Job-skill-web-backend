package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDto {
    private Integer employerId;
    private String companyName;
    private String contactNumber;
    private String address;
    private String profileImage;
    private Integer userId;
    private String userName; // Added by UserDto
    private String email;  // Added by UserDto
    private List<JobDto> jobs;
}
