package com.example.ormcoursework.dto;

import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapistProgramAssignmentFK;
import com.example.ormcoursework.entity.TherapyProgram;
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
public class TherapistProgramAssignmentDTO {

    private TherapistProgramAssignmentFK id;
    private Therapist therapist;
    private TherapyProgram therapyProgram;
    private Date assignmentDate;
    private String assignmentStatus;


    public TherapistProgramAssignmentDTO(Therapist therapist, TherapyProgram therapyProgram, Date assignmentDate, String assignmentStatus) {
        this.therapist = therapist;
        this.therapyProgram = therapyProgram;
        this.assignmentDate = assignmentDate;
        this.assignmentStatus = assignmentStatus;
    }
}
