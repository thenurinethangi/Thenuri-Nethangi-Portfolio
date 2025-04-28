package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.Schedule;
import org.hibernate.Session;

import java.util.List;

public interface ScheduleDAO extends CrudDAO {

    String generateNewId();

    boolean delete(Schedule schedule);

    List<Schedule> getAll();

    boolean add(Schedule schedule);

    boolean update(Schedule schedule);

    boolean isExist(Schedule schedule);

    boolean chageAvailability(Schedule schedule);

    Schedule searchScheduleByDateAndTherapist(Schedule schedule);

    boolean updateBookedSessionCount(Schedule schedule1, Session session);

    boolean reduceBookedSessionCount(Schedule schedule1, Session session);
}
