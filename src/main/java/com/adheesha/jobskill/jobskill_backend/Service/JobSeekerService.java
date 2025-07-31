package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import com.adheesha.jobskill.jobskill_backend.dto.JobSeekerDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobSeekerService {
    // Register with form-data (CV + Profile Image)
    JobSeekerDto registerJobSeekerWithFiles(
            String contactNumber,
            String experience,
            Integer userId,
            MultipartFile resumeFile,
            MultipartFile profileImageFile
    );

    // Update with form-data (CV + Profile Image)
    JobSeekerDto updateJobSeekerWithFiles(
            Integer seekerId,
            String contactNumber,
            String experience,
            MultipartFile resumeFile,
            MultipartFile profileImageFile
    );

    // Get and Delete with CV awareness
    JobSeekerDto getJobSeekerWithFilesById(Integer seekerId);
    JobSeekerDto getJobSeekerWithFilesByUserId(Integer userId);
    boolean deleteJobSeekerWithFiles(Integer seekerId);


    //  Get All
    List<JobSeekerDto> getAllJobSeekersWithFiles();  // Optional rename: getAllJobSeekersWithCv()

    //  Apply to Job (form-data: upload CV while applying)
    ApplicationDto applyToJobWithCv(Integer seekerId, Integer jobId, MultipartFile resumeFile);

    //  Course enrollment (no CV needed)
    EnrollmentDto enrollInCourse(Integer seekerId, Integer courseId);
    List<CourseDto> viewAllCourses();

    //Get Enroll course
    List<Integer> getEnrolledCourseIdsBySeekerId(Integer seekerId);

    List<ApplicationDto> getAllApplications();
}
