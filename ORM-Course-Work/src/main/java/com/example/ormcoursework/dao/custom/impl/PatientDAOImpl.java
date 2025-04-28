package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.PatientDAO;
import com.example.ormcoursework.entity.Patient;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;


public class PatientDAOImpl implements PatientDAO {

    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Patient patient = session.createQuery("from Patient order by patientId desc", Patient.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(patient==null){
                return "P-000001";
            }

            String[] str = patient.getPatientId().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("P-%06d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "P-000001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean delete(Patient patient) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Patient p = session.get(Patient.class,patient.getPatientId());

            if(p==null){
                return false;
            }

            p.setActive(false);
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
    public List<Patient> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            List<Patient> patients = session.createQuery("from Patient where isActive = true ",Patient.class).list();
            return patients;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean add(Patient patient) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.persist(patient);
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
    public boolean update(Patient patient) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Patient p = session.get(Patient.class,patient.getPatientId());

            if(p==null){
                return false;
            }

            p.setName(patient.getName());
            p.setAge(patient.getAge());
            p.setGender(patient.getGender());
            p.setPhoneNo(patient.getPhoneNo());

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
    public Patient search(String id) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Patient patient = session.get(Patient.class,id);
            return patient;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return null;
    }


    @Override
    public Patient getPatientByPhoneNo(String phoneNo) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Patient> query = session.createQuery("from Patient where phoneNo = :p", Patient.class);
            query.setParameter("p", phoneNo);

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
    public boolean updateEnrolledTherapyProgramCount(Patient patient, Session session,String action) {

        try{
            Patient patient1 = session.get(Patient.class,patient.getPatientId());

            if(patient1==null){
                return false;
            }

            if(action.equals("plus")){
                patient1.setEnrolledTherapyProgramCount(patient1.getEnrolledTherapyProgramCount()+1);
            }
            else {
                patient1.setEnrolledTherapyProgramCount(patient1.getEnrolledTherapyProgramCount()-1);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}












