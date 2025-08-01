package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {

    private Integer trainerId;
    private String qualification;
    private String contactNumber;
    private String experience;
    private String courseCategory;
    private String profileImage;
    private Integer userId;
    private String userName;  // added by userDto
    private String email;  // added by userDto
}
