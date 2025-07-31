package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.ApplicationService;
import com.adheesha.jobskill.jobskill_backend.Service.EmployerService;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerApplicationViewDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerWithJobDto;
import com.adheesha.jobskill.jobskill_backend.entity.Employer;
import com.adheesha.jobskill.jobskill_backend.utill.JWTTokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/employer")
public class EmployerController {

    private final EmployerService employerService;
    private final ApplicationService applicationService;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployerController(EmployerService employerService, ApplicationService applicationService, JWTTokenGenerator jwtTokenGenerator, ModelMapper modelMapper) {
        this.employerService = employerService;
        this.applicationService = applicationService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/addWithImage")
    public ResponseEntity<EmployerDto> addEmployerWithImage(
            @RequestParam("companyName") String companyName,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("address") String address,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("userId") Integer userId,
            @RequestHeader("Authorization") String authorization) {

        //if (jwtTokenGenerator.verifyToken(authorization)) {
        EmployerDto dto = employerService.addEmployerWithProfileImage(companyName, contactNumber, address, profileImage, userId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
        //}
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/updateWithImage/{employerId}")
    public ResponseEntity<EmployerDto> updateEmployerWithImage(
            @PathVariable Integer employerId,
            @RequestParam("companyName") String companyName,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("address") String address,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestHeader("Authorization") String authorization) {

        // Optionally verify token here if needed
        EmployerDto dto = employerService.updateEmployerWithProfileImage(
                employerId,
                companyName,
                contactNumber,
                address,
                profileImage // may be null
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/deleteWithImage/{employerId}")
    public ResponseEntity<EmployerDto> deleteEmployerWithImage(@PathVariable Integer employerId) {
        EmployerDto dto = employerService.deleteEmployerWithProfileImage(employerId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<EmployerDto> getEmployerByUserId(@PathVariable Integer userId) {
        EmployerDto dto = employerService.getEmployerByUserId(userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/getAll/allWithImage")
    public ResponseEntity<List<EmployerDto>> getAllEmployersWithImage() {
        List<EmployerDto> dtos = employerService.getAllEmployersWithImage();
        return ResponseEntity.ok(dtos);
    }


    @PostMapping("/saveWithJobs")
    public ResponseEntity<EmployerWithJobDto> saveEmployerWithJob(@RequestBody EmployerWithJobDto employerWithJobDto) {
        EmployerWithJobDto dto =  employerService.saveEmployerWithJob(employerWithJobDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/deleteWithJobs/{employerId}")
    public ResponseEntity<EmployerWithJobDto> deleteEmployerWithJob(@PathVariable("employerId") Integer employerId) {
        EmployerWithJobDto dto = employerService.deleteEmployerWithJob(employerId);
        if(dto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{employerId}/jobs")
    public ResponseEntity<List<EmployerWithJobDto>> getEmployerJobs(@PathVariable Integer employerId) {
        List<EmployerWithJobDto> jobs = employerService.getJobsByEmployerId(employerId);
        if (jobs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobs);
    }



    @GetMapping("/applications/{employerId}")
    public ResponseEntity<List<EmployerApplicationViewDto>> getApplicationsByEmployer(@PathVariable Integer employerId) {
        List<EmployerApplicationViewDto> apps = applicationService.getApplicationsForEmployer(employerId);
        return ResponseEntity.ok(apps);
    }


    //application
    @PostMapping("/application/accept")
    public ResponseEntity<String> acceptApplication(@RequestParam Integer applicationId) {
        employerService.acceptApplication(applicationId);
        return ResponseEntity.ok("Application Accepted");
    }

    @PostMapping("/application/reject")
    public ResponseEntity<String> rejectApplication(@RequestParam Integer applicationId) {
        employerService.rejectApplication(applicationId);
        return ResponseEntity.ok("Application Rejected");
    }



}
