package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.ApplicationService;
import com.adheesha.jobskill.jobskill_backend.Service.CVMatchService;
import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.CVMatchRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final CVMatchService cvMatchService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, CVMatchService cvMatchService) {
        this.applicationService = applicationService;
        this.cvMatchService = cvMatchService;
    }


    @PostMapping("/apply")
    public ResponseEntity<ApplicationDto> applyToJob(@RequestBody ApplicationDto applicationDto) {
        ApplicationDto saved = applicationService.applyToJob(applicationDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable Integer applicationId) {
        ApplicationDto dto = applicationService.getApplicationById(applicationId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<List<ApplicationDto>> getApplicationBySeekerId(@PathVariable Integer seekerId) {
        List<ApplicationDto> list = applicationService.getApplicationsBySeekerId(seekerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByJobId(@PathVariable Integer jobId) {
        List<ApplicationDto> list = applicationService.getApplicationsByJobId(jobId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update-status/{applicationId}")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable Integer applicationId,
            @RequestParam String status
    ) {
        ApplicationDto updated = applicationService.updateApplicationStatus(applicationId, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/withdraw/{applicationId}")
    public ResponseEntity<String> withdrawApplication(@PathVariable Integer applicationId) {
        boolean success = applicationService.withdrawApplication(applicationId);
        return success
                ? ResponseEntity.ok("Application withdrawn successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
    }

    @PostMapping("/match")
    public ResponseEntity<Double> getCVMatchScore(@RequestBody CVMatchRequestDto request) {
        double score = cvMatchService.getMatchScore(request.getResumeUrl(), request.getJobId());
        return ResponseEntity.ok(score);
    }
}
