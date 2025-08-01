package com.adheesha.jobskill.jobskill_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "skill")
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer skillId;

    private String skillName;

    // 🔗 Many-to-One: Many skills can belong to one job_seeker
    @ManyToOne
    @JoinColumn(name = "seekerId", nullable = false)
    private JobSeeker seeker;
}
