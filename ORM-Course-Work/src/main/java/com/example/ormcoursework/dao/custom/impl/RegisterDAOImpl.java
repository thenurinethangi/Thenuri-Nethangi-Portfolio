package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.RegisterDAO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Register;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RegisterDAOImpl implements RegisterDAO {

    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Register register = session.createQuery("from Register order by registrationId desc", Register.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(register==null){
                return "R-000001";
            }

            String[] str = register.getRegistrationId().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("R-%06d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "R-000001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<Register> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
           List<Register> registers = session.createQuery("from Register",Register.class).list();
           return registers;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<Register> getAvailableTherapyProgramForSelectedPatient(Patient patient) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Register> query = session.createQuery("from Register where patient = :p and status = 'Active'", Register.class);
            query.setParameter("p", patient);
            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean delete(Register register) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Register r = session.get(Register.class,register.getRegistrationId());

            if(r==null){
                return false;
            }

            session.remove(r);
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
    public boolean delete(Session session,Register register) {

        try{
            Register r = session.get(Register.class,register.getRegistrationId());

            if(r==null){
                return false;
            }

            session.remove(r);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean add(Register register) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.persist(register);
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
    public boolean addPaymentCompletedRegistration(Register register, Session session) {

        try{
            session.persist(register);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean completePayment(Payment payment, String pendingPaymentId,Session session) {

        try{
            Register register = session.get(Register.class,pendingPaymentId);
            register.setPayment(payment);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Register checkIsStillLeftSessions(String therapyId, String patientId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Register> query = session.createNativeQuery("select * from register where patientId=:p and therapy=:t and status='Active'",Register.class);
            query.setParameter("p",patientId);
            query.setParameter("t",therapyId);

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
    public boolean addPaymentUncompletedRegistration(Register register, Session session) {

        try{
            session.persist(register);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Register x) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Register register = session.get(Register.class,x.getRegistrationId());

            if(register==null){
                return false;
            }

            register.setStatus(x.getStatus());
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
    public Register searchByTherapyAndPatient(Register register) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Register> query = session.createQuery("from Register where patient = :p and therapy = :t and status = 'Active'",Register.class);
            query.setParameter("p",register.getPatient());
            query.setParameter("t",register.getTherapy());

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
    public boolean incrementSessionTaken(Register registrationDetails, Session session) {

        try{
            Register register = session.get(Register.class,registrationDetails.getRegistrationId());

            if(register==null){
                return false;
            }

            register.setSessionsTaken(register.getSessionsTaken()+1);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean decrementSessionTaken(Register registrationDetails, Session session) {

        try{
            Register register = session.get(Register.class,registrationDetails.getRegistrationId());

            if(register==null){
                return false;
            }

            register.setSessionsTaken(register.getSessionsTaken()-1);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Register> getRegistrationDetailsByPatientId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Register> query = session.createNativeQuery("select * from register where patientId = 'P-000002'",Register.class);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }
}












