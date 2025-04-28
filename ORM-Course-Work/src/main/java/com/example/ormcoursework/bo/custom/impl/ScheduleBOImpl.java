package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.ScheduleBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.ScheduleDAO;
import com.example.ormcoursework.dao.custom.TherapistDAO;
import com.example.ormcoursework.dto.ScheduleDTO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.entity.Schedule;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class ScheduleBOImpl implements ScheduleBO {

    private final ScheduleDAO scheduleDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SCHEDULE);
    private final TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public String generateNewId() {

        return scheduleDAO.generateNewId();
    }

    @Override
    public boolean delete(ScheduleDTO scheduleDTO) {

        Therapist therapist = therapistDAO.search(scheduleDTO.getTherapist().getTherapistId());

        if(therapist==null){
            return false;
        }

        scheduleDTO.setTherapist(therapist);
        return scheduleDAO.delete(DTOToEntity.ToScheduleEntity(scheduleDTO));
    }

    @Override
    public List<ScheduleDTO> getAll() {

        List<Schedule> schedules = scheduleDAO.getAll();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        for(Schedule x : schedules){
            scheduleDTOS.add(EntityToDTO.ToScheduleDTO(x));
        }

        return scheduleDTOS;
    }


    @Override
    public boolean add(ScheduleDTO scheduleDTO) {

        Therapist therapist = therapistDAO.search(scheduleDTO.getTherapist().getTherapistId());

        if(therapist==null){
            return false;
        }

        scheduleDTO.setTherapist(therapist);
        return scheduleDAO.add(DTOToEntity.ToScheduleEntity(scheduleDTO));
    }


    @Override
    public boolean update(ScheduleDTO scheduleDTO) {

        Therapist therapist = therapistDAO.search(scheduleDTO.getTherapist().getTherapistId());

        if(therapist==null){
            return false;
        }

        scheduleDTO.setTherapist(therapist);
        return scheduleDAO.update(DTOToEntity.ToScheduleEntity(scheduleDTO));
    }


    @Override
    public boolean isExist(ScheduleDTO scheduleDTO) {

        Therapist therapist = therapistDAO.search(scheduleDTO.getTherapist().getTherapistId());

        if(therapist==null){
            return false;
        }

        scheduleDTO.setTherapist(therapist);
        return scheduleDAO.isExist(DTOToEntity.ToScheduleEntity(scheduleDTO));
    }


    @Override
    public List<TherapistDTO> getAllTherapist() {

        List<Therapist> therapists = therapistDAO.getAll();
        List<TherapistDTO> therapistDTOS = new ArrayList<>();

        for(Therapist x : therapists){
            therapistDTOS.add(EntityToDTO.ToTherapistDTO(x));
        }

        return therapistDTOS;
    }

    @Override
    public boolean changeAvailability(ScheduleDTO scheduleDTO) {

        return scheduleDAO.chageAvailability(DTOToEntity.ToScheduleEntity(scheduleDTO));
    }
}















