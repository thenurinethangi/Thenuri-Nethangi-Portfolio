package com.example.ormcoursework.dto;

import com.example.ormcoursework.entity.Patient;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MedicalHistoryDTO {

    private String medicalHistoryId;
    private Patient patient;
    private String primaryDiagnosis;
    private String bloodPressure;
    private String sleepingHabit;
    private String therapyResponse;
    private String condition;
    private Date date;
}
