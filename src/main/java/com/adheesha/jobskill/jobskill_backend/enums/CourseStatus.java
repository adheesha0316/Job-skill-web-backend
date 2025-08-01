package com.adheesha.jobskill.jobskill_backend.enums;

public enum CourseStatus {
    // --- Current State ---
    ACTIVE,          // The course is currently available and likely open for enrollment or running.
    PENDING_REVIEW,  // The course is awaiting internal approval or review before being offered.
    UPCOMING,        // The course is scheduled for a future offering, but not yet active.
    FULL,            // The course is running but has no more available slots.
    COMPLETED,       // The course offering has finished.

    // --- Unavailable States ---
    ON_HOLD,         // The course's offering is temporarily paused.
    CANCELED,        // The specific course offering has been withdrawn.
    ARCHIVED,        // The course is no longer offered and is kept for historical records.
    DRAFT            // The course content is still being developed.
}
