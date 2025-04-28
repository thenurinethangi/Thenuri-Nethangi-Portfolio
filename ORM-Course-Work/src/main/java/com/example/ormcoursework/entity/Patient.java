package com.example.ormcoursework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class Patient {

    @Id
    private String patientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String phoneNo;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int enrolledTherapyProgramCount = 0;

    @Column(nullable = false)
    private boolean isActive;


    public Patient(String patientId) {
        this.patientId = patientId;
    }
}



