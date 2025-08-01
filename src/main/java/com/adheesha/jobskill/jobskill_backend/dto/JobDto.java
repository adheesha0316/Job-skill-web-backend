package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private Integer jobId;
    private String title;
    private String description;
    private String location;
    private String jobType;
    private Double salary;
    private String logoPath;
    private Integer employerId;
}
