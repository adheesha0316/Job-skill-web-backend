package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto addCourse(CourseDto courseDto);
    CourseDto updateCourse(CourseDto courseDto);
    boolean deleteCourse(Integer courseId);
    CourseDto getCourseById(Integer courseId);
    List<CourseDto> getAllCourses();
    List<CourseDto> getCoursesByTrainerId(Integer trainerId);
}
