package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.EmployerService;
import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerWithJobDto;
import com.adheesha.jobskill.jobskill_backend.dto.JobDto;
import com.adheesha.jobskill.jobskill_backend.entity.Application;
import com.adheesha.jobskill.jobskill_backend.entity.Employer;
import com.adheesha.jobskill.jobskill_backend.entity.Job;
import com.adheesha.jobskill.jobskill_backend.entity.User;
import com.adheesha.jobskill.jobskill_backend.enums.ApplicationStatus;
import com.adheesha.jobskill.jobskill_backend.repo.ApplicationRepo;
import com.adheesha.jobskill.jobskill_backend.repo.EmployerRepo;
import com.adheesha.jobskill.jobskill_backend.repo.JobRepo;
import com.adheesha.jobskill.jobskill_backend.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepo employerRepo;
    private final UserRepo userRepo;
    private final JobRepo jobRepo;
    private final ApplicationRepo applicationRepo;
    private final ModelMapper modelMapper;

    private final String PROFILE_DIR = System.getProperty("user.dir") + "/uploads/profiles/";

    @Autowired
    public EmployerServiceImpl(EmployerRepo employerRepo, UserRepo userRepo, JobRepo jobRepo, ApplicationRepo applicationRepo, ModelMapper modelMapper) {
        this.employerRepo = employerRepo;
        this.userRepo = userRepo;
        this.jobRepo = jobRepo;
        this.applicationRepo = applicationRepo;
        this.modelMapper = modelMapper;
    }

    private String saveImage(MultipartFile file, String directory, String defaultPath) {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("Invalid image file name");
            }

            if (!originalFilename.toLowerCase().matches(".*\\.(png|jpg|jpeg)$")) {
                throw new RuntimeException("Only PNG, JPG, or JPEG images are allowed");
            }

            String fileName = UUID.randomUUID() + "_" + originalFilename;
            try {
                File dir = new File(PROFILE_DIR);
                if (!dir.exists()) dir.mkdirs();

                file.transferTo(new File(directory + fileName));
                return "uploads/profiles/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }
        return defaultPath;
    }


    @Override
    public EmployerDto addEmployerWithProfileImage(String companyName, String contactNumber, String address, MultipartFile profileImage, Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (employerRepo.findByUser_UserId(userId).isPresent()) {
            throw new RuntimeException("An employer already exists for this user.");
        }

        Employer employer = new Employer();
        employer.setCompanyName(companyName);
        employer.setContactNumber(contactNumber);
        employer.setAddress(address);
        employer.setUser(user);
        employer.setProfileImage(saveImage(profileImage, PROFILE_DIR, "uploads/profiles/default.png"));

        Employer saved = employerRepo.save(employer);
        return modelMapper.map(saved, EmployerDto.class);
    }

    @Override
    public EmployerDto getEmployerByUserId(Integer userId) {
        return employerRepo.findByUser_UserId(userId)
                .map(employer -> {
                    EmployerDto dto = modelMapper.map(employer, EmployerDto.class);
                    dto.setUserId(employer.getUser().getUserId());  // ensure userId is set
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public EmployerDto updateEmployerWithProfileImage(Integer employerId, String companyName, String contactNumber, String address, MultipartFile profileImage) {
        Employer employer = employerRepo.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        // Update basic info
        employer.setCompanyName(companyName);
        employer.setContactNumber(contactNumber);
        employer.setAddress(address);

        // Update profile image only if a new one is provided
        if (profileImage != null && !profileImage.isEmpty()) {
            String newImagePath = saveImage(profileImage, PROFILE_DIR, employer.getProfileImage());
            employer.setProfileImage(newImagePath);
        }

        // Save updated employer and map to DTO
        Employer updatedEmployer = employerRepo.save(employer);
        return modelMapper.map(updatedEmployer, EmployerDto.class);
    }

    @Override
    public EmployerDto getEmployerWithProfileImage(Integer employerId) {
        return employerRepo.findById(employerId)
                .map(emp -> modelMapper.map(emp, EmployerDto.class))
                .orElse(null);
    }

    @Override
    public EmployerDto deleteEmployerWithProfileImage(Integer employerId) {
        Optional<Employer> byId = employerRepo.findById(employerId);
        if (byId.isPresent()) {
            Employer emp = byId.get();

            // Delete image file if exists
            if (emp.getProfileImage() != null && !emp.getProfileImage().equals("uploads/logos/default.png")) {
                File imageFile = new File(System.getProperty("user.dir") + "/" + emp.getProfileImage());
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }

            employerRepo.deleteById(employerId);
            return modelMapper.map(emp, EmployerDto.class);
        }
        return null;
    }

    @Override
    public List<EmployerDto> getAllEmployersWithImage() {
        return employerRepo.findAll().stream()
                .map(emp -> {
                    EmployerDto dto = modelMapper.map(emp, EmployerDto.class);

                    // Set profile image with full URL if available
                    if (emp.getProfileImage() != null) {
                        dto.setProfileImage("http://localhost:8080/" + emp.getProfileImage());
                    }

                    // Set user-related fields if user exists
                    if (emp.getUser() != null) {
                        dto.setUserId(emp.getUser().getUserId());
                        dto.setUserName(emp.getUser().getUserName());
                        dto.setEmail(emp.getUser().getEmail());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Employer> findByUserId(Integer userId) {
        return employerRepo.findByUser_UserId(userId);
    }

    @Override
    public EmployerWithJobDto saveEmployerWithJob(EmployerWithJobDto employerWithJobDto) {
        Optional<User> userOptional = userRepo.findById(employerWithJobDto.getEmployerId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found for employerId: " + employerWithJobDto.getEmployerId());
        }

        User user = userOptional.get();
        Employer employer = modelMapper.map(employerWithJobDto, Employer.class);
        employer.setUser(user);

        List<Job> jobs = employerWithJobDto.getJobs().stream()
                .map(dto -> {
                    Job job = modelMapper.map(dto, Job.class);
                    job.setEmployer(employer);
                    return job;
                })
                .collect(Collectors.toList());

        employer.setJobs(jobs);
        Employer saved = employerRepo.save(employer);

        List<JobDto> savedJobs = saved.getJobs().stream()
                .map(job -> {
                    JobDto dto = modelMapper.map(job, JobDto.class);
                    dto.setEmployerId(saved.getEmployerId());
                    return dto;
                })
                .collect(Collectors.toList());

        EmployerWithJobDto result = modelMapper.map(saved, EmployerWithJobDto.class);
        result.setJobs(savedJobs);
        return result;
    }

    @Override
    public EmployerWithJobDto deleteEmployerWithJob(Integer employerId) {
        Optional<Employer> optionalEmployer = employerRepo.findById(employerId);

        if (optionalEmployer.isEmpty()) {
            throw new RuntimeException("Employer not found with ID: " + employerId);
        }

        Employer employer = optionalEmployer.get();

        // Map before deleting so you can return the deleted data
        EmployerWithJobDto dto = modelMapper.map(employer, EmployerWithJobDto.class);
        dto.setUserId(employer.getUser().getUserId());

        List<JobDto> jobDtos = employer.getJobs().stream()
                .map(job -> {
                    JobDto jobDto = modelMapper.map(job, JobDto.class);
                    jobDto.setEmployerId(employer.getEmployerId());
                    return jobDto;
                }).collect(Collectors.toList());

        dto.setJobs(jobDtos);

        employerRepo.delete(employer); // delete after mapping

        return dto;
    }

    @Override
    public List<EmployerWithJobDto> getJobsByEmployerId(Integer employerId) {
        Optional<Employer> employerOptional = employerRepo.findById(employerId);

        if (employerOptional.isPresent()) {
            Employer employer = employerOptional.get();

            EmployerWithJobDto dto = modelMapper.map(employer, EmployerWithJobDto.class);

            List<JobDto> jobDtos = employer.getJobs().stream()
                    .map(job -> {
                        JobDto jobDto = modelMapper.map(job, JobDto.class);
                        jobDto.setEmployerId(employer.getEmployerId());
                        return jobDto;
                    }).collect(Collectors.toList());

            dto.setJobs(jobDtos);

            return List.of(dto); // return as a single-item list
        }

        return Collections.emptyList(); // or throw an exception if needed
    }

    @Override
    public List<ApplicationDto> getAllApplicationsForEmployer(Integer employerId) {
        List<Job> jobs = jobRepo.findByEmployerEmployerId(employerId);

        List<Application> allApplications = new ArrayList<>();
        for (Job job : jobs) {
            List<Application> applications = applicationRepo.findByJob_JobId(job.getJobId());
            allApplications.addAll(applications);
        }

        // Map each Application to ApplicationDto with nested fields and full resume URL
        return allApplications.stream().map(app -> {
            ApplicationDto dto = modelMapper.map(app, ApplicationDto.class);

            // Set nested jobTitle from job entity
            if (app.getJob() != null) {
                dto.setJobId(app.getJob().getJobId());
                dto.setJobTitle(app.getJob().getTitle());
            }

            // Set nested seekerName from seeker -> user entity
            if (app.getSeeker() != null && app.getSeeker().getUser() != null) {
                dto.setSeekerId(app.getSeeker().getSeekerId());
                dto.setSeekerName(app.getSeeker().getUser().getUserName());
            }

            // Set resume filename
            dto.setResume(app.getResume());

            // Set full resume URL
            if (app.getResume() != null && !app.getResume().isEmpty()) {
                dto.setResumeUrl("http://localhost:8080/cvs/" + app.getResume());

            }


            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void acceptApplication(Integer applicationId) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepo.save(application);
    }

    @Override
    public void rejectApplication(Integer applicationId) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepo.save(application);
    }
}
