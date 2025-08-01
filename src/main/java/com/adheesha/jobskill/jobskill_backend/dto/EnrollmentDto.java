package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDto {
    private Integer enrollmentId;
    private LocalDate enrollmentDate;
    private Integer courseId;
    private String courseTitle;
    private Integer seekerId;
    private String seekerName; // add new
    private String email; // add new
    private String contactNumber; //add new
}
