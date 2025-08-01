package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto);
    EnrollmentDto updateEnrollment(EnrollmentDto enrollmentDto);
    EnrollmentDto getEnrollmentById(Integer enrollmentId);

    List<EnrollmentDto> getEnrollmentsBySeekerId(Integer seekerId);

    List<EnrollmentDto> getEnrollmentsByCourseId(Integer courseId);

    boolean deleteEnrollment(Integer enrollmentId);

    EnrollmentDto getEnrollmentBySeekerIdAndCourseId(Integer seekerId, Integer courseId);

    List<EnrollmentDto> getEnrollmentsByTrainer(Integer trainerId);
}
