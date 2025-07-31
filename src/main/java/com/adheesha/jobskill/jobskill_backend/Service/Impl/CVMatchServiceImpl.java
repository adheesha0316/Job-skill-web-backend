package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.CVMatchService;
import com.adheesha.jobskill.jobskill_backend.config.AppConfig;
import com.adheesha.jobskill.jobskill_backend.entity.Job;
import com.adheesha.jobskill.jobskill_backend.repo.JobRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
public class CVMatchServiceImpl implements CVMatchService {

    @Autowired
    private AppConfig appConfig; // To get your API key

    @Autowired
    private JobRepo jobRepo;  // Make sure import is correct!


    private final RestTemplate restTemplate = new RestTemplate();

    //  Extract actual resume text from uploaded PDF
    public String downloadResumeText(String fileName) {
        try {
            // Create full file path (local system)
            String filePath = System.getProperty("user.dir") + "/uploads/cvs/" + fileName;
            File file = new File(filePath);

            if (!file.exists()) {
                throw new FileNotFoundException("Resume file not found: " + filePath);
            }

            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();

            return text;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public String getJobDescription(Integer jobId) {
        Optional<Job> jobOpt = jobRepo.findById(jobId);
        if (jobOpt.isPresent()) {
            return jobOpt.get().getDescription();
        } else {
            return "Job description not found.";
        }
    }
    @Override
    public double getMatchScore(String resumeUrl, Integer jobId) {
        return 0;
    }
}
