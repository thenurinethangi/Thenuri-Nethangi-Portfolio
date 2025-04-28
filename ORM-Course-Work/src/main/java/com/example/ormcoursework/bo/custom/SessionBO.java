package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.dto.SessionDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.view.tm.SessionTM;

import java.util.List;

public interface SessionBO extends SuperBO {

    String generateNewId();

    List<SessionDTO> getAll();

    PatientDTO getPatientById(String str);

    PatientDTO getPatientByPhoneNo(String str);

    List<TherapyProgramDTO> getEnrolledTherapyProgramList(String patientId);

    RegisterDTO checkIsStillLeftSessions(String therapy, String patientId);

    TherapyProgramDTO getSelectedTherapyDetails(String therapy);

    List<String> getAvailableTherapistList(String therapy, String date);

    List<String> checkAvailableTimeSlotListForSelectedTherapist(String date, String therapistId);

    boolean addAndPay(SessionDTO sessionDTO);

    boolean addAndPayLater(SessionDTO sessionDTO);

    boolean delete(SessionDTO sessionDTO);

    boolean reschedule(SessionDTO sessionDTO, SessionTM selectedItemToUpdate);

    boolean completeSession(SessionDTO sessionDTO);

    boolean cancelSession(SessionDTO sessionDTO);
}
