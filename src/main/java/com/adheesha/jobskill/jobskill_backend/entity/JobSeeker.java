package com.adheesha.jobskill.jobskill_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "job_seeker")
@NoArgsConstructor
@AllArgsConstructor
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seekerId;
    private String contactNumber;
    private String experience;
    private String resumePath;

    @Column(name = "profile_image")
    private String profileImage;

    // 🔗 Many-to-One with User (each seeker is a user)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔗 One-to-Many with Skills
    @OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    // 🔗 One-to-Many with Applications
    @OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    // 🔗 One-to-Many with Enrollments (if taking courses)
    @OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;


}
