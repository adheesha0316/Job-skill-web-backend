package com.adheesha.jobskill.jobskill_backend.repo;

import com.adheesha.jobskill.jobskill_backend.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerRepo extends JpaRepository<JobSeeker, Integer> {
    Optional<JobSeeker> findByUserUserId(Integer userId);

}
