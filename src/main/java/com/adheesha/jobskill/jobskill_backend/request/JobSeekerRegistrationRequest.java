package com.adheesha.jobskill.jobskill_backend.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JobSeekerRegistrationRequest {
    private String contactNumber;
    private String experience;
    private Integer userId;

    // MultipartFile allows file uploads via form-data (e.g. CV/PDF)
    private MultipartFile resume;
    private MultipartFile profileImage;
}
