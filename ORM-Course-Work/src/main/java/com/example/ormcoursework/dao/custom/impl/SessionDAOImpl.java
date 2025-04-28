package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.SessionDAO;
import com.example.ormcoursework.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SessionDAOImpl implements SessionDAO {

    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            TherapySession therapySession = session.createQuery("from TherapySession order by sessionId desc", TherapySession.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(therapySession==null){
                return "S-0000001";
            }

            String[] str = therapySession.getSessionId().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("S-%07d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "S-0000001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<TherapySession> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            List<TherapySession> therapySessions = session.createQuery("from TherapySession", TherapySession.class).list();
            return therapySessions;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<TherapySession> checkAvailableTimeSlotListForSelectedTherapist(String date, String therapistId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<TherapySession> therapistProgramAssignments = session.createNativeQuery("select * from sessions where therapistId = :t and Date = :d and status != 'Canceled'",TherapySession.class);
            therapistProgramAssignments.setParameter("t",therapistId);
            therapistProgramAssignments.setParameter("d",date);

            return therapistProgramAssignments.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean AddSessionByCompletingPayment(TherapySession therapySession, Session session) {

        try{
            session.persist(therapySession);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean AddSessionByInCompletingPayment(TherapySession therapySession, Session session) {

        try{
            session.persist(therapySession);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(TherapySession therapySession) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            TherapySession therapySession1 = session.get(TherapySession.class,therapySession.getSessionId());

            if(therapySession1==null){
                return false;
            }

            session.remove(therapySession1);
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
    public TherapySession search(String sessionId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{

            return session.get(TherapySession.class,sessionId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean reschedule(TherapySession therapySession, Session session) {

        try{
            session.merge(therapySession);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean completeSession(TherapySession therapySession) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            TherapySession therapySession1 = session.get(TherapySession.class,therapySession.getSessionId());

            if(therapySession1==null){
                return false;
            }

            therapySession1.setStatus("Completed");
            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean cancelSession(TherapySession therapySession, Session session) {

        try{
            TherapySession therapySession1 = session.get(TherapySession.class,therapySession.getSessionId());

            if(therapySession1==null){
                return false;
            }

            therapySession1.setStatus("Canceled");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<TherapySession> getPendingSessions() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<TherapySession> therapySessionQuery = session.createQuery("from TherapySession where payment = null",TherapySession.class);
            return therapySessionQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean completePayment(Payment payment, String pendingPaymentId, Session session) {

        try{
            TherapySession therapySession = session.get(TherapySession.class,pendingPaymentId);
            therapySession.setPayment(payment);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TherapySession> getAllTodaySessions() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d", Locale.ENGLISH);
            String formattedDate = today.format(formatter);
            System.out.println(formattedDate);

            Query<TherapySession> therapySessionQuery = session.createQuery("from TherapySession where status != 'Canceled' and date = :d",TherapySession.class);
            therapySessionQuery.setParameter("d",formattedDate);

            return therapySessionQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }
}



















