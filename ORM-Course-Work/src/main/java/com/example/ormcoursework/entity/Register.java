package com.example.ormcoursework.entity;

import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.TherapyProgram;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class Register {

    @Id
    private String registrationId;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(nullable = false)
    private int totalSessionsCountCanTake;

    @Column(nullable = false)
    private int sessionsTaken;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "therapy")
    private TherapyProgram therapy;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "paymentId")
    private Payment payment;

}









