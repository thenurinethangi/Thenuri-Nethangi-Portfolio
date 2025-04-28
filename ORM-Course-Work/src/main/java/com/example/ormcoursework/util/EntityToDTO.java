package com.example.ormcoursework.util;

import com.example.ormcoursework.dto.*;
import com.example.ormcoursework.entity.*;

public class EntityToDTO {

    public static UserDTO ToUserDTO(User user){

        return new UserDTO(user.getUserName(), user.getPassword(), user.getFullName(), user.getEmail(), user.getRole());

    }

    public static TherapyProgramDTO ToTherapyProgramDTO(TherapyProgram program){

        TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO();

        therapyProgramDTO.setTherapyId(program.getTherapyId());
        therapyProgramDTO.setName(program.getName());
        therapyProgramDTO.setDuration(program.getDuration());
        therapyProgramDTO.setFee(program.getFee());
        therapyProgramDTO.setUpfrontPayment(program.getUpfrontPayment());
        therapyProgramDTO.setFeeForASingleSession(program.getFeeForASingleSession());
        therapyProgramDTO.setActive(program.isActive());

        return therapyProgramDTO;

    }


    public static TherapistDTO ToTherapistDTO(Therapist therapist){

        TherapistDTO therapistDTO = new TherapistDTO();

        therapistDTO.setTherapistId(therapist.getTherapistId());
        therapistDTO.setName(therapist.getName());
        therapistDTO.setPhoneNo(therapist.getPhoneNo());
        therapistDTO.setSpecialization(therapist.getSpecialization());
        therapistDTO.setActive(therapist.isActive());

        return therapistDTO;

    }


    public static ScheduleDTO ToScheduleDTO(Schedule schedule){

        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setScheduleNo(schedule.getScheduleNo());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setBookedSessionCount(schedule.getBookedSessionCount());
        scheduleDTO.setAvailable(schedule.getAvailable());
        scheduleDTO.setTherapist(schedule.getTherapist());

        return scheduleDTO;

    }


    public static PatientDTO ToPatientDTO(Patient patient){

        PatientDTO patientDTO = new PatientDTO();

        patientDTO.setPatientId(patient.getPatientId());
        patientDTO.setName(patient.getName());
        patientDTO.setPhoneNo(patient.getPhoneNo());
        patientDTO.setAge(patient.getAge());
        patientDTO.setGender(patient.getGender());
        patientDTO.setEnrolledTherapyProgramCount(patient.getEnrolledTherapyProgramCount());
        patientDTO.setActive(patient.isActive());

        return patientDTO;
    }


    public static RegisterDTO ToRegisterDTO(Register register){

        RegisterDTO registerDTO = new RegisterDTO();

        registerDTO.setRegistrationId(register.getRegistrationId());
        registerDTO.setPatient(register.getPatient());
        registerDTO.setTherapy(register.getTherapy());
        registerDTO.setStartDate(register.getStartDate());
        registerDTO.setEndDate(register.getEndDate());
        registerDTO.setPayment(register.getPayment());
        registerDTO.setTotalSessionsCountCanTake(register.getTotalSessionsCountCanTake());
        registerDTO.setSessionsTaken(register.getSessionsTaken());
        registerDTO.setStatus(register.getStatus());

        return registerDTO;
    }


    public static PaymentDTO ToPaymentDTO(Payment payment){

        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setDate(payment.getDate());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setStatus(payment.getStatus());

        return paymentDTO;
    }


    public static SessionDTO ToSessionDTO(TherapySession therapySession){

        SessionDTO sessionDTO = new SessionDTO();

        sessionDTO.setSessionId(therapySession.getSessionId());
        sessionDTO.setDate(therapySession.getDate());
        sessionDTO.setTime(therapySession.getTime());
        sessionDTO.setStatus(therapySession.getStatus());
        sessionDTO.setPatient(therapySession.getPatient());
        sessionDTO.setTherapyProgram(therapySession.getTherapyProgram());
        sessionDTO.setTherapist(therapySession.getTherapist());
        sessionDTO.setPayment(therapySession.getPayment());

        return sessionDTO;
    }


    public static TherapistProgramAssignmentDTO ToTherapistProgramAssignmentDTO(TherapistProgramAssignment therapistProgramAssignment){

        TherapistProgramAssignmentDTO therapistProgramAssignmentDTO = new TherapistProgramAssignmentDTO();

        therapistProgramAssignmentDTO.setTherapist(therapistProgramAssignment.getTherapist());
        therapistProgramAssignmentDTO.setTherapyProgram(therapistProgramAssignment.getTherapyProgram());
        therapistProgramAssignmentDTO.setAssignmentDate(therapistProgramAssignment.getAssignmentDate());
        therapistProgramAssignmentDTO.setAssignmentStatus(therapistProgramAssignment.getAssignmentStatus());

        return therapistProgramAssignmentDTO;
    }


    public static MedicalHistoryDTO ToMedicalHistoryDTO(MedicalHistory medicalHistory){

        MedicalHistoryDTO medicalHistoryDTO = new MedicalHistoryDTO();

        medicalHistoryDTO.setMedicalHistoryId(medicalHistory.getMedicalHistoryId());
        medicalHistoryDTO.setPatient(medicalHistory.getPatient());
        medicalHistoryDTO.setPrimaryDiagnosis(medicalHistory.getPrimaryDiagnosis());
        medicalHistoryDTO.setBloodPressure(medicalHistory.getBloodPressure());
        medicalHistoryDTO.setSleepingHabit(medicalHistory.getSleepingHabit());
        medicalHistoryDTO.setTherapyResponse(medicalHistory.getTherapyResponse());
        medicalHistoryDTO.setCondition(medicalHistory.getPrimaryCondition());
        medicalHistoryDTO.setDate(medicalHistory.getRecordedDate());

        return medicalHistoryDTO;
    }
}

