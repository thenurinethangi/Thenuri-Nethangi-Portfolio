package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.TherapistDAO;
import com.example.ormcoursework.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TherapistDAOImpl implements TherapistDAO {


    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Therapist therapist = session.createQuery("from Therapist order by therapistId desc", Therapist.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(therapist==null){
                return "T-001";
            }

            String[] str = therapist.getTherapistId().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("T-%03d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "T-001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean delete(Therapist therapist) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Therapist t = session.get(Therapist.class,therapist.getTherapistId());

            if(t==null){
                return false;
            }

            t.setActive(false);
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
    public List<Therapist> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            List<Therapist> therapists = session.createQuery("from Therapist where isActive = true",Therapist.class).list();
            return therapists;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean add(Therapist therapist) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.persist(therapist);
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
    public boolean update(Therapist therapist) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Therapist t = session.get(Therapist.class,therapist.getTherapistId());

            if(t==null){
                return false;
            }

            session.merge(therapist);
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
    public Therapist search(String therapistId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Therapist therapist = session.get(Therapist.class,therapistId);

            if(therapist==null){
                return null;
            }

            return therapist;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
    }

}











