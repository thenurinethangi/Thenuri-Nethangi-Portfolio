package com.example.ormcoursework.dao;

import com.example.ormcoursework.bo.custom.impl.MedicalHistoryBOImpl;
import com.example.ormcoursework.bo.custom.impl.TherapistProgramAssignmentBOImpl;
import com.example.ormcoursework.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory(){

    }

    public static DAOFactory getInstance(){

        return daoFactory ==null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOType{

        USER,THERAPY_PROGRAM,THERAPIST,SCHEDULE,PATIENT,SESSION,PAYMENT,REGISTER,
        THERAPY_RECORD,MEDICAL_HISTORY,QUERY,THERAPIST_PROGRAM_ASSIGNMENT
    }

    public <T extends SuperDAO> T getDAO(DAOType type){

        switch (type){

            case USER -> {
                return (T)new UserDAOImpl();
            }
            case THERAPY_PROGRAM -> {
                return (T)new TherapyProgramDAOImpl();
            }
            case THERAPIST -> {
                return (T)new TherapistDAOImpl();
            }
            case SCHEDULE -> {
                return (T)new ScheduleDAOImpl();
            }
            case PATIENT -> {
                return (T)new PatientDAOImpl();
            }
            case REGISTER -> {
                return (T)new RegisterDAOImpl();
            }
            case PAYMENT -> {
                return (T)new PaymentDAOImpl();
            }
            case QUERY -> {
                return (T)new QueryDAOImpl();
            }
            case SESSION -> {
                return (T)new SessionDAOImpl();
            }
            case THERAPIST_PROGRAM_ASSIGNMENT -> {
                return (T)new TherapistProgramAssignmentDAOImpl();
            }
            case MEDICAL_HISTORY -> {
                return (T)new MedicalHistoryDAOImpl();
            }
            default -> {
                return null;
            }
        }
    }
}
