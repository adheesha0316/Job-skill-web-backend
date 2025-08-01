package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.JobSeekerService;
import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import com.adheesha.jobskill.jobskill_backend.dto.JobSeekerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/seeker")
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;

    public JobSeekerController(JobSeekerService jobSeekerService) {
        this.jobSeekerService = jobSeekerService;
    }

    // Register with CV and Profile Image
    @PostMapping("/register")
    public ResponseEntity<JobSeekerDto> registerJobSeeker(
            @RequestParam String contactNumber,
            @RequestParam String experience,
            @RequestParam Integer userId,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            @RequestParam("profileImageFile") MultipartFile profileImageFile
    ) {
        JobSeekerDto dto = jobSeekerService.registerJobSeekerWithFiles(contactNumber, experience, userId, resumeFile, profileImageFile);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // Update JobSeeker with new CV/Profile Image
    @PutMapping("/update/{seekerId}")
    public ResponseEntity<JobSeekerDto> updateJobSeeker(
            @PathVariable Integer seekerId,
            @RequestParam String contactNumber,
            @RequestParam String experience,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile
    ) {
        JobSeekerDto dto = jobSeekerService.updateJobSeekerWithFiles(seekerId, contactNumber, experience, resumeFile, profileImageFile);
        return ResponseEntity.ok(dto);
    }

    // Get JobSeeker by ID
    @GetMapping("/get/{seekerId}")
    public ResponseEntity<JobSeekerDto> getById(@PathVariable Integer seekerId) {
        JobSeekerDto dto = jobSeekerService.getJobSeekerWithFilesById(seekerId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }


    // Get JobSeeker by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<JobSeekerDto> getByUserId(@PathVariable Integer userId) {
        JobSeekerDto dto = jobSeekerService.getJobSeekerWithFilesByUserId(userId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // Get All JobSeekers
    @GetMapping("/getAll")
    public ResponseEntity<List<JobSeekerDto>> getAll() {
        return ResponseEntity.ok(jobSeekerService.getAllJobSeekersWithFiles());
    }

    // Delete JobSeeker and files
    @DeleteMapping("/delete/{seekerId}")
    public ResponseEntity<String> delete(@PathVariable Integer seekerId) {
        boolean deleted = jobSeekerService.deleteJobSeekerWithFiles(seekerId);
        return deleted ?
                ResponseEntity.ok("Deleted successfully.") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobSeeker not found.");
    }

    // Apply to job with CV upload
    @PostMapping("/apply/{seekerId}/{jobId}")
    public ResponseEntity<ApplicationDto> applyToJobWithCv(
            @PathVariable Integer seekerId,
            @PathVariable Integer jobId,
            @RequestParam("resumeFile") MultipartFile resumeFile) {

        ApplicationDto dto = jobSeekerService.applyToJobWithCv(seekerId, jobId, resumeFile);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    // Enroll to Course (Path Variable version to match frontend call)
    @PostMapping("/enroll/{seekerId}/{courseId}")
    public ResponseEntity<EnrollmentDto> enrollInCoursePath(
            @PathVariable Integer seekerId,
            @PathVariable Integer courseId) {

        EnrollmentDto enrolled = jobSeekerService.enrollInCourse(seekerId, courseId);
        return ResponseEntity.ok(enrolled);
    }


    // View all courses
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> viewAllCourses() {
        return ResponseEntity.ok(jobSeekerService.viewAllCourses());
    }

    // Get enrolled course IDs for a given seeker
    @GetMapping("/enrolled-course-ids/{seekerId}")
    public ResponseEntity<List<Integer>> getEnrolledCourseIds(@PathVariable Integer seekerId) {
        List<Integer> enrolledIds = jobSeekerService.getEnrolledCourseIdsBySeekerId(seekerId);
        return ResponseEntity.ok(enrolledIds);
    }

    // getAll Application
    @GetMapping("/applications/getAll")
    public ResponseEntity<List<ApplicationDto>> getAllApplications() {
        List<ApplicationDto> applications = jobSeekerService.getAllApplications(); // or from applicationService
        return ResponseEntity.ok(applications);
    }

}
