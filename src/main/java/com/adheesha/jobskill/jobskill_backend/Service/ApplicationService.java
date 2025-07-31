package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerApplicationViewDto;

import java.util.List;

public interface ApplicationService {

    ApplicationDto applyToJob(ApplicationDto applicationDto);

    ApplicationDto getApplicationById(Integer applicationId);

    List<ApplicationDto> getApplicationsBySeekerId(Integer seekerId);

    List<ApplicationDto> getApplicationsByJobId(Integer jobId);

    ApplicationDto updateApplicationStatus(Integer applicationId, String status);

    List<EmployerApplicationViewDto> getApplicationsForEmployer(Integer employerId);

    boolean withdrawApplication(Integer applicationId);
}
