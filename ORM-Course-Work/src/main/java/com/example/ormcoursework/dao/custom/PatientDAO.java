package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.Patient;
import org.hibernate.Session;

import java.util.List;

public interface PatientDAO extends CrudDAO {

    String generateNewId();

    boolean delete(Patient patient);

    List<Patient> getAll();

    boolean add(Patient patient);

    boolean update(Patient patient);

    Patient search(String id);

    Patient getPatientByPhoneNo(String phoneNo);

    boolean updateEnrolledTherapyProgramCount(Patient patient, Session session,String action);
}
