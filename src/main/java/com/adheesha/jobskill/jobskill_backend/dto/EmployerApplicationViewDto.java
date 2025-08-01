package com.adheesha.jobskill.jobskill_backend.dto;

import com.adheesha.jobskill.jobskill_backend.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerApplicationViewDto {

    private Integer applicationId;
    private String resumeUrl;
    private ApplicationStatus status;
    private LocalDateTime createdAt;

    private JobDto job;
    private JobSeekerDto seeker;
}
