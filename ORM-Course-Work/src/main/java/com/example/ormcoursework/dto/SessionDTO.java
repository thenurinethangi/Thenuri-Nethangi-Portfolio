package com.example.ormcoursework.dto;

import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapyProgram;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SessionDTO {

    private String sessionId;
    private String date;
    private String time;
    private String status;
    private Patient patient;
    private TherapyProgram therapyProgram;
    private Therapist therapist;
    private Payment payment;
}
