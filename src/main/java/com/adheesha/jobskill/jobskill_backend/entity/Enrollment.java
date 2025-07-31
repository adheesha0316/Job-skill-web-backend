package com.adheesha.jobskill.jobskill_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "enrollment")
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enrollmentId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate enrollmentDate;

    // 🔗 Many-to-One: Many enrollments can point to one course
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    // 🔗 Many-to-One: Many enrollments can belong to one seeker
    @ManyToOne
    @JoinColumn(name = "seekerId", nullable = false)
    private JobSeeker seeker;
}
