package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.SessionBO;
import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.*;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.dto.SessionDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.*;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;
import com.example.ormcoursework.util.PaymentMethod;
import com.example.ormcoursework.util.PaymentStatus;
import com.example.ormcoursework.view.tm.SessionTM;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SessionBOImpl implements SessionBO {

    private final SessionDAO sessionDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SESSION);
    private final PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    private final QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);
    private final RegisterDAO registerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REGISTER);
    private final TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    private final TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    private final ScheduleDAO scheduleDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SCHEDULE);

    @Override
    public String generateNewId() {

        return sessionDAO.generateNewId();
    }

    @Override
    public List<SessionDTO> getAll() {

        List<TherapySession> therapySessions = sessionDAO.getAll();
        List<SessionDTO> sessionDTOS = new ArrayList<>();

        for(TherapySession x : therapySessions){
            sessionDTOS.add(EntityToDTO.ToSessionDTO(x));
        }

        return sessionDTOS;
    }

    @Override
    public PatientDTO getPatientById(String str) {

        Patient patient = patientDAO.search(str);

        if(patient!=null){
            return EntityToDTO.ToPatientDTO(patient);
        }
        return null;
    }

    @Override
    public PatientDTO getPatientByPhoneNo(String str) {

        Patient patient = patientDAO.getPatientByPhoneNo(str);

        if(patient!=null){
            return EntityToDTO.ToPatientDTO(patient);
        }
        return null;
    }

    @Override
    public List<TherapyProgramDTO> getEnrolledTherapyProgramList(String patientId) {

        List<TherapyProgram> therapyPrograms = queryDAO.getEnrolledTherapyProgramListOfSelectedPatient(patientId);
        List<TherapyProgramDTO> therapyProgramDTOS = new ArrayList<>();

        for(TherapyProgram x : therapyPrograms){
            therapyProgramDTOS.add(EntityToDTO.ToTherapyProgramDTO(x));
        }

        return therapyProgramDTOS;
    }

    @Override
    public RegisterDTO checkIsStillLeftSessions(String therapy, String patientId) {

        TherapyProgram therapyProgram = therapyProgramDAO.searchByName(therapy);

        if(therapyProgram==null){
            return null;
        }

        Register register = registerDAO.checkIsStillLeftSessions(therapyProgram.getTherapyId(),patientId);

        if(register==null){
            return null;
        }
        System.out.println(register.getRegistrationId());
        return EntityToDTO.ToRegisterDTO(register);

    }

    @Override
    public TherapyProgramDTO getSelectedTherapyDetails(String therapy) {

        TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(therapy);

        if(therapyProgram==null){
            return null;
        }

        return EntityToDTO.ToTherapyProgramDTO(therapyProgram);
    }

    @Override
    public List<String> getAvailableTherapistList(String therapy, String date) {

        TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(therapy);
        String[] str = date.split(" ");
        List<String> therapistIds = queryDAO.getAvailableTherapistIds(therapyProgram.getTherapyId(),str[0]);
        return therapistIds;
    }

    @Override
    public List<String> checkAvailableTimeSlotListForSelectedTherapist(String date, String therapistId) {

        System.out.println("date: "+date);
        System.out.println("therapist id: "+therapistId);
        List<String> times = new ArrayList<>(Arrays.asList("09.00AM", "10.00AM", "11.00AM", "01.00PM", "02.00PM", "03.00PM", "04.00PM", "05.00PM", "06.00PM", "07.00PM"));

        List<TherapySession> therapySessions = sessionDAO.checkAvailableTimeSlotListForSelectedTherapist(date,therapistId);
        List<String> availableTimeList = new ArrayList<>();

L1:     for (int i = 0; i < times.size(); i++) {
            String x = times.get(i);

L2:         for (int j = 0; j < therapySessions.size(); j++) {
                String y = therapySessions.get(j).getTime();

                if(x.equals(y)){
                    continue L1;
                }
            }

            availableTimeList.add(x);
        }

        System.out.println(availableTimeList);
        return availableTimeList;
    }


    @Override
    public boolean addAndPay(SessionDTO sessionDTO) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{

           Patient patient = patientDAO.search(sessionDTO.getPatient().getPatientId());
           if(patient==null){
               return false;
           }

           TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(sessionDTO.getTherapyProgram().getName());
           if(therapyProgram==null){
                return false;
            }

            Therapist therapist = therapistDAO.search(sessionDTO.getTherapist().getTherapistId());
            if(therapist==null){
                return false;
            }

            String paymentId = paymentDAO.generateNewId();
            Payment payment = new Payment(paymentId,new Date(),therapyProgram.getFeeForASingleSession(), PaymentMethod.SESSION_PAYMENT, PaymentStatus.COMPLETE);
            boolean isPaymentAdded = paymentDAO.add(payment,session);

            if(!isPaymentAdded){
                transaction.rollback();
                return false;
            }

            TherapySession therapySession = new TherapySession(sessionDTO.getSessionId(),sessionDTO.getDate(),sessionDTO.getTime(),sessionDTO.getStatus(),patient,therapyProgram,therapist,payment);
            boolean isAddedSession = sessionDAO.AddSessionByCompletingPayment(therapySession,session);

            if(!isAddedSession){
                transaction.rollback();
                return false;
            }

            Register register = new Register();
            register.setTherapy(therapyProgram);
            register.setPatient(patient);

            Register registrationDetails = registerDAO.searchByTherapyAndPatient(register);
            if(registrationDetails==null){
                return false;
            }

            boolean isUpdatedSessionTaken = registerDAO.incrementSessionTaken(registrationDetails,session);
            if(!isUpdatedSessionTaken){
                transaction.rollback();
                return false;
            }

            String date1 = sessionDTO.getDate();
            String[] dateArr = date1.split(" ");
            String date2 = dateArr[0];

            Schedule schedule = new Schedule();
            schedule.setTherapist(therapist);
            schedule.setDate(date2);

            Schedule schedule1 = scheduleDAO.searchScheduleByDateAndTherapist(schedule);
            if(schedule1==null){
                return false;
            }

            boolean isUpdatedSessionCountBooked = scheduleDAO.updateBookedSessionCount(schedule1,session);
            if(!isUpdatedSessionCountBooked){
                transaction.rollback();
                return false;
            }

            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }


    public boolean addAndPayLater(SessionDTO sessionDTO){

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{

            Patient patient = patientDAO.search(sessionDTO.getPatient().getPatientId());
            if(patient==null){
                return false;
            }

            TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(sessionDTO.getTherapyProgram().getName());
            if(therapyProgram==null){
                return false;
            }

            Therapist therapist = therapistDAO.search(sessionDTO.getTherapist().getTherapistId());
            if(therapist==null){
                return false;
            }

            TherapySession therapySession = new TherapySession(sessionDTO.getSessionId(),sessionDTO.getDate(),sessionDTO.getTime(),sessionDTO.getStatus(),patient,therapyProgram,therapist,null);
            boolean isAddedSession = sessionDAO.AddSessionByInCompletingPayment(therapySession,session);

            if(!isAddedSession){
                transaction.rollback();
                return false;
            }

            Register register = new Register();
            register.setTherapy(therapyProgram);
            register.setPatient(patient);

            Register registrationDetails = registerDAO.searchByTherapyAndPatient(register);
            if(registrationDetails==null){
                return false;
            }

            boolean isUpdatedSessionTaken = registerDAO.incrementSessionTaken(registrationDetails,session);
            if(!isUpdatedSessionTaken){
                transaction.rollback();
                return false;
            }

            String date1 = sessionDTO.getDate();
            String[] dateArr = date1.split(" ");
            String date2 = dateArr[0];

            Schedule schedule = new Schedule();
            schedule.setTherapist(therapist);
            schedule.setDate(date2);

            Schedule schedule1 = scheduleDAO.searchScheduleByDateAndTherapist(schedule);
            if(schedule1==null){
                return false;
            }

            boolean isUpdatedSessionCountBooked = scheduleDAO.updateBookedSessionCount(schedule1,session);
            if(!isUpdatedSessionCountBooked){
                transaction.rollback();
                return false;
            }

            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean delete(SessionDTO sessionDTO) {

        return sessionDAO.delete(DTOToEntity.ToSessionEntity(sessionDTO));
    }

    @Override
    public boolean reschedule(SessionDTO sessionDTO, SessionTM selectedItemToUpdate) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            TherapySession therapySession = sessionDAO.search(selectedItemToUpdate.getSessionId());
            if(therapySession==null){
                return false;
            }

            Therapist therapist = therapistDAO.search(sessionDTO.getTherapist().getTherapistId());
            if(therapist==null){
                return false;
            }

            therapySession.setTherapist(therapist);
            therapySession.setDate(sessionDTO.getDate());
            therapySession.setTime(sessionDTO.getTime());
            therapySession.setStatus("Rescheduled");

            boolean isUpdated = sessionDAO.reschedule(therapySession,session);
            if(!isUpdated){
                transaction.rollback();
                return true;
            }

            String beforeDate = selectedItemToUpdate.getDate();
            String afterDate = sessionDTO.getDate();

            String beforeTherapist = selectedItemToUpdate.getTherapistId();
            String afterTherapist = sessionDTO.getTherapist().getTherapistId();

            String beforeTime = selectedItemToUpdate.getTime();
            String afterTime = sessionDTO.getTime();

            if(beforeDate.equals(afterDate) && beforeTherapist.equals(afterTherapist)){
                transaction.commit();
                return true;
            }
            else {
                String[] dateArr = beforeDate.split(" ");
                String date = dateArr[0];

                Schedule schedule = new Schedule();
                Therapist therapist1 = therapistDAO.search(beforeTherapist);
                schedule.setTherapist(therapist1);
                schedule.setDate(date);
                Schedule schedule1 = scheduleDAO.searchScheduleByDateAndTherapist(schedule);

                if(schedule1==null){
                    transaction.rollback();
                    return false;
                }

                boolean isReduce = scheduleDAO.reduceBookedSessionCount(schedule1,session);
                if(!isReduce){
                    transaction.rollback();
                    return false;
                }

                String[] dateArr2 = afterDate.split(" ");
                String date2 = dateArr2[0];

                Schedule schedule2 = new Schedule();
                Therapist therapist2 = therapistDAO.search(afterTherapist);
                schedule2.setTherapist(therapist2);
                schedule2.setDate(date2);
                Schedule schedule3 = scheduleDAO.searchScheduleByDateAndTherapist(schedule2);

                if(schedule3==null){
                    transaction.rollback();
                    return false;
                }

                boolean isUpdatedCount = scheduleDAO.updateBookedSessionCount(schedule3,session);
                if(!isUpdatedCount){
                    transaction.rollback();
                    return false;
                }

                transaction.commit();
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean completeSession(SessionDTO sessionDTO) {

        return sessionDAO.completeSession(DTOToEntity.ToSessionEntity(sessionDTO));
    }

    @Override
    public boolean cancelSession(SessionDTO sessionDTO) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Patient patient = patientDAO.search(sessionDTO.getPatient().getPatientId());
            if(patient==null){
                return false;
            }

            TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(sessionDTO.getTherapyProgram().getName());
            if(therapyProgram==null){
                return false;
            }

            Therapist therapist = therapistDAO.search(sessionDTO.getTherapist().getTherapistId());
            if(therapist==null){
                return false;
            }

            boolean isCanceledSession = sessionDAO.cancelSession(DTOToEntity.ToSessionEntity(sessionDTO),session);

            if(!isCanceledSession){
                transaction.rollback();
                return false;
            }

            Register register = new Register();
            register.setTherapy(therapyProgram);
            register.setPatient(patient);

            Register registrationDetails = registerDAO.searchByTherapyAndPatient(register);
            if(registrationDetails==null){
                return false;
            }

            boolean isUpdatedSessionTaken = registerDAO.decrementSessionTaken(registrationDetails,session);
            if(!isUpdatedSessionTaken){
                transaction.rollback();
                return false;
            }

            String date1 = sessionDTO.getDate();
            String[] dateArr = date1.split(" ");
            String date2 = dateArr[0];

            Schedule schedule = new Schedule();
            schedule.setTherapist(therapist);
            schedule.setDate(date2);

            Schedule schedule1 = scheduleDAO.searchScheduleByDateAndTherapist(schedule);
            if(schedule1==null){
                return false;
            }

            boolean isUpdatedSessionCountBooked = scheduleDAO.reduceBookedSessionCount(schedule1,session);
            if(!isUpdatedSessionCountBooked){
                transaction.rollback();
                return false;
            }

            transaction.commit();
            return true;


        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }
}

















