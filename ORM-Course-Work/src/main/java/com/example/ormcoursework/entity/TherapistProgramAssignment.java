package com.example.ormcoursework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "therapistprogramassignment")
public class TherapistProgramAssignment {

    @EmbeddedId
    private TherapistProgramAssignmentFK id;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    @MapsId("therapist_id")
    private Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "therapyProgram_id")
    @MapsId("therapyProgram_id")
    private  TherapyProgram therapyProgram;

    @Column(nullable = false)
    private Date assignmentDate;

    @Column(nullable = false)
    private String assignmentStatus;
}




