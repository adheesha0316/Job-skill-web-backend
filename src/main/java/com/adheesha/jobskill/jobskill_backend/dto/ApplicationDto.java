package com.adheesha.jobskill.jobskill_backend.dto;

import com.adheesha.jobskill.jobskill_backend.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {
    private Integer applicationId;
    private LocalDateTime createdAt;
    private Integer jobId;
    private String jobTitle; // add new
    private Integer seekerId;
    private String seekerName;  // add new
    private String resume;
    @JsonProperty("resumeUrl")
    private String resumeUrl; // renamed from resumeUrl
    private ApplicationStatus status;

}
