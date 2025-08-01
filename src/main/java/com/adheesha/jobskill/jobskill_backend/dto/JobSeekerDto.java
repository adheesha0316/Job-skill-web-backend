package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerDto {
    private Integer seekerId;
    private String contactNumber;
    private String experience;
    private String resumePath;
    private String profileImage;
    private Integer userId;
    private String email;  // Added by UserDto
    private String userName; // Added by UserDto
}
