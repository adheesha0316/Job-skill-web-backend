package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.EnrollmentService;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import com.adheesha.jobskill.jobskill_backend.entity.Course;
import com.adheesha.jobskill.jobskill_backend.entity.Enrollment;
import com.adheesha.jobskill.jobskill_backend.entity.JobSeeker;
import com.adheesha.jobskill.jobskill_backend.repo.CourseRepo;
import com.adheesha.jobskill.jobskill_backend.repo.EnrollmentRepo;
import com.adheesha.jobskill.jobskill_backend.repo.JobSeekerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepo enrollmentRepo;
    private final JobSeekerRepo jobSeekerRepo;
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepo enrollmentRepo, JobSeekerRepo jobSeekerRepo, CourseRepo courseRepo, ModelMapper modelMapper) {
        this.enrollmentRepo = enrollmentRepo;
        this.jobSeekerRepo = jobSeekerRepo;
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto) {
        Enrollment enrollment = new Enrollment();

        Optional<JobSeeker> seekerOpt = jobSeekerRepo.findById(enrollmentDto.getSeekerId());
        Optional<Course> courseOpt = courseRepo.findById(enrollmentDto.getCourseId());

        if (seekerOpt.isEmpty() || courseOpt.isEmpty()) {
            throw new RuntimeException("JobSeeker or Course not found.");
        }

        enrollment.setSeeker(seekerOpt.get());
        enrollment.setCourse(courseOpt.get());
        enrollment.setEnrollmentDate(LocalDate.now());  // set current date

        Enrollment saved = enrollmentRepo.save(enrollment);
        return modelMapper.map(saved, EnrollmentDto.class);
    }

    @Override
    public EnrollmentDto updateEnrollment(EnrollmentDto enrollmentDto) {
        Optional<Enrollment> opt = enrollmentRepo.findById(enrollmentDto.getEnrollmentId());

        if (opt.isEmpty()) {
            throw new RuntimeException("Enrollment not found.");
        }

        Enrollment enrollment = opt.get();

        // Optionally update course & seeker if included
        if (enrollmentDto.getCourseId() != null) {
            courseRepo.findById(enrollmentDto.getCourseId()).ifPresent(enrollment::setCourse);
        }
        if (enrollmentDto.getSeekerId() != null) {
            jobSeekerRepo.findById(enrollmentDto.getSeekerId()).ifPresent(enrollment::setSeeker);
        }

        // You can update the date if you want, or leave it as original
        if (enrollmentDto.getEnrollmentDate() != null) {
            enrollment.setEnrollmentDate(enrollmentDto.getEnrollmentDate());
        }

        Enrollment updated = enrollmentRepo.save(enrollment);
        return modelMapper.map(updated, EnrollmentDto.class);
    }

    @Override
    public EnrollmentDto getEnrollmentById(Integer enrollmentId) {
        return enrollmentRepo.findById(enrollmentId)
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDto.class))
                .orElse(null);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsBySeekerId(Integer seekerId) {
        List<Enrollment> enrollments = enrollmentRepo.findBySeekerSeekerId(seekerId);
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByCourseId(Integer courseId) {
        List<Enrollment> enrollments = enrollmentRepo.findByCourseCourseId(courseId);
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteEnrollment(Integer enrollmentId) {
        if (enrollmentRepo.existsById(enrollmentId)) {
            enrollmentRepo.deleteById(enrollmentId);
            return true;
        }
        return false;
    }

    @Override
    public EnrollmentDto getEnrollmentBySeekerIdAndCourseId(Integer seekerId, Integer courseId) {
        Enrollment enrollment = enrollmentRepo.findBySeekerSeekerIdAndCourseCourseId(seekerId, courseId);
        if (enrollment == null) return null;
        return modelMapper.map(enrollment, EnrollmentDto.class);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByTrainer(Integer trainerId) {
        List<Enrollment> enrollments = enrollmentRepo.findByCourse_Trainer_TrainerId(trainerId);

        return enrollments.stream().map(enrollment -> {
            EnrollmentDto dto = new EnrollmentDto();
            dto.setEnrollmentId(enrollment.getEnrollmentId());
            dto.setEnrollmentDate(enrollment.getEnrollmentDate());
            dto.setCourseId(enrollment.getCourse().getCourseId());
            dto.setCourseTitle(enrollment.getCourse().getTitle());
            dto.setSeekerId(enrollment.getSeeker().getSeekerId());
            dto.setSeekerName(enrollment.getSeeker().getUser().getUserName());
            dto.setEmail(enrollment.getSeeker().getUser().getEmail());
            dto.setContactNumber(enrollment.getSeeker().getContactNumber());
            return dto;
        }).collect(Collectors.toList());
    }
}
