package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.TherapyProgramDAO;
import com.example.ormcoursework.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {

    @Override
    public boolean add(TherapyProgram therapyProgram) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{

            TherapyProgram tProgram = session.get(TherapyProgram.class,therapyProgram.getTherapyId());

            if(tProgram!=null){
                return false;
            }

            session.persist(therapyProgram);
            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            if(session!=null) {
                session.close();
            }
        }
    }


    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            TherapyProgram therapyProgram = session.createQuery("from TherapyProgram order by therapyId desc", TherapyProgram.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(therapyProgram==null){
                return "P-001";
            }

            String[] str = therapyProgram.getTherapyId().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("P-%03d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "P-001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<TherapyProgram> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapyProgram> therapyPrograms = new ArrayList<>();

        try{

            therapyPrograms = session.createQuery("from TherapyProgram where isActive = true", TherapyProgram.class).list();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }

        return therapyPrograms;
    }


    @Override
    public boolean delete(TherapyProgram therapyProgram) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{

            TherapyProgram tProgram = session.get(TherapyProgram.class,therapyProgram.getTherapyId());

            if(tProgram==null){
                return false;
            }

            tProgram.setActive(false);
            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.commit();
            return false;
        }
        finally {
            if(session!=null) {
                session.close();
            }
        }
    }


    @Override
    public boolean update(TherapyProgram therapyProgram) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            TherapyProgram tProgram = session.get(TherapyProgram.class,therapyProgram.getTherapyId());

            if(tProgram==null){
                return false;
            }

            session.merge(therapyProgram);
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
    public TherapyProgram getTherapyProgramByName(String name) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<TherapyProgram> query = session.createQuery("from TherapyProgram where name = :n and isActive=true ",TherapyProgram.class);
            query.setParameter("n",name);

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
    public TherapyProgram searchByName(String therapy) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<TherapyProgram> query = session.createQuery("from TherapyProgram where name = :n",TherapyProgram.class);
            query.setParameter("n",therapy);
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













