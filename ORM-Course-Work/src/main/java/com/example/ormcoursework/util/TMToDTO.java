package com.example.ormcoursework.util;

import com.example.ormcoursework.dto.*;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.view.tm.*;

import java.math.BigDecimal;

public class TMToDTO {

    public static TherapyProgramDTO ToTherapyProgramDTO(TherapyProgramTM therapyProgramTM){

        TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO();

        therapyProgramDTO.setTherapyId(therapyProgramTM.getTherapyId());
        therapyProgramDTO.setName(therapyProgramTM.getName());
        therapyProgramDTO.setDuration(therapyProgramTM.getDuration());
        therapyProgramDTO.setFee(therapyProgramTM.getFee());

        return therapyProgramDTO;

    }

    public static TherapistDTO ToTherapistDTO(TherapistTM therapistTM){

        TherapistDTO therapistDTO = new TherapistDTO();

        therapistDTO.setTherapistId(therapistTM.getTherapistId());
        therapistDTO.setName(therapistTM.getName());
        therapistDTO.setPhoneNo(therapistTM.getPhoneNo());
        therapistDTO.setSpecialization(therapistTM.getSpecialization());

        return therapistDTO;

    }


    public static ScheduleDTO ToScheduleDTO(ScheduleTM scheduleTM){

        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setScheduleNo(scheduleTM.getScheduleNo());
        scheduleDTO.setDate(scheduleTM.getDate());
        scheduleDTO.setBookedSessionCount(scheduleTM.getBookedSessionCount());
        scheduleDTO.setAvailable(scheduleTM.getAvailable());
        scheduleDTO.setTherapist(new Therapist(scheduleTM.getTherapistId()));

        return scheduleDTO;
    }


    public static PatientDTO ToPatientDTO(PatientTM patientTM){

        PatientDTO patientDTO = new PatientDTO();

        patientDTO.setPatientId(patientTM.getPatientId());
        patientDTO.setName(patientTM.getName());
        patientDTO.setPhoneNo(patientTM.getPhoneNo());
        patientDTO.setAge(patientTM.getAge());
        patientDTO.setGender(patientTM.getGender());
        patientDTO.setEnrolledTherapyProgramCount(patientTM.getEnrolledTherapyProgramCount());

        return patientDTO;
    }


    public static RegisterDTO ToRegisterDTO(RegisterTM registerTM){

        RegisterDTO registerDTO = new RegisterDTO();

        registerDTO.setRegistrationId(registerTM.getRegisterId());
        registerDTO.setPatient(new Patient(registerTM.getPatientId()));
        registerDTO.setTherapy(new TherapyProgram(registerTM.getTherapy()));
        registerDTO.setStartDate(registerTM.getStartDate());
        registerDTO.setEndDate(registerTM.getEndDate());
        registerDTO.setPayment(new Payment(registerTM.getPaymentId()));
        registerDTO.setSessionsTaken(registerTM.getSessionsTaken());
        registerDTO.setStatus(registerTM.getStatus());

        return registerDTO;
    }


    public static PaymentDTO ToPaymentDTO(PaymentTM paymentTM){

        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setPaymentId(paymentTM.getPaymentId());
        paymentDTO.setDate(paymentTM.getDate());
        paymentDTO.setAmount(paymentTM.getAmount());
        paymentDTO.setPaymentMethod(paymentTM.getPaymentMethod());
        paymentDTO.setStatus(paymentTM.getStatus());

        return paymentDTO;
    }


    public static SessionDTO ToSessionDTO(SessionTM sessionTM){

        SessionDTO sessionDTO = new SessionDTO();

        sessionDTO.setSessionId(sessionTM.getSessionId());
        sessionDTO.setDate(sessionTM.getDate());
        sessionDTO.setTime(sessionDTO.getTime());
        sessionDTO.setStatus(sessionTM.getStatus());
        sessionDTO.setPatient(new Patient(sessionTM.getPatientId()));
        sessionDTO.setTherapyProgram(new TherapyProgram(sessionTM.getTherapy(),new BigDecimal("0.0")));
        sessionDTO.setTherapist(new Therapist(sessionTM.getTherapistId()));
        sessionDTO.setPayment(new Payment(sessionTM.getPaymentId()));

        return sessionDTO;
    }


    public static TherapistProgramAssignmentDTO ToTherapistProgramAssignmentDTO(TherapistProgramAssignmentTM therapistProgramAssignmentTM){

        TherapistProgramAssignmentDTO therapistProgramAssignmentDTO = new TherapistProgramAssignmentDTO();

        therapistProgramAssignmentDTO.setTherapist(new Therapist(therapistProgramAssignmentTM.getTherapistId()));
        therapistProgramAssignmentDTO.setTherapyProgram(new TherapyProgram(therapistProgramAssignmentTM.getTherapyProgram(),new BigDecimal("0.0")));
        therapistProgramAssignmentDTO.setAssignmentDate(therapistProgramAssignmentTM.getAssignmentDate());
        therapistProgramAssignmentDTO.setAssignmentStatus(therapistProgramAssignmentTM.getAssignmentStatus());

        return therapistProgramAssignmentDTO;
    }


    public static MedicalHistoryDTO ToMedicalHistoryDTO(MedicalHistoryTM medicalHistoryTM){

        MedicalHistoryDTO medicalHistoryDTO = new MedicalHistoryDTO();

        medicalHistoryDTO.setMedicalHistoryId(medicalHistoryTM.getMedicalHistoryId());
        medicalHistoryDTO.setPatient(new Patient(medicalHistoryTM.getPatientId()));
        medicalHistoryDTO.setPrimaryDiagnosis(medicalHistoryTM.getPrimaryDiagnosis());
        medicalHistoryDTO.setBloodPressure(medicalHistoryTM.getBloodPressure());
        medicalHistoryDTO.setSleepingHabit(medicalHistoryTM.getSleepingHabit());
        medicalHistoryDTO.setTherapyResponse(medicalHistoryTM.getTherapyResponse());
        medicalHistoryDTO.setCondition(medicalHistoryTM.getCondition());
        medicalHistoryDTO.setDate(medicalHistoryTM.getDate());

        return medicalHistoryDTO;
    }
}




