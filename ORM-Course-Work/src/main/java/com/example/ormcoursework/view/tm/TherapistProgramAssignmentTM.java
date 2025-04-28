package com.example.ormcoursework.view.tm;

import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapistProgramAssignmentFK;
import com.example.ormcoursework.entity.TherapyProgram;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapistProgramAssignmentTM {

    private String therapistId;
    private String therapyProgram;
    private Date assignmentDate;
    private String assignmentStatus;
}
