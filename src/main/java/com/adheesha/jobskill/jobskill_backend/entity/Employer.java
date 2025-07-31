package com.adheesha.jobskill.jobskill_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "employer")
@NoArgsConstructor
@AllArgsConstructor
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String contactNumber;
    private String address;

    @Column(name = "profile_image")
    private String profileImage; // file path like uploads/logos/logo.png


    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    public Employer(User user, String companyName, String contactNumber, String address) {
        this.user = user;
        this.companyName = companyName;
        this.contactNumber = contactNumber;
        this.address = address;
    }


}
