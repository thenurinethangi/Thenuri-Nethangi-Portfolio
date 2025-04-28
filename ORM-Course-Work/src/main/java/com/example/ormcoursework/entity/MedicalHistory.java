package com.example.ormcoursework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class MedicalHistory {

    @Id
    private String medicalHistoryId;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @Column(nullable = false)
    private String primaryDiagnosis;

    @Column(nullable = false)
    private String bloodPressure;

    @Column(nullable = false)
    private String sleepingHabit;

    @Column(nullable = false)
    private String therapyResponse;

    @Column(nullable = false)
    private String primaryCondition;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date RecordedDate;
}








