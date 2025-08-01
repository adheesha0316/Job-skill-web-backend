package com.adheesha.jobskill.jobskill_backend.repo;

import com.adheesha.jobskill.jobskill_backend.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepo extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUser_UserId(Integer userId);

}
