package com.example.ormcoursework.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "sessions")
public class TherapySession {

    @Id
    private String sessionId;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "therapyProgramId")
    private TherapyProgram therapyProgram;

    @ManyToOne
    @JoinColumn(name = "therapistId")
    private Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "paymentId")
    private Payment payment;
}










