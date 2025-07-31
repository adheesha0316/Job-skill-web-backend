package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.ApplicationService;
import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerApplicationViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Override
    public ApplicationDto applyToJob(ApplicationDto applicationDto) {
        return null;
    }

    @Override
    public ApplicationDto getApplicationById(Integer applicationId) {
        return null;
    }

    @Override
    public List<ApplicationDto> getApplicationsBySeekerId(Integer seekerId) {
        return List.of();
    }

    @Override
    public List<ApplicationDto> getApplicationsByJobId(Integer jobId) {
        return List.of();
    }

    @Override
    public ApplicationDto updateApplicationStatus(Integer applicationId, String status) {
        return null;
    }

    @Override
    public List<EmployerApplicationViewDto> getApplicationsForEmployer(Integer employerId) {
        return List.of();
    }

    @Override
    public boolean withdrawApplication(Integer applicationId) {
        return false;
    }
}
