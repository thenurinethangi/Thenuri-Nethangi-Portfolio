package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.TherapistProgramAssignmentDAO;
import com.example.ormcoursework.entity.TherapistProgramAssignment;
import com.example.ormcoursework.entity.TherapistProgramAssignmentFK;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TherapistProgramAssignmentDAOImpl implements TherapistProgramAssignmentDAO {

    @Override
    public List<TherapistProgramAssignment> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            return session.createQuery("from TherapistProgramAssignment",TherapistProgramAssignment.class).list();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<TherapistProgramAssignment> getSelectedTherapistAssignedProgramList(String therapistId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<TherapistProgramAssignment> therapistProgramAssignmentQuery = session.createNativeQuery("select * from therapistprogramassignment where therapist_id=:t",TherapistProgramAssignment.class);
            therapistProgramAssignmentQuery.setParameter("t",therapistId);
            return therapistProgramAssignmentQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean add(TherapistProgramAssignment therapistProgramAssignment) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.merge(therapistProgramAssignment);
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
    public boolean delete(TherapistProgramAssignmentFK therapistProgramAssignmentFK) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            TherapistProgramAssignment therapistProgramAssignment = session.get(TherapistProgramAssignment.class,therapistProgramAssignmentFK);

            if(therapistProgramAssignment==null){
                return false;
            }

            session.remove(therapistProgramAssignment);
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
    public boolean changeStatus(TherapistProgramAssignmentFK therapistProgramAssignmentFK,TherapistProgramAssignment therapistProgramAssignment) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            TherapistProgramAssignment t = session.get(TherapistProgramAssignment.class,therapistProgramAssignmentFK);

            if(t==null){
                return false;
            }

            t.setAssignmentStatus(therapistProgramAssignment.getAssignmentStatus());
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
}












