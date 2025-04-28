package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;

import java.util.List;

public interface RegisterBO extends SuperBO {

    String generateNewId();

    List<RegisterDTO> getAll();

    PatientDTO getPatientById(String id);

    PatientDTO getPatientByPhoneNo(String phoneNo);

    List<TherapyProgramDTO> getAvailableTherapyProgramForSelectedPatient(PatientDTO patientDTO);

    boolean delete(RegisterDTO registerDTO);

    boolean addAndPayLater(RegisterDTO registerDTO);

    boolean addAndPay(RegisterDTO registerDTO);
}
