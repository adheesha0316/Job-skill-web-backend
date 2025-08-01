package com.adheesha.jobskill.jobskill_backend.repo;

import com.adheesha.jobskill.jobskill_backend.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerRepo extends JpaRepository<Employer, Integer> {
    Optional<Employer> findByUser_UserId(Integer userId);

}
