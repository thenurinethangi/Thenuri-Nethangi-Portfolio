package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.QueryDAO;
import com.example.ormcoursework.entity.Register;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public BigDecimal getUpFrontFee(String selectedId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<BigDecimal> query = session.createNativeQuery(
                    "SELECT t.upfrontPayment FROM therapyprogram t " +
                            "JOIN register r ON t.therapyId = r.therapy " +
                            "WHERE r.registrationId = :regId", BigDecimal.class
            );
            query.setParameter("regId", selectedId);
            return query.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<Therapist> getAvailableTherapistList(String dayName) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
           Query<Therapist> query = session.createNativeQuery("select t.therapistId,t.isActive,t.name,t.phoneNo,t.specialization from therapist t join schedule s on t.therapistId = s.therapistId where s.date = :d and s.available='Available'",Therapist.class);
           query.setParameter("d",dayName);
           return  query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<TherapyProgram> getEnrolledTherapyProgramListOfSelectedPatient(String patientId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<TherapyProgram> therapyProgramQuery = session.createNativeQuery("select t.therapyId,t.duration,t.fee,t.feeForASingleSession,t.name,t.upfrontPayment,t.isActive from therapyprogram t join register r on t.therapyId=r.therapy where r.patientId = :p and r.status='Active'", TherapyProgram.class);
            therapyProgramQuery.setParameter("p",patientId);
            return therapyProgramQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<String> getAvailableTherapistIds(String therapyId, String date) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<String> therapyProgramQuery = session.createNativeQuery("select t.therapistId from therapist t join therapistprogramassignment th on t.therapistId = th.therapist_id join schedule s on s.therapistId = th.therapist_id where th.therapyProgram_id = :p and s.available = 'Available' and s.date = :d", String.class);
            therapyProgramQuery.setParameter("p",therapyId);
            therapyProgramQuery.setParameter("d",date);
            return therapyProgramQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public BigDecimal getSessionFee(String selectedId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<BigDecimal> query = session.createNativeQuery(
                    "SELECT t.feeForASingleSession FROM therapyprogram t " +
                            "JOIN  sessions r ON t.therapyId = r.therapyProgramId " +
                            "WHERE r.SessionId = :sessionId", BigDecimal.class
            );
            query.setParameter("sessionId", selectedId);
            return query.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
    }

}

















