package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.JobSeekerService;
import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import com.adheesha.jobskill.jobskill_backend.dto.JobSeekerDto;
import com.adheesha.jobskill.jobskill_backend.entity.*;
import com.adheesha.jobskill.jobskill_backend.enums.ApplicationStatus;
import com.adheesha.jobskill.jobskill_backend.repo.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerRepo jobSeekerRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final ApplicationRepo applicationRepo;
    private final EnrollmentRepo enrollmentRepo;
    private final JobRepo jobRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public JobSeekerServiceImpl(JobSeekerRepo jobSeekerRepo, UserRepo userRepo, CourseRepo courseRepo, ApplicationRepo applicationRepo, EnrollmentRepo enrollmentRepo, JobRepo jobRepo, ModelMapper modelMapper) {
        this.jobSeekerRepo = jobSeekerRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.applicationRepo = applicationRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.jobRepo = jobRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public JobSeekerDto registerJobSeekerWithFiles(String contactNumber, String experience, Integer userId, MultipartFile resumeFile, MultipartFile profileImageFile) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker seeker = new JobSeeker();
        seeker.setContactNumber(contactNumber);
        seeker.setExperience(experience != null ? experience.trim() : null);
        seeker.setUser(user);

        // Store files if present
        if (resumeFile != null && !resumeFile.isEmpty()) {
            String resumePath = storeFile(resumeFile, "uploads/cvs/");
            seeker.setResumePath(resumePath);
        }

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String profileImagePath = storeFile(profileImageFile, "uploads/profiles/");
            seeker.setProfileImage(profileImagePath);
        }

        JobSeeker saved = jobSeekerRepo.save(seeker);
        JobSeekerDto dto = modelMapper.map(saved, JobSeekerDto.class);
        dto.setUserId(saved.getUser().getUserId());
        return dto;
    }

    @Override
    public JobSeekerDto updateJobSeekerWithFiles(Integer seekerId, String contactNumber, String experience, MultipartFile resumeFile, MultipartFile profileImageFile) {
        JobSeeker seeker = jobSeekerRepo.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

        seeker.setContactNumber(contactNumber);
        seeker.setExperience(experience);

        // Update CV if new file provided
        if (resumeFile != null && !resumeFile.isEmpty()) {
            String resumePath = storeFile(resumeFile, "uploads/cvs/");
            seeker.setResumePath(resumePath);
        }

        // Update profile image if new file provided
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String profileImagePath = storeFile(profileImageFile, "uploads/profiles/");
            seeker.setProfileImage(profileImagePath);
        }

        JobSeeker saved = jobSeekerRepo.save(seeker);
        JobSeekerDto dto = modelMapper.map(saved, JobSeekerDto.class);
        dto.setUserId(saved.getUser().getUserId());
        return dto;
    }

    @Override
    public JobSeekerDto getJobSeekerWithFilesById(Integer seekerId) {
        return jobSeekerRepo.findById(seekerId)
                .map(seeker -> {
                    JobSeekerDto dto = modelMapper.map(seeker, JobSeekerDto.class);
                    dto.setUserId(seeker.getUser().getUserId());
                    return dto;
                }).orElse(null);
    }

    @Override
    public JobSeekerDto getJobSeekerWithFilesByUserId(Integer userId) {
        return jobSeekerRepo.findByUserUserId(userId)
                .map(seeker -> {
                    JobSeekerDto dto = modelMapper.map(seeker, JobSeekerDto.class);
                    dto.setUserId(seeker.getUser().getUserId());
                    return dto;
                }).orElse(null);
    }

    @Override
    public boolean deleteJobSeekerWithFiles(Integer seekerId) {
        if (jobSeekerRepo.existsById(seekerId)) {
            jobSeekerRepo.deleteById(seekerId);
            return true;
        }
        return false;
    }

    @Override
    public List<JobSeekerDto> getAllJobSeekersWithFiles() {
        return jobSeekerRepo.findAll()
                .stream()
                .map(seeker -> {
                    JobSeekerDto dto = new JobSeekerDto();
                    dto.setSeekerId(seeker.getSeekerId());
                    dto.setContactNumber(seeker.getContactNumber());
                    dto.setExperience(seeker.getExperience());
                    dto.setResumePath(seeker.getResumePath());

                    // Add full profile image URL if available
                    if (seeker.getProfileImage() != null) {
                        dto.setProfileImage("http://localhost:8080/" + seeker.getProfileImage());
                    }

                    if (seeker.getUser() != null) {
                        dto.setUserId(seeker.getUser().getUserId());
                        dto.setUserName(seeker.getUser().getUserName());
                        dto.setEmail(seeker.getUser().getEmail());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDto applyToJobWithCv(Integer seekerId, Integer jobId, MultipartFile resumeFile) {
        Optional<Job> jobOpt = jobRepo.findById(jobId);
        Optional<JobSeeker> seekerOpt = jobSeekerRepo.findById(seekerId);

        if (jobOpt.isEmpty() || seekerOpt.isEmpty()) {
            throw new RuntimeException("Job or JobSeeker not found");
        }

        Application application = new Application();
        application.setJob(jobOpt.get());
        application.setSeeker(seekerOpt.get());
        application.setStatus(ApplicationStatus.PENDING);

        if (resumeFile != null && !resumeFile.isEmpty()) {
            String filename = storeFile(resumeFile, "uploads/cvs/");
            application.setResume(filename);
        }

        Application saved = applicationRepo.save(application);
        return modelMapper.map(saved, ApplicationDto.class);
    }

    @Override
    public EnrollmentDto enrollInCourse(Integer seekerId, Integer courseId) {
        JobSeeker seeker = jobSeekerRepo.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Seeker not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepo.findBySeekerSeekerIdAndCourseCourseId(seekerId, courseId) != null) {
            throw new RuntimeException("Already enrolled");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setSeeker(seeker);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        Enrollment saved = enrollmentRepo.save(enrollment);
        return modelMapper.map(saved, EnrollmentDto.class);
    }

    @Override
    public List<CourseDto> viewAllCourses() {
        return courseRepo.findAll()
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getEnrolledCourseIdsBySeekerId(Integer seekerId) {
        return enrollmentRepo.findCourseIdsBySeekerId(seekerId);
    }

    @Override
    public List<ApplicationDto> getAllApplications() {
        List<Application> applicationEntities = applicationRepo.findAll();

        return applicationEntities.stream().map(app -> {
            ApplicationDto dto = new ApplicationDto();
            dto.setApplicationId(app.getApplicationId());
            dto.setCreatedAt(app.getCreatedAt());

            if (app.getJob() != null) {
                dto.setJobId(app.getJob().getJobId());
                dto.setJobTitle(app.getJob().getTitle());
            }


            if (app.getSeeker() != null) {
                dto.setSeekerId(app.getSeeker().getSeekerId());
                if (app.getSeeker().getUser() != null) {
                    dto.setSeekerName(app.getSeeker().getUser().getUserName()); // Requires getSeekerName in DTO
                }
            }

            dto.setResume(app.getResume());
            dto.setStatus(app.getStatus());

            return dto;
        }).collect(Collectors.toList());
    }


    // 🛠️ Helper to save files to disk
    private String storeFile(MultipartFile file, String directoryPath) {
        try {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filename = UUID.randomUUID() + extension;

            File directory = new File(directoryPath);
            if (!directory.exists()) directory.mkdirs();

            Path path = Paths.get(directoryPath + filename);
            Files.write(path, file.getBytes());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
