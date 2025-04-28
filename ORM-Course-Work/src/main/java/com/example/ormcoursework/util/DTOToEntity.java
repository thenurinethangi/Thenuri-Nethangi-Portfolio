package com.example.ormcoursework.util;

import com.example.ormcoursework.dto.*;
import com.example.ormcoursework.entity.*;

public class DTOToEntity {

    public static User ToUserEntity(UserDTO userDTO){

        return new User(userDTO.getUserName(), userDTO.getPassword(), userDTO.getFullName(), userDTO.getEmail(), userDTO.getRole());

    }

    public static TherapyProgram ToTherapyProgramEntity(TherapyProgramDTO therapyProgramDTO){

        TherapyProgram therapyProgram = new TherapyProgram();

        therapyProgram.setTherapyId(therapyProgramDTO.getTherapyId());
        therapyProgram.setName(therapyProgramDTO.getName());
        therapyProgram.setDuration(therapyProgramDTO.getDuration());
        therapyProgram.setFee(therapyProgramDTO.getFee());
        therapyProgram.setUpfrontPayment(therapyProgramDTO.getUpfrontPayment());
        therapyProgram.setFeeForASingleSession(therapyProgramDTO.getFeeForASingleSession());
        therapyProgram.setActive(therapyProgramDTO.isActive());

        return therapyProgram;

    }

    public static Therapist ToTherapistEntity(TherapistDTO therapistDTO){

        Therapist therapist = new Therapist();

        therapist.setTherapistId(therapistDTO.getTherapistId());
        therapist.setName(therapistDTO.getName());
        therapist.setPhoneNo(therapistDTO.getPhoneNo());
        therapist.setSpecialization(therapistDTO.getSpecialization());
        therapist.setActive(therapistDTO.isActive());

        return therapist;

    }


    public static Schedule ToScheduleEntity(ScheduleDTO scheduleDTO){

        Schedule schedule = new Schedule();

        schedule.setScheduleNo(scheduleDTO.getScheduleNo());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setBookedSessionCount(scheduleDTO.getBookedSessionCount());
        schedule.setAvailable(scheduleDTO.getAvailable());
        schedule.setTherapist(scheduleDTO.getTherapist());

        return schedule;

    }

    public static Patient ToPatientEntity(PatientDTO patientDTO){

        Patient patient = new Patient();

        patient.setPatientId(patientDTO.getPatientId());
        patient.setName(patientDTO.getName());
        patient.setPhoneNo(patientDTO.getPhoneNo());
        patient.setAge(patientDTO.getAge());
        patient.setGender(patientDTO.getGender());
        patient.setEnrolledTherapyProgramCount(patientDTO.getEnrolledTherapyProgramCount());
        patient.setActive(patientDTO.isActive());

        return patient;
    }


    public static Register ToRegisterEntity(RegisterDTO registerDTO){

        Register register = new Register();

        register.setRegistrationId(registerDTO.getRegistrationId());
        register.setPatient(registerDTO.getPatient());
        register.setTherapy(registerDTO.getTherapy());
        register.setStartDate(registerDTO.getStartDate());
        register.setEndDate(registerDTO.getEndDate());
        register.setPayment(registerDTO.getPayment());
        register.setTotalSessionsCountCanTake(registerDTO.getTotalSessionsCountCanTake());
        register.setSessionsTaken(registerDTO.getSessionsTaken());
        register.setStatus(registerDTO.getStatus());

        return register;
    }


    public static Payment ToPaymentEntity(PaymentDTO paymentDTO){

        Payment payment = new Payment();

        payment.setPaymentId(paymentDTO.getPaymentId());
        payment.setDate(paymentDTO.getDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setStatus(paymentDTO.getStatus());

        return payment;
    }


    public static TherapySession ToSessionEntity(SessionDTO sessionDTO){

        TherapySession therapySession = new TherapySession();

        therapySession.setSessionId(sessionDTO.getSessionId());
        therapySession.setDate(sessionDTO.getDate());
        therapySession.setTime(therapySession.getTime());
        therapySession.setStatus(sessionDTO.getStatus());
        therapySession.setPatient(sessionDTO.getPatient());
        therapySession.setTherapyProgram(sessionDTO.getTherapyProgram());
        therapySession.setTherapist(sessionDTO.getTherapist());
        therapySession.setPayment(sessionDTO.getPayment());

        return therapySession;
    }


    public static TherapistProgramAssignment ToTherapistProgramAssignmentEntity(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO){

        TherapistProgramAssignment therapistProgramAssignment = new TherapistProgramAssignment();

        therapistProgramAssignment.setId(therapistProgramAssignmentDTO.getId());
        therapistProgramAssignment.setTherapist(therapistProgramAssignmentDTO.getTherapist());
        therapistProgramAssignment.setTherapyProgram(therapistProgramAssignmentDTO.getTherapyProgram());
        therapistProgramAssignment.setAssignmentDate(therapistProgramAssignmentDTO.getAssignmentDate());
        therapistProgramAssignment.setAssignmentStatus(therapistProgramAssignmentDTO.getAssignmentStatus());

        return therapistProgramAssignment;
    }


    public static MedicalHistory ToMedicalHistoryEntity(MedicalHistoryDTO medicalHistoryDTO){

        MedicalHistory medicalHistory= new MedicalHistory();

        medicalHistory.setMedicalHistoryId(medicalHistoryDTO.getMedicalHistoryId());
        medicalHistory.setPatient(medicalHistoryDTO.getPatient());
        medicalHistory.setPrimaryDiagnosis(medicalHistoryDTO.getPrimaryDiagnosis());
        medicalHistory.setBloodPressure(medicalHistoryDTO.getBloodPressure());
        medicalHistory.setSleepingHabit(medicalHistoryDTO.getSleepingHabit());
        medicalHistory.setTherapyResponse(medicalHistoryDTO.getTherapyResponse());
        medicalHistory.setPrimaryCondition(medicalHistoryDTO.getCondition());
        medicalHistory.setRecordedDate(medicalHistoryDTO.getDate());

        return medicalHistory;
    }
}







