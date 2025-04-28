package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.SuperDAO;
import com.example.ormcoursework.entity.Register;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapyProgram;

import java.math.BigDecimal;
import java.util.List;

public interface QueryDAO extends SuperDAO {

    BigDecimal getUpFrontFee(String selectedId);

    List<Therapist> getAvailableTherapistList(String dayName);

    List<TherapyProgram> getEnrolledTherapyProgramListOfSelectedPatient(String patientId);

    List<String> getAvailableTherapistIds(String therapyId, String date);

    BigDecimal getSessionFee(String selectedId);
}
