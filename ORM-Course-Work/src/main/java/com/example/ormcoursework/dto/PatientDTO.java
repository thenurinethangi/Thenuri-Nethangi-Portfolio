package com.example.ormcoursework.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PatientDTO {

    private String patientId;
    private String name;
    private String phoneNo;
    private int age;
    private String gender;
    private int enrolledTherapyProgramCount;
    private boolean isActive;

    public PatientDTO(String patientId, String name, String phoneNo, int age, String gender) {
        this.patientId = patientId;
        this.name = name;
        this.phoneNo = phoneNo;
        this.age = age;
        this.gender = gender;
    }
}
