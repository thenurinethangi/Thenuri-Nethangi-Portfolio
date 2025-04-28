package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.PatientBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.PatientDAO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;

import java.util.ArrayList;
import java.util.List;

public class PatientBOImpl implements PatientBO {

    private final PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public String generateNewId() {

        return patientDAO.generateNewId();
    }

    @Override
    public boolean delete(PatientDTO patientDTO) {

        return patientDAO.delete(DTOToEntity.ToPatientEntity(patientDTO));
    }

    @Override
    public List<PatientDTO> getAll() {

        List<Patient> patients = patientDAO.getAll();
        List<PatientDTO> patientDTOS = new ArrayList<>();

        for(Patient x : patients){
            patientDTOS.add(EntityToDTO.ToPatientDTO(x));
        }

        return patientDTOS;
    }

    @Override
    public boolean add(PatientDTO patientDTO) {

        return patientDAO.add(DTOToEntity.ToPatientEntity(patientDTO));
    }

    @Override
    public boolean update(PatientDTO patientDTO) {

        return patientDAO.update(DTOToEntity.ToPatientEntity(patientDTO));
    }
}








