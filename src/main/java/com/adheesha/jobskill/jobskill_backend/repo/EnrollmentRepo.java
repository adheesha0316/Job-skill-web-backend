package com.adheesha.jobskill.jobskill_backend.repo;

import com.adheesha.jobskill.jobskill_backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepo extends JpaRepository<Enrollment, Integer> {
    // Find all enrollments by seeker ID
    List<Enrollment> findBySeekerSeekerId(Integer seekerId);

    // Find all enrollments by course ID
    List<Enrollment> findByCourseCourseId(Integer courseId);

    // Optional: Find enrollment for a specific seeker and course
    Enrollment findBySeekerSeekerIdAndCourseCourseId(Integer seekerId, Integer courseId);

    List<Enrollment> findByCourse_Trainer_TrainerId(Integer trainerId);
}
