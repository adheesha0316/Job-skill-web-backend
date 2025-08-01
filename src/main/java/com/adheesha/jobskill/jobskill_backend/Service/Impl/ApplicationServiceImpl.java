package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.ApplicationService;
import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerApplicationViewDto;
import com.adheesha.jobskill.jobskill_backend.dto.JobDto;
import com.adheesha.jobskill.jobskill_backend.dto.JobSeekerDto;
import com.adheesha.jobskill.jobskill_backend.entity.Application;
import com.adheesha.jobskill.jobskill_backend.entity.Job;
import com.adheesha.jobskill.jobskill_backend.entity.JobSeeker;
import com.adheesha.jobskill.jobskill_backend.enums.ApplicationStatus;
import com.adheesha.jobskill.jobskill_backend.repo.ApplicationRepo;
import com.adheesha.jobskill.jobskill_backend.repo.JobRepo;
import com.adheesha.jobskill.jobskill_backend.repo.JobSeekerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final JobSeekerRepo jobSeekerRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepo applicationRepo, JobRepo jobRepo, JobSeekerRepo jobSeekerRepo, ModelMapper modelMapper) {
        this.applicationRepo = applicationRepo;
        this.jobRepo = jobRepo;
        this.jobSeekerRepo = jobSeekerRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApplicationDto applyToJob(ApplicationDto applicationDto) {
        Optional<Job> jobOpt = jobRepo.findById(applicationDto.getJobId());
        Optional<JobSeeker> seekerOpt = jobSeekerRepo.findById(applicationDto.getSeekerId());

        if (jobOpt.isEmpty() || seekerOpt.isEmpty()) {
            throw new RuntimeException("Job or JobSeeker not found.");
        }

        Application application = new Application();
        application.setJob(jobOpt.get());
        application.setSeeker(seekerOpt.get());
        application.setResume(applicationDto.getResume());
        application.setStatus(ApplicationStatus.PENDING); // default status

        Application saved = applicationRepo.save(application);
        return modelMapper.map(saved, ApplicationDto.class);
    }

    @Override
    public ApplicationDto getApplicationById(Integer applicationId) {
        return applicationRepo.findById(applicationId)
                .map(app -> modelMapper.map(app, ApplicationDto.class))
                .orElse(null);
    }

    @Override
    public List<ApplicationDto> getApplicationsBySeekerId(Integer seekerId) {
        return applicationRepo.findBySeeker_SeekerId(seekerId).stream()
                .map(app -> modelMapper.map(app, ApplicationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDto> getApplicationsByJobId(Integer jobId) {
        return applicationRepo.findByJob_JobId(jobId).stream()
                .map(app -> modelMapper.map(app, ApplicationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDto updateApplicationStatus(Integer applicationId, String status) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));

        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status.toUpperCase());
            application.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid application status: " + status + ". Allowed values: " +
                    Arrays.toString(ApplicationStatus.values()));
        }

        Application updated = applicationRepo.save(application);
        return modelMapper.map(updated, ApplicationDto.class);
    }

    @Override
    public List<EmployerApplicationViewDto> getApplicationsForEmployer(Integer employerId) {
        List<Application> applications = applicationRepo.findByJob_Employer_EmployerId(employerId);

        // Convert each Application entity to EmployerApplicationViewDto
        return applications.stream().map(app -> {
            Job job = app.getJob();
            JobSeeker seeker = app.getSeeker();

            // Map Job → JobDto
            JobDto jobDto = new JobDto(
                    job.getJobId(),
                    job.getTitle(),
                    job.getDescription(),
                    job.getLocation(),
                    job.getJobType(),
                    job.getSalary(),
                    job.getLogoPath(),
                    job.getEmployer().getEmployerId()
            );

            // Map Seeker → JobSeekerDto
            JobSeekerDto seekerDto = new JobSeekerDto(
                    seeker.getSeekerId(),
                    seeker.getContactNumber(),
                    seeker.getExperience(),
                    seeker.getResumePath(),
                    seeker.getProfileImage(),
                    seeker.getUser() != null ? seeker.getUser().getUserId() : null,     // userId
                    seeker.getUser() != null ? seeker.getUser().getEmail() : null,      // email
                    seeker.getUser() != null ? seeker.getUser().getUserName() : null    // userName
            );





            // Create and return the final view DTO
            return new EmployerApplicationViewDto(
                    app.getApplicationId(),
                    app.getResume(),
                    app.getStatus(),
                    app.getCreatedAt(),
                    jobDto,
                    seekerDto
            );
        }).collect(Collectors.toList());
    }

    @Override
    public boolean withdrawApplication(Integer applicationId) {
        if (applicationRepo.existsById(applicationId)) {
            applicationRepo.deleteById(applicationId);
            return true;
        }
        return false;
    }
}
