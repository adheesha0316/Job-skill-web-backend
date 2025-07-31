package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.CourseService;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public ResponseEntity<CourseDto> addCourse(@RequestBody CourseDto courseDto) {
        CourseDto created = courseService.addCourse(courseDto);
        return ResponseEntity.ok(created);
    }


    @PutMapping("/update/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Integer courseId, @RequestBody CourseDto courseDto) {
        courseDto.setCourseId(courseId);
        CourseDto updated = courseService.updateCourse(courseDto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer courseId) {
        boolean deleted = courseService.deleteCourse(courseId);
        if (deleted) {
            return ResponseEntity.ok("Course deleted successfully.");
        }
        return ResponseEntity.status(404).body("Course not found.");
    }


    @GetMapping("/get/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Integer courseId) {
        CourseDto dto = courseService.getCourseById(courseId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/by-trainer/{trainerId}")
    public ResponseEntity<List<CourseDto>> getCoursesByTrainer(@PathVariable Integer trainerId) {
        List<CourseDto> courses = courseService.getCoursesByTrainerId(trainerId);
        return ResponseEntity.ok(courses);
    }
}
