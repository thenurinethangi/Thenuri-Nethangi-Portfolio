package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.TherapistBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.TherapistDAO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;
import javafx.collections.FXCollections;

import java.util.List;

public class TherapistBOImpl implements TherapistBO {

    private final TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public String generateNewId() {

        return therapistDAO.generateNewId();
    }

    @Override
    public boolean delete(TherapistDTO therapistDTO) {

        therapistDTO.setActive(false);
        return therapistDAO.delete(DTOToEntity.ToTherapistEntity(therapistDTO));
    }

    @Override
    public List<TherapistDTO> getAll() {

        List<Therapist> therapists = therapistDAO.getAll();
        List<TherapistDTO> therapistDTOS = FXCollections.observableArrayList();

        for(Therapist x : therapists){
            therapistDTOS.add(EntityToDTO.ToTherapistDTO(x));
        }

        return therapistDTOS;
    }

    @Override
    public boolean add(TherapistDTO therapistDTO) {

        return therapistDAO.add(DTOToEntity.ToTherapistEntity(therapistDTO));
    }

    @Override
    public boolean update(TherapistDTO therapistDTO) {

        return therapistDAO.update(DTOToEntity.ToTherapistEntity(therapistDTO));
    }
}












