package com.adheesha.jobskill.jobskill_backend.entity;

import com.adheesha.jobskill.jobskill_backend.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    private String title;
    private String description;
    private String duration;

    // Add the status field using the enum
    @Enumerated(EnumType.STRING) // Stores the enum name (e.g., "ACTIVE") as a string in the DB
    private CourseStatus status;

    @ManyToOne
    @JoinColumn(name = "trainerId", nullable = false)
    private Trainer trainer;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();
}
