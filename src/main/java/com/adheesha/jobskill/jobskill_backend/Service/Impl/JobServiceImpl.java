package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.JobService;
import com.adheesha.jobskill.jobskill_backend.dto.JobDto;
import com.adheesha.jobskill.jobskill_backend.entity.Employer;
import com.adheesha.jobskill.jobskill_backend.entity.Job;
import com.adheesha.jobskill.jobskill_backend.repo.EmployerRepo;
import com.adheesha.jobskill.jobskill_backend.repo.JobRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepo jobRepo;
    private  final EmployerRepo employerRepo;
    private  final ModelMapper modelMapper;

    private final String LOGO_DIR = System.getProperty("user.dir") + "/uploads/logos/";

    @Autowired
    public JobServiceImpl(JobRepo jobRepo, EmployerRepo employerRepo, ModelMapper modelMapper) {
        this.jobRepo = jobRepo;
        this.employerRepo = employerRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public JobDto createJobWithLogo(String title, String description, String location, String jobType, Double salary, Integer employerId, MultipartFile logoFile) {
        Employer employer = employerRepo.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setJobType(jobType);
        job.setSalary(salary);
        job.setEmployer(employer);

        // Handle logo
        if (logoFile != null && !logoFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + logoFile.getOriginalFilename();
            File dir = new File(System.getProperty("user.dir") + "/uploads/logos/");
            if (!dir.exists()) dir.mkdirs();

            try {
                File dest = new File(dir, fileName);
                logoFile.transferTo(dest);
                job.setLogoPath("uploads/logos/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload logo", e);
            }
        } else {
            job.setLogoPath("uploads/logos/default.png");
        }

        Job saved = jobRepo.save(job);
        return modelMapper.map(saved, JobDto.class);
    }

    @Override
    public JobDto updateJobWithLogo(Integer jobId, String title, String description, String location, String jobType, Double salary, MultipartFile logoFile) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setJobType(jobType);
        job.setSalary(salary);

        if (logoFile != null && !logoFile.isEmpty()) {
            String logoPath = handleLogoUpload(logoFile);
            job.setLogoPath(logoPath);
        }

        Job updated = jobRepo.save(job);
        return modelMapper.map(updated, JobDto.class);
    }

    @Override
    public JobDto getJob(Integer jobId) {
        return jobRepo.findById(jobId)
                .map(job -> modelMapper.map(job, JobDto.class))
                .orElse(null);
    }

    @Override
    public boolean deleteJob(Integer jobId) {
        if (jobRepo.existsById(jobId)) {
            jobRepo.deleteById(jobId);
            return true;
        }
        return false;
    }

    @Override
    public List<JobDto> getAllJobs() {
        return jobRepo.findAll()
                .stream()
                .map(job -> modelMapper.map(job, JobDto.class))
                .collect(Collectors.toList());
    }

    private String handleLogoUpload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("Invalid file name");
            }

            if (!originalFilename.toLowerCase().matches(".*\\.(png|jpg|jpeg)$")) {
                throw new RuntimeException("Only PNG, JPG, or JPEG images are allowed");
            }

            File dir = new File(LOGO_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + originalFilename;
            File dest = new File(LOGO_DIR + fileName);
            file.transferTo(dest);

            return "uploads/logos/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save logo: " + e.getMessage());
        }
    }
}
