package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.PatientDTO;

import java.util.List;

public interface PatientBO extends SuperBO {

    String generateNewId();

    boolean delete(PatientDTO patientDTO);

    List<PatientDTO> getAll();

    boolean add(PatientDTO patientDTO);

    boolean update(PatientDTO patientDTO);
}
