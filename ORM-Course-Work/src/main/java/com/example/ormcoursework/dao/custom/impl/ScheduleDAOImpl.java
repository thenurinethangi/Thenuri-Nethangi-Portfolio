package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.ScheduleDAO;
import com.example.ormcoursework.entity.Schedule;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOImpl implements ScheduleDAO {

    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Schedule schedule = session.createQuery("from Schedule order by scheduleNo desc", Schedule.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(schedule==null){
                return "SD-0001";
            }

            String[] str = schedule.getScheduleNo().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("SD-%04d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "SD-0001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean delete(Schedule schedule) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Schedule sh = session.get(Schedule.class,schedule.getScheduleNo());

            if(sh==null){
                return false;
            }

            session.remove(sh);
            transaction.commit();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
    }


    @Override
    public List<Schedule> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            List<Schedule> schedules = session.createQuery("from Schedule ", Schedule.class).list();
            return schedules;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean add(Schedule schedule) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.persist(schedule);
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
    public boolean update(Schedule schedule) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Schedule sh = session.get(Schedule.class,schedule.getScheduleNo());

            if(sh==null){
                return false;
            }

            sh.setTherapist(schedule.getTherapist());
            sh.setDate(schedule.getDate());
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
    public boolean isExist(Schedule schedule) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Schedule> query = session.createQuery("FROM Schedule WHERE therapist = :therapistId AND date = :date", Schedule.class);
            query.setParameter("therapistId", schedule.getTherapist());
            query.setParameter("date", schedule.getDate());

            List<Schedule> schedules = query.list();

            if(schedules.isEmpty()){
                return false;
            }
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
    public boolean chageAvailability(Schedule schedule) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Schedule sh = session.get(Schedule.class,schedule.getScheduleNo());

            if(sh==null){
                return false;
            }

            if(sh.getAvailable().equals("Available")){
                sh.setAvailable("Unavailable");
            }
            else if(sh.getAvailable().equals("Unavailable")){
                sh.setAvailable("Available");
            }

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
    public Schedule searchScheduleByDateAndTherapist(Schedule schedule) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Query<Schedule> query = session.createQuery("from Schedule where therapist = :t and date = :d ",Schedule.class);
            query.setParameter("t",schedule.getTherapist());
            query.setParameter("d",schedule.getDate());

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
    public boolean updateBookedSessionCount(Schedule schedule1, Session session) {

        try{
            Schedule schedule = session.get(Schedule.class,schedule1.getScheduleNo());

            if(schedule==null){
                return false;
            }

            schedule.setBookedSessionCount(schedule.getBookedSessionCount()+1);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean reduceBookedSessionCount(Schedule schedule1, Session session) {

        try{
            Schedule schedule = session.get(Schedule.class,schedule1.getScheduleNo());

            if(schedule==null){
                return false;
            }

            schedule.setBookedSessionCount(schedule.getBookedSessionCount()-1);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}











