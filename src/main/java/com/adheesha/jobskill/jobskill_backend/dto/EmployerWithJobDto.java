package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerWithJobDto {
    private Integer employerId;
    private String companyName;
    private String contactNumber;
    private String address;
    private Integer userId;
    private Integer jobId;
    private List<JobDto> jobs;
}
