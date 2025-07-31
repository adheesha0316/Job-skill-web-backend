package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.CourseService;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.entity.Course;
import com.adheesha.jobskill.jobskill_backend.entity.Trainer;
import com.adheesha.jobskill.jobskill_backend.repo.CourseRepo;
import com.adheesha.jobskill.jobskill_backend.repo.TrainerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepo courseRepo;
    private final TrainerRepo trainerRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseServiceImpl(CourseRepo courseRepo, TrainerRepo trainerRepo, ModelMapper modelMapper) {
        this.courseRepo = courseRepo;
        this.trainerRepo = trainerRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CourseDto addCourse(CourseDto courseDto) {
        Optional<Trainer> optionalTrainer = trainerRepo.findById(courseDto.getTrainerId());
        if (optionalTrainer.isEmpty()) {
            throw new RuntimeException("Trainer not found with ID: " + courseDto.getTrainerId());
        }

        Course course = modelMapper.map(courseDto, Course.class);
        course.setTrainer(optionalTrainer.get());

        Course saved = courseRepo.save(course);
        CourseDto result = modelMapper.map(saved, CourseDto.class);
        result.setTrainerId(saved.getTrainer().getTrainerId());
        return result;
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        Optional<Course> optionalCourse = courseRepo.findById(courseDto.getCourseId());
        if (optionalCourse.isEmpty()) {
            throw new RuntimeException("Course not found with ID: " + courseDto.getCourseId());
        }

        Optional<Trainer> optionalTrainer = trainerRepo.findById(courseDto.getTrainerId());
        if (optionalTrainer.isEmpty()) {
            throw new RuntimeException("Trainer not found with ID: " + courseDto.getTrainerId());
        }

        Course course = optionalCourse.get();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setDuration(courseDto.getDuration());
        course.setStatus(courseDto.getStatus());
        course.setTrainer(optionalTrainer.get()); // important

        Course updated = courseRepo.save(course);
        CourseDto result = modelMapper.map(updated, CourseDto.class);
        result.setTrainerId(updated.getTrainer().getTrainerId());
        return result;
    }

    @Override
    public boolean deleteCourse(Integer courseId) {
        if (courseRepo.existsById(courseId)) {
            courseRepo.deleteById(courseId);
            return true;
        }
        return false;
    }

    @Override
    public CourseDto getCourseById(Integer courseId) {
        return courseRepo.findById(courseId)
                .map(course -> {
                    CourseDto dto = modelMapper.map(course, CourseDto.class);
                    dto.setTrainerId(course.getTrainer().getTrainerId());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepo.findAll().stream()
                .map(course -> {
                    CourseDto dto = modelMapper.map(course, CourseDto.class);
                    dto.setTrainerId(course.getTrainer().getTrainerId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesByTrainerId(Integer trainerId) {
        return courseRepo.findByTrainerTrainerId(trainerId).stream()
                .map(course -> {
                    CourseDto dto = modelMapper.map(course, CourseDto.class);
                    dto.setTrainerId(course.getTrainer().getTrainerId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
