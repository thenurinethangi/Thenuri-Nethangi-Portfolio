package com.example.ormcoursework.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TherapistProgramAssignmentFK {

    private String therapist_id;
    private String therapyProgram_id;
}
