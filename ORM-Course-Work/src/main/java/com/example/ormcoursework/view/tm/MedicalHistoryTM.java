package com.example.ormcoursework.view.tm;

import com.example.ormcoursework.entity.Patient;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MedicalHistoryTM {

    private String medicalHistoryId;
    private String patientId;
    private String primaryDiagnosis;
    private String bloodPressure;
    private String sleepingHabit;
    private String therapyResponse;
    private String condition;
    private Date date;
}
