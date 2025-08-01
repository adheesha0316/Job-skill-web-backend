package com.adheesha.jobskill.jobskill_backend.repo;

import com.adheesha.jobskill.jobskill_backend.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application, Integer> {
    List<Application> findBySeeker_SeekerId(Integer seekerId);
    List<Application> findByJob_JobId(Integer jobId);
    List<Application> findByJob_Employer_EmployerId(Integer employerId);
}
