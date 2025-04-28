package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Register;
import com.example.ormcoursework.entity.TherapyProgram;
import org.hibernate.Session;

import java.util.List;

public interface RegisterDAO extends CrudDAO {

    String generateNewId();

    List<Register> getAll();

    List<Register> getAvailableTherapyProgramForSelectedPatient(Patient patient);

    boolean delete(Register register);

    boolean delete(Session session,Register register);

    boolean add(Register register);

    boolean addPaymentCompletedRegistration(Register register, Session session);

    boolean completePayment(Payment payment, String pendingPaymentId,Session session);

    Register checkIsStillLeftSessions(String therapyId, String patientId);

    boolean addPaymentUncompletedRegistration(Register register, Session session);

    boolean update(Register x);

    Register searchByTherapyAndPatient(Register register);

    boolean incrementSessionTaken(Register registrationDetails, Session session);

    boolean decrementSessionTaken(Register registrationDetails, Session session);

    List<Register> getRegistrationDetailsByPatientId();
}
