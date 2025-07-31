package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.EnrollmentService;
import com.adheesha.jobskill.jobskill_backend.Service.TrainerService;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import com.adheesha.jobskill.jobskill_backend.dto.TrainerDto;
import com.adheesha.jobskill.jobskill_backend.utill.JWTTokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/trainer")
public class TrainerController {
    private final TrainerService trainerService;
    private final EnrollmentService enrollmentService;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final ModelMapper modelMapper;

    public TrainerController(TrainerService trainerService, EnrollmentService enrollmentService, JWTTokenGenerator jwtTokenGenerator, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.enrollmentService = enrollmentService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add/course")
    public ResponseEntity<CourseDto> addCourse(@RequestBody CourseDto courseDto,
                                               @RequestHeader(name = "Authorization") String authorization) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        CourseDto savedCourse = trainerService.addCourse(courseDto);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);

        //}
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update/course/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Integer courseId,
                                                  @RequestBody CourseDto courseDto,
                                                  @RequestHeader(name = "Authorization") String authorization) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        courseDto.setCourseId(courseId);
        CourseDto updatedCourse = trainerService.updateCourse(courseDto);
        if (updatedCourse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

        //}
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @DeleteMapping("/delete/course/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer courseId,
                                               @RequestHeader(name = "Authorization") String authorization) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        boolean deleted = trainerService.deleteCourse(courseId);
        return deleted ? new ResponseEntity<>("Course deleted successfully",HttpStatus.NO_CONTENT)
                : new ResponseEntity<>("Course not found",HttpStatus.NOT_FOUND);

        //}
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/course/all/{trainerId}")
    public ResponseEntity<List<CourseDto>> listCoursesByTrainer(@PathVariable Integer trainerId,
                                                                @RequestHeader(name = "Authorization") String authorization) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        List<CourseDto> courses = trainerService.listCoursesByTrainer(trainerId);
        return new ResponseEntity<>(courses, HttpStatus.OK);

        //}
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/enrollments/by-trainer/{trainerId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByTrainer(@PathVariable Integer trainerId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByTrainer(trainerId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/get/course/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Integer courseId,
                                                   @RequestHeader(name = "Authorization") String authorization) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        CourseDto course = trainerService.getCourseById(courseId);
        return (course == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(course, HttpStatus.OK);
        //}
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }

    // -------------------- Trainer Profile --------------------

    // Create Trainer Profile with Profile Image
    @PostMapping("/profile/create")
    public ResponseEntity<TrainerDto> createTrainerWithProfile(
            @RequestParam("qualification") String qualification,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("experience") String experience,
            @RequestParam("courseCategory") String courseCategory,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("userId") Integer userId,
            @RequestHeader(name = "Authorization") String authorization
    ) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        TrainerDto created = trainerService.createTrainerWithProfile(
                qualification, contactNumber, experience, courseCategory, profileImage, userId);

        return new ResponseEntity<>(created, HttpStatus.CREATED);

        //}

        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }

    @PutMapping("/profile/update/{trainerId}")
    public ResponseEntity<TrainerDto> updateTrainerWithProfile(
            @PathVariable Integer trainerId,
            @RequestParam("qualification") String qualification,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("experience") String experience,
            @RequestParam("courseCategory") String courseCategory,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestHeader(name = "Authorization") String authorization
    ) {
        //if (!jwtTokenGenerator.verifyToken(authorization)) {
        TrainerDto updatedTrainer = trainerService.updateTrainerWithProfile(
                trainerId, qualification, contactNumber, experience, courseCategory, profileImage
        );
        return new ResponseEntity<>(updatedTrainer, HttpStatus.OK);

        // }

        // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    //getAll trainer
    @GetMapping("/getAll")
    public ResponseEntity<List<TrainerDto>> getAllTrainers() {
        List<TrainerDto> trainers = trainerService.getAllTrainers();
        return ResponseEntity.ok(trainers);
    }



    @GetMapping("/profile/get/user/{userId}")
    public ResponseEntity<TrainerDto> getTrainerByUserId(@PathVariable Integer userId) {
        TrainerDto dto = trainerService.getTrainerByUserId(userId);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/profile/get/trainer/{trainerId}")
    public ResponseEntity<TrainerDto> getTrainerWithProfile(
            @PathVariable Integer trainerId,
            @RequestHeader(name = "Authorization", required = false) String authorization
    ) {
        System.out.println("Authorization header: " + authorization);

        // Remove "Bearer " prefix from the header
//        String token = authorization.replace("Bearer ", "").trim();
//        System.out.println("Extracted token: " + token);
//
//        String email = jwtTokenGenerator.extractEmail(token);
//        System.out.println("Email from token: " + email);
//
//        if (!jwtTokenGenerator.verifyToken(token)) {
//            System.out.println("Token verification failed");
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

        try {
            TrainerDto trainer = trainerService.getTrainerWithProfile(trainerId);
            return new ResponseEntity<>(trainer, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println("Trainer not found for ID: " + trainerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //delete api
    @DeleteMapping("/delete/{trainerId}")
    public ResponseEntity<String> deleteTrainer(@PathVariable Integer trainerId) {
        trainerService.deleteTrainerById(trainerId);
        return ResponseEntity.ok("Trainer deleted successfully");
    }
}
