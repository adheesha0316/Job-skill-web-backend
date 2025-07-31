package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.EnrollmentService;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<EnrollmentDto> createEnrollment(@RequestBody EnrollmentDto enrollmentDto) {
        EnrollmentDto created = enrollmentService.createEnrollment(enrollmentDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{enrollmentId}")
    public ResponseEntity<EnrollmentDto> updateEnrollment(@PathVariable Integer enrollmentId,
                                                          @RequestBody EnrollmentDto enrollmentDto) {
        enrollmentDto.setEnrollmentId(enrollmentId);
        EnrollmentDto updated = enrollmentService.updateEnrollment(enrollmentDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }

    @GetMapping("/get/{enrollmentId}")
    public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable Integer enrollmentId) {
        EnrollmentDto dto = enrollmentService.getEnrollmentById(enrollmentId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentBySeekerId(@PathVariable Integer seekerId) {
        List<EnrollmentDto> list = enrollmentService.getEnrollmentsBySeekerId(seekerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentByCourseId(@PathVariable Integer courseId) {
        List<EnrollmentDto> list = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{enrollmentId}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable Integer enrollmentId) {
        boolean deleted = enrollmentService.deleteEnrollment(enrollmentId);
        if (deleted) {
            return new ResponseEntity<>("Deleted enrollment successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enrollment not found");
        }
    }

    @GetMapping("/seeker/{seekerId}/course/{courseId}")
    public ResponseEntity<EnrollmentDto> getEnrollmentBySeekerIdAndCourseId(@PathVariable Integer seekerId,
                                                                            @PathVariable Integer courseId) {
        EnrollmentDto dto = enrollmentService.getEnrollmentBySeekerIdAndCourseId(seekerId, courseId);
        return  dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
