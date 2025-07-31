package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.CVMatchService;
import com.adheesha.jobskill.jobskill_backend.config.AppConfig;
import com.adheesha.jobskill.jobskill_backend.entity.Job;
import com.adheesha.jobskill.jobskill_backend.repo.JobRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String apiKey = appConfig.getExternalApiKey();
        String externalApiUrl = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String cvText = downloadResumeText(resumeUrl);

        // Return early if resume text is empty or null
        if (cvText == null || cvText.isBlank()) {
            System.out.println("Resume text is empty.");
            return 0.0;
        }
        String jobDescription = getJobDescription(jobId);

        String prompt = "You are an assistant that scores how well a CV matches a job description.\n\n"
                + "CV Content:\n" + cvText + "\n\n"
                + "Job Description:\n" + jobDescription + "\n\n"
                + "Return only a decimal number between 0 and 1 representing the match score.";

        Map<String, Object> messageSystem = Map.of(
                "role", "system",
                "content", "You are a helpful assistant that scores CV match."
        );

        Map<String, Object> messageUser = Map.of(
                "role", "user",
                "content", prompt
        );

        List<Map<String, Object>> messages = List.of(messageSystem, messageUser);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 10);
        requestBody.put("temperature", 0);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map body = response.getBody();
                var choices = (List<Map>) body.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map firstChoice = choices.get(0);
                    Map message = (Map) firstChoice.get("message");
                    if (message != null) {
                        String content = ((String) message.get("content")).trim();
                        Pattern pattern = Pattern.compile("(0\\.\\d+|1\\.0|1)");
                        Matcher matcher = pattern.matcher(content);
                        if (matcher.find()) {
                            return Double.parseDouble(matcher.group());
                        } else {
                            System.out.println("Cannot parse score from content: " + content);
                            return 0.0;
                        }
                    }
                }
            }
            System.out.println("API call returned status: " + response.getStatusCode());
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
