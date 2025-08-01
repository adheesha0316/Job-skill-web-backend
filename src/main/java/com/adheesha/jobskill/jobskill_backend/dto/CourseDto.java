package com.adheesha.jobskill.jobskill_backend.dto;

import com.adheesha.jobskill.jobskill_backend.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Integer courseId;
    private String title;
    private String description;
    private String duration;
    private CourseStatus status;
    private Integer trainerId;
}
