package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.MedicalHistoryBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.MedicalHistoryDAO;
import com.example.ormcoursework.dao.custom.PatientDAO;
import com.example.ormcoursework.dto.MedicalHistoryDTO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.entity.MedicalHistory;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.util.EntityToDTO;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistoryBOImpl implements MedicalHistoryBO {

    private final MedicalHistoryDAO medicalHistoryDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.MEDICAL_HISTORY);
    private final PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public String generateNewId() {

        return medicalHistoryDAO.generateNewId();
    }

    @Override
    public List<MedicalHistoryDTO> getAll() {

        List<MedicalHistory> medicalHistories = medicalHistoryDAO.getAll();
        List<MedicalHistoryDTO> medicalHistoryDTOS = new ArrayList<>();

        for (MedicalHistory x : medicalHistories){
            medicalHistoryDTOS.add(EntityToDTO.ToMedicalHistoryDTO(x));
        }

        return medicalHistoryDTOS;
    }

    @Override
    public List<PatientDTO> getAllPatient() {

        List<Patient> patients = patientDAO.getAll();
        List<PatientDTO> patientDTOS = new ArrayList<>();

        for (Patient x : patients){
            patientDTOS.add(EntityToDTO.ToPatientDTO(x));
        }

        return patientDTOS;
    }
}








