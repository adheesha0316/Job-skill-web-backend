package com.adheesha.jobskill.jobskill_backend.Service;

public interface CVMatchService {
    double getMatchScore(String resumeUrl, Integer jobId);
}
