package com.example.ormcoursework.bo;

import com.example.ormcoursework.bo.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getInstance(){

        return boFactory==null ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOType{

        USER,THERAPY_PROGRAM,THERAPIST,SCHEDULE,PATIENT,SESSION,PAYMENT,REGISTER,
        THERAPY_RECORD,MEDICAL_HISTORY,DASHBOARD,THERAPIST_PROGRAM_ASSIGNMENT
    }

    public <T extends SuperBO> T getBO(BOType type){

        switch (type){

            case USER -> {
                return (T)new UserBOImpl();
            }
            case THERAPY_PROGRAM -> {
                return (T)new TherapyProgramBOImpl();
            }
            case THERAPIST -> {
                return (T)new TherapistBOImpl();
            }
            case SCHEDULE -> {
                return (T)new ScheduleBOImpl();
            }
            case PATIENT -> {
                return (T)new PatientBOImpl();
            }
            case REGISTER -> {
                return (T)new RegisterBOImpl();
            }
            case PAYMENT -> {
                return (T)new PaymentBOImpl();
            }
            case DASHBOARD -> {
                return (T)new DashboardBOImpl();
            }
            case SESSION -> {
                return (T)new SessionBOImpl();
            }
            case THERAPIST_PROGRAM_ASSIGNMENT -> {
                return (T)new TherapistProgramAssignmentBOImpl();
            }
            case MEDICAL_HISTORY -> {
                return (T)new MedicalHistoryBOImpl();
            }
            default -> {
                return null;
            }
        }
    }
}
