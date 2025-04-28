package com.example.ormcoursework.view.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PatientTM {

    private String patientId;
    private String name;
    private String phoneNo;
    private int age;
    private String gender;
    private int enrolledTherapyProgramCount;
}
