package com.example.ormcoursework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
public class Therapist {

    @Id
    @Column(updatable = false)
    private String therapistId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNo;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "therapist")
    private List<Schedule> schedules;

    public Therapist(String therapistId) {
        this.therapistId = therapistId;
    }
}






