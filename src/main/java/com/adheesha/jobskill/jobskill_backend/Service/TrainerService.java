package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.dto.TrainerDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrainerService {
    CourseDto addCourse(CourseDto courseDto);
    CourseDto updateCourse(CourseDto courseDto);
    boolean deleteCourse(Integer courseId);
    CourseDto getCourseById(Integer courseId);
    List<CourseDto> listCoursesByTrainer(Integer trainerId);


    // Trainer profile methods
    TrainerDto createTrainerWithProfile(String qualification,
                                        String contactNumber,
                                        String experience,
                                        String courseCategory,
                                        MultipartFile profileImage,
                                        Integer userId);
    TrainerDto updateTrainerWithProfile(Integer trainerId,
                                        String qualification,
                                        String contactNumber,
                                        String experience,
                                        String courseCategory,
                                        MultipartFile profileImage);

    TrainerDto getTrainerWithProfile(Integer trainerId);

    TrainerDto getTrainerByUserId(Integer userId);

    List<TrainerDto> getAllTrainers();

    void deleteTrainerById(Integer trainerId);
}
