package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.EnrollmentService;
import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Override
    public EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto) {
        return null;
    }

    @Override
    public EnrollmentDto updateEnrollment(EnrollmentDto enrollmentDto) {
        return null;
    }

    @Override
    public EnrollmentDto getEnrollmentById(Integer enrollmentId) {
        return null;
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsBySeekerId(Integer seekerId) {
        return List.of();
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByCourseId(Integer courseId) {
        return List.of();
    }

    @Override
    public boolean deleteEnrollment(Integer enrollmentId) {
        return false;
    }

    @Override
    public EnrollmentDto getEnrollmentBySeekerIdAndCourseId(Integer seekerId, Integer courseId) {
        return null;
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByTrainer(Integer trainerId) {
        return List.of();
    }
}
