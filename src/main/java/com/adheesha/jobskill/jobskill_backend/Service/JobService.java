package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.JobDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobService {
    //  Create a new job with logo (supports multipart/form-data)
    JobDto createJobWithLogo(String title, String description, String location,
                             String jobType, Double salary, Integer employerId,
                             MultipartFile logoFile);

    //  Update an existing job with optional new logo
    JobDto updateJobWithLogo(Integer jobId, String title, String description, String location,
                             String jobType, Double salary, MultipartFile logoFile);

    //  Get single job by ID
    JobDto getJob(Integer jobId);

    //  Delete job by ID
    boolean deleteJob(Integer jobId);

    // Get all jobs (logo path included)
    List<JobDto> getAllJobs();
}
