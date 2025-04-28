package com.example.ormcoursework.util;

import com.example.ormcoursework.dto.*;
import com.example.ormcoursework.entity.MedicalHistory;
import com.example.ormcoursework.view.tm.*;

public class DTOToTM {

    public static TherapyProgramTM ToTherapyProgramTM(TherapyProgramDTO therapyProgramDTO){

        TherapyProgramTM therapyProgramTM = new TherapyProgramTM();

        therapyProgramTM.setTherapyId(therapyProgramDTO.getTherapyId());
        therapyProgramTM.setName(therapyProgramDTO.getName());
        therapyProgramTM.setDuration(therapyProgramDTO.getDuration());
        therapyProgramTM.setFee(therapyProgramDTO.getFee());

        return therapyProgramTM;

    }

    public static TherapistTM ToTherapistTM(TherapistDTO therapistDTO){

        TherapistTM therapistTM = new TherapistTM();

        therapistTM.setTherapistId(therapistDTO.getTherapistId());
        therapistTM.setName(therapistDTO.getName());
        therapistTM.setPhoneNo(therapistDTO.getPhoneNo());
        therapistTM.setSpecialization(therapistDTO.getSpecialization());

        return therapistTM;

    }


    public static ScheduleTM ToScheduleTM(ScheduleDTO scheduleDTO){

        ScheduleTM scheduleTM = new ScheduleTM();

        scheduleTM.setScheduleNo(scheduleDTO.getScheduleNo());
        scheduleTM.setDate(scheduleDTO.getDate());
        scheduleTM.setBookedSessionCount(scheduleDTO.getBookedSessionCount());
        scheduleTM.setAvailable(scheduleDTO.getAvailable());
        scheduleTM.setTherapistId(scheduleDTO.getTherapist().getTherapistId());

        return scheduleTM;
    }


    public static PatientTM ToPatientTM(PatientDTO patientDTO){

        PatientTM patientTM = new PatientTM();

        patientTM.setPatientId(patientDTO.getPatientId());
        patientTM.setName(patientDTO.getName());
        patientTM.setPhoneNo(patientDTO.getPhoneNo());
        patientTM.setAge(patientDTO.getAge());
        patientTM.setGender(patientDTO.getGender());
        patientTM.setEnrolledTherapyProgramCount(patientDTO.getEnrolledTherapyProgramCount());

        return patientTM;
    }


    public static RegisterTM ToRegisterTM(RegisterDTO registerDTO){

        RegisterTM registerTM = new RegisterTM();

        registerTM.setRegisterId(registerDTO.getRegistrationId());
        registerTM.setPatientId(registerDTO.getPatient().getPatientId());
        registerTM.setTherapy(registerDTO.getTherapy().getName());
        registerTM.setStartDate(registerDTO.getStartDate());
        registerTM.setEndDate(registerDTO.getEndDate());
        if(registerDTO.getPayment()==null){
            registerTM.setPaymentId(null);
        }
        else{
            registerTM.setPaymentId(registerDTO.getPayment().getPaymentId());
        }
        registerTM.setSessionsTaken(registerDTO.getSessionsTaken());
        registerTM.setStatus(registerDTO.getStatus());

        return registerTM;
    }


    public static PaymentTM ToPaymentTM(PaymentDTO paymentDTO){

        PaymentTM paymentTM = new PaymentTM();

        paymentTM.setPaymentId(paymentDTO.getPaymentId());
        paymentTM.setDate(paymentDTO.getDate());
        paymentTM.setAmount(paymentDTO.getAmount());
        paymentTM.setPaymentMethod(paymentDTO.getPaymentMethod());
        paymentTM.setStatus(paymentDTO.getStatus());

        return paymentTM;
    }


    public static SessionTM ToSessionTM(SessionDTO sessionDTO){

        SessionTM sessionTM = new SessionTM();

        sessionTM.setSessionId(sessionDTO.getSessionId());
        sessionTM.setDate(sessionDTO.getDate());
        sessionTM.setTime(sessionDTO.getTime());
        sessionTM.setStatus(sessionDTO.getStatus());
        sessionTM.setPatientId(sessionDTO.getPatient().getPatientId());
        sessionTM.setTherapy(sessionDTO.getTherapyProgram().getName());
        sessionTM.setTherapistId(sessionDTO.getTherapist().getTherapistId());

        if(sessionDTO.getPayment()==null){
            sessionTM.setPaymentId(null);
        }
        else {
            sessionTM.setPaymentId(sessionDTO.getPayment().getPaymentId());
        }

        return sessionTM;
    }


    public static TherapistProgramAssignmentTM ToTherapistProgramAssignmentTM(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO){

        TherapistProgramAssignmentTM therapistProgramAssignmentTM = new TherapistProgramAssignmentTM();

        therapistProgramAssignmentTM.setTherapistId(therapistProgramAssignmentDTO.getTherapist().getTherapistId());
        therapistProgramAssignmentTM.setTherapyProgram(therapistProgramAssignmentDTO.getTherapyProgram().getName());
        therapistProgramAssignmentTM.setAssignmentDate(therapistProgramAssignmentDTO.getAssignmentDate());
        therapistProgramAssignmentTM.setAssignmentStatus(therapistProgramAssignmentDTO.getAssignmentStatus());

        return therapistProgramAssignmentTM;
    }


    public static MedicalHistoryTM ToMedicalHistoryTM(MedicalHistoryDTO medicalHistoryDTO){

        MedicalHistoryTM medicalHistoryTM = new MedicalHistoryTM();

        medicalHistoryTM.setMedicalHistoryId(medicalHistoryDTO.getMedicalHistoryId());
        medicalHistoryTM.setPatientId(medicalHistoryDTO.getPatient().getPatientId());
        medicalHistoryTM.setPrimaryDiagnosis(medicalHistoryDTO.getPrimaryDiagnosis());
        medicalHistoryTM.setBloodPressure(medicalHistoryDTO.getBloodPressure());
        medicalHistoryTM.setSleepingHabit(medicalHistoryDTO.getSleepingHabit());
        medicalHistoryTM.setTherapyResponse(medicalHistoryDTO.getTherapyResponse());
        medicalHistoryTM.setCondition(medicalHistoryDTO.getCondition());
        medicalHistoryTM.setDate(medicalHistoryDTO.getDate());

        return medicalHistoryTM;
    }
}



