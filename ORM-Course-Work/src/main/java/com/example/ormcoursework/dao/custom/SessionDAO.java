package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.TherapistProgramAssignment;
import com.example.ormcoursework.entity.TherapySession;
import org.hibernate.Session;

import java.util.List;

public interface SessionDAO extends CrudDAO {

    String generateNewId();

    List<TherapySession> getAll();

    List<TherapySession> checkAvailableTimeSlotListForSelectedTherapist(String date, String therapistId);

    boolean AddSessionByCompletingPayment(TherapySession therapySession, Session session);

    boolean AddSessionByInCompletingPayment(TherapySession therapySession, Session session);

    boolean delete(TherapySession therapySession);

    TherapySession search(String sessionId);

    boolean reschedule(TherapySession therapySession, Session session);

    boolean completeSession(TherapySession therapySession);

    boolean cancelSession(TherapySession therapySession, Session session);

    List<TherapySession> getPendingSessions();

    boolean completePayment(Payment payment, String pendingPaymentId, Session session);

    List<TherapySession> getAllTodaySessions();
}
