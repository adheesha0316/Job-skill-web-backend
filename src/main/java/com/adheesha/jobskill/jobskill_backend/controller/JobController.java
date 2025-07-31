package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.JobService;
import com.adheesha.jobskill.jobskill_backend.dto.JobDto;
import com.adheesha.jobskill.jobskill_backend.utill.JWTTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/job")
public class JobController {
    private final JobService jobService;
    private final JWTTokenGenerator jwtTokenGenerator;

    @Autowired
    public JobController(JobService jobService, JWTTokenGenerator jwtTokenGenerator) {
        this.jobService = jobService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    //  Create Job with logo
    @PostMapping("/create")
    public ResponseEntity<?> createJobWithLogo(@RequestParam String title,
                                               @RequestParam String description,
                                               @RequestParam String location,
                                               @RequestParam String jobType,
                                               @RequestParam Double salary,
                                               @RequestParam Integer employerId,
                                               @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
                                               @RequestHeader("Authorization") String authHeader) {
        //if (!jwtTokenGenerator.verifyToken(authHeader)) {
        JobDto jobDto = jobService.createJobWithLogo(title, description, location, jobType, salary, employerId, logoFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(jobDto);
        //}

        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");



    }

    //  Update Job with optional logo
    @PutMapping("/update/{jobId}")
    public ResponseEntity<?> updateJobWithLogo(@PathVariable Integer jobId,
                                               @RequestParam String title,
                                               @RequestParam String description,
                                               @RequestParam String location,
                                               @RequestParam String jobType,
                                               @RequestParam Double salary,
                                               @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
                                               @RequestHeader("Authorization") String authHeader) {
        //if (!jwtTokenGenerator.verifyToken(authHeader)) {
        JobDto jobDto = jobService.updateJobWithLogo(jobId, title, description, location, jobType, salary, logoFile);
        return ResponseEntity.ok(jobDto);

        //}
        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");


    }

    //  Get Job by ID
    @GetMapping("/get/{jobId}")
    public ResponseEntity<JobDto> getJob(@PathVariable Integer jobId) {
        JobDto dto = jobService.getJob(jobId);
        return ResponseEntity.ok(dto);
    }

    //  Delete Job
    @DeleteMapping("/delete/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer jobId,
                                       @RequestHeader("Authorization") String authHeader) {
        //if (!jwtTokenGenerator.verifyToken(authHeader)) {
        boolean deleted = jobService.deleteJob(jobId);
        return deleted ? ResponseEntity.ok("Job deleted ucscessfully.") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");

        //}

        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");


    }

    //  Get All Jobs
    @GetMapping("/getAll")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}
