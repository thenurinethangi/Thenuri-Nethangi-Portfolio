package com.example.ormcoursework.dto;

import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.TherapyProgram;

import java.util.Date;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RegisterDTO {

    private String registrationId;
    private Date startDate;
    private Date endDate;
    private int totalSessionsCountCanTake;
    private int sessionsTaken;
    private String status;
    private TherapyProgram therapy;
    private Patient patient;
    private Payment payment;

    public RegisterDTO(String registrationId, Date startDate,String status,TherapyProgram therapy,Patient patient) {
        this.registrationId = registrationId;
        this.startDate = startDate;
        this.status = status;
        this.therapy = therapy;
        this.patient = patient;

    }

    public RegisterDTO(String registrationId, Date startDate,String status,TherapyProgram therapy,Patient patient,Payment payment) {
        this.registrationId = registrationId;
        this.startDate = startDate;
        this.status = status;
        this.therapy = therapy;
        this.patient = patient;
        this.payment = payment;
    }
}
