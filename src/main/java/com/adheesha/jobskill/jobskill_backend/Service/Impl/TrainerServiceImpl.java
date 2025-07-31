package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.TrainerService;
import com.adheesha.jobskill.jobskill_backend.dto.CourseDto;
import com.adheesha.jobskill.jobskill_backend.dto.TrainerDto;
import com.adheesha.jobskill.jobskill_backend.entity.Course;
import com.adheesha.jobskill.jobskill_backend.entity.Trainer;
import com.adheesha.jobskill.jobskill_backend.entity.User;
import com.adheesha.jobskill.jobskill_backend.repo.CourseRepo;
import com.adheesha.jobskill.jobskill_backend.repo.TrainerRepo;
import com.adheesha.jobskill.jobskill_backend.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepo trainerRepo;
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;

    private static final String PROFILE_DIR = System.getProperty("user.dir") + "/uploads/profiles/";

    @Autowired
    public TrainerServiceImpl(TrainerRepo trainerRepo, CourseRepo courseRepo, ModelMapper modelMapper, UserRepo userRepo) {
        this.trainerRepo = trainerRepo;
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
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
        return modelMapper.map(saved, CourseDto.class);
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        Optional<Course> optionalCourse = courseRepo.findById(courseDto.getCourseId());
        if (optionalCourse.isEmpty()) {
            throw new RuntimeException("Course not found with ID: " + courseDto.getCourseId());
        }

        Course course = optionalCourse.get();

        // Update basic fields
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setDuration(courseDto.getDuration());

        // If trainer update is allowed
        if (courseDto.getTrainerId() != null) {
            Optional<Trainer> optionalTrainer = trainerRepo.findById(courseDto.getTrainerId());
            optionalTrainer.ifPresent(course::setTrainer);
        }

        Course updated = courseRepo.save(course);
        return modelMapper.map(updated, CourseDto.class);
    }

    @Override
    public boolean deleteCourse(Integer courseId) {
        if (!courseRepo.existsById(courseId)) {
            return false;
        }
        courseRepo.deleteById(courseId);
        return true;
    }

    @Override
    public CourseDto getCourseById(Integer courseId) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            return modelMapper.map(courseOpt.get(), CourseDto.class);
        }
        return null;
    }

    @Override
    public List<CourseDto> listCoursesByTrainer(Integer trainerId) {
        List<Course> courses = courseRepo.findByTrainerTrainerId(trainerId);
        return courses.stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public TrainerDto createTrainerWithProfile(String qualification, String contactNumber, String experience, String courseCategory, MultipartFile profileImage, Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Trainer trainer = new Trainer();
        trainer.setQualification(qualification);
        trainer.setContactNumber(contactNumber);
        trainer.setExperience(experience);
        trainer.setCourseCategory(courseCategory);
        trainer.setProfileImage(profileImage.getOriginalFilename());
        trainer.setUser(user);


        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            try {
                File dir = new File(PROFILE_DIR);
                if (!dir.exists()) dir.mkdirs();

                profileImage.transferTo(new File(PROFILE_DIR + fileName));
                trainer.setProfileImage("uploads/profiles/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload trainer image", e);
            }
        } else {
            trainer.setProfileImage("uploads/profiles/default.png");
        }

        Trainer saved = trainerRepo.save(trainer);
        return mapTrainer(saved);
    }

    @Override
    public TrainerDto updateTrainerWithProfile(Integer trainerId, String qualification, String contactNumber, String experience, String courseCategory, MultipartFile profileImage) {
        Trainer trainer = trainerRepo.findById(trainerId).orElseThrow(() -> new RuntimeException("Trainer not found"));

        trainer.setTrainerId(trainerId);
        trainer.setQualification(qualification.trim());
        trainer.setContactNumber(contactNumber.trim());
        trainer.setExperience(experience.trim());
        trainer.setCourseCategory(courseCategory.trim());


        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            try {
                File dir = new File(PROFILE_DIR);
                if (!dir.exists()) dir.mkdirs();

                profileImage.transferTo(new File(PROFILE_DIR + fileName));
                trainer.setProfileImage("uploads/profiles/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update trainer image", e);
            }
        }

        Trainer updated = trainerRepo.save(trainer);

        TrainerDto dto = mapTrainer(updated);
        dto.setTrainerId(updated.getTrainerId());
        dto.setUserId(updated.getUser().getUserId());

        return dto;
    }

    @Override
    public TrainerDto getTrainerWithProfile(Integer trainerId) {
        Trainer trainer = trainerRepo.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found for Trainer ID: " + trainerId));
        return modelMapper.map(trainer, TrainerDto.class);
    }

    @Override
    public TrainerDto getTrainerByUserId(Integer userId) {
        Trainer trainer = trainerRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Trainer not found for User ID: " + userId));

        TrainerDto dto = modelMapper.map(trainer, TrainerDto.class);

        // Clean and format profile image URL
        if (trainer.getProfileImage() != null && !trainer.getProfileImage().isEmpty()) {
            String profileImage = trainer.getProfileImage().replace("\\", "/"); // Normalize path

            // Remove leading "uploads/" if present
            if (profileImage.startsWith("uploads/")) {
                profileImage = profileImage.substring("uploads/".length());
            }

            // Construct full URL
            dto.setProfileImage("http://localhost:8080/" + profileImage);
        }

        if (trainer.getUser() != null) {
            dto.setUserId(trainer.getUser().getUserId());
            dto.setUserName(trainer.getUser().getUserName());
            dto.setEmail(trainer.getUser().getEmail());
        }

        return dto;
    }

    @Override
    public List<TrainerDto> getAllTrainers() {
        return trainerRepo.findAll().stream()
                .map(trainer -> {
                    TrainerDto dto = new TrainerDto();
                    dto.setTrainerId(trainer.getTrainerId());
                    dto.setQualification(trainer.getQualification());
                    dto.setContactNumber(trainer.getContactNumber());
                    dto.setExperience(trainer.getExperience());
                    dto.setCourseCategory(trainer.getCourseCategory());

                    // Set profile image URL if available
                    if (trainer.getProfileImage() != null) {
                        dto.setProfileImage("http://localhost:8080/" + trainer.getProfileImage());
                    }

                    // Set user details if available
                    if (trainer.getUser() != null) {
                        dto.setUserId(trainer.getUser().getUserId());
                        dto.setUserName(trainer.getUser().getUserName());
                        dto.setEmail(trainer.getUser().getEmail());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public void deleteTrainerById(Integer trainerId) {
        Trainer trainer = trainerRepo.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + trainerId));
        trainerRepo.delete(trainer);
    }

    private TrainerDto mapTrainer(Trainer trainer) {
        TrainerDto dto = modelMapper.map(trainer, TrainerDto.class);
        dto.setUserId(trainer.getUser().getUserId());
        return dto;
    }
}
