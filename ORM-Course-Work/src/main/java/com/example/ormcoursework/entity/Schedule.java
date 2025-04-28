package com.example.ormcoursework.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class Schedule {

    @Id
    private String scheduleNo;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int bookedSessionCount;

    @Column(nullable = false)
    private String available;

    @ManyToOne
    @JoinColumn(name = "therapistId")
    private Therapist therapist;
}









