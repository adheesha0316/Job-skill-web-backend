package com.adheesha.jobskill.jobskill_backend.Service;

import com.adheesha.jobskill.jobskill_backend.dto.ApplicationDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerDto;
import com.adheesha.jobskill.jobskill_backend.dto.EmployerWithJobDto;
import com.adheesha.jobskill.jobskill_backend.entity.Employer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EmployerService {
    // ----------------------------
    // CRUD operations for Employer + Profile Image
    // ----------------------------
    EmployerDto addEmployerWithProfileImage(String companyName, String contactNumber,
                                            String address, MultipartFile profileImage, Integer userId);

    EmployerDto getEmployerByUserId(Integer userId);

    EmployerDto updateEmployerWithProfileImage(Integer employerId, String companyName,
                                               String contactNumber, String address, MultipartFile profileImage);

    EmployerDto getEmployerWithProfileImage(Integer employerId);

    EmployerDto deleteEmployerWithProfileImage(Integer employerId);

    List<EmployerDto> getAllEmployersWithImage();

    Optional<Employer> findByUserId(Integer userId);


    // ----------------------------
    // Employer + Jobs Handling
    // ----------------------------
    EmployerWithJobDto saveEmployerWithJob(EmployerWithJobDto employerWithJobDto);

    EmployerWithJobDto deleteEmployerWithJob(Integer employerId);

    List<EmployerWithJobDto> getJobsByEmployerId(Integer employerId);


    // ----------------------------
    // Job Applications
    // ----------------------------
    List<ApplicationDto> getAllApplicationsForEmployer(Integer employerId);

    void acceptApplication(Integer applicationId);

    void rejectApplication(Integer applicationId);
}
