package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.TherapistProgramAssignmentBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.TherapistDAO;
import com.example.ormcoursework.dao.custom.TherapistProgramAssignmentDAO;
import com.example.ormcoursework.dao.custom.TherapyProgramDAO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.TherapistProgramAssignmentDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapistProgramAssignment;
import com.example.ormcoursework.entity.TherapistProgramAssignmentFK;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;

import java.util.ArrayList;
import java.util.List;

public class TherapistProgramAssignmentBOImpl implements TherapistProgramAssignmentBO {

    private final TherapistProgramAssignmentDAO therapistProgramAssignmentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST_PROGRAM_ASSIGNMENT);
    private final TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    private final TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public List<TherapistProgramAssignmentDTO> getAll() {

        List<TherapistProgramAssignment> therapistProgramAssignments = therapistProgramAssignmentDAO.getAll();
        List<TherapistProgramAssignmentDTO> therapistProgramAssignmentDTOS = new ArrayList<>();

        for(TherapistProgramAssignment x : therapistProgramAssignments){
            therapistProgramAssignmentDTOS.add(EntityToDTO.ToTherapistProgramAssignmentDTO(x));
        }

        return therapistProgramAssignmentDTOS;
    }


    @Override
    public List<TherapistDTO> getAllTherapists() {

        List<Therapist> therapists = therapistDAO.getAll();
        List<TherapistDTO> therapistDTOS = new ArrayList<>();

        for(Therapist x : therapists){
            therapistDTOS.add(EntityToDTO.ToTherapistDTO(x));
        }

        return therapistDTOS;
    }

    @Override
    public TherapistDTO getSelectedTherapistDetails(String therapistId) {

        Therapist therapist = therapistDAO.search(therapistId);

        if(therapist==null){
            return null;
        }

        return EntityToDTO.ToTherapistDTO(therapist);
    }

    @Override
    public TherapyProgramDTO getSelectedTherapyDetails(String therapy) {

        TherapyProgram therapyProgram = therapyProgramDAO.searchByName(therapy);

        if(therapyProgram==null){
            return null;
        }

        return EntityToDTO.ToTherapyProgramDTO(therapyProgram);
    }

    @Override
    public List<TherapyProgramDTO> getTherapyProgramList(String therapistId) {

        List<TherapistProgramAssignment> therapistProgramAssignments = therapistProgramAssignmentDAO.getSelectedTherapistAssignedProgramList(therapistId);
        List<TherapyProgram> therapyPrograms = therapyProgramDAO.getAll();

        List<TherapyProgramDTO> therapyProgramDTOS = new ArrayList<>();

L1:     for(TherapyProgram x : therapyPrograms){
    L2:     for (int i = 0; i < therapistProgramAssignments.size(); i++) {
                if(x.getTherapyId().equals(therapistProgramAssignments.get(i).getTherapyProgram().getTherapyId())){
                    continue L1;
                }
            }

            therapyProgramDTOS.add(EntityToDTO.ToTherapyProgramDTO(x));
        }

        return therapyProgramDTOS;
    }

    @Override
    public boolean add(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO) {

        Therapist therapist = therapistDAO.search(therapistProgramAssignmentDTO.getTherapist().getTherapistId());
        if(therapist==null){
            System.out.println("therapist null");
            return false;
        }
        System.out.println(therapistProgramAssignmentDTO.getTherapyProgram().getName());
        TherapyProgram therapyProgram = therapyProgramDAO.searchByName(therapistProgramAssignmentDTO.getTherapyProgram().getName());
        if(therapyProgram==null){
            System.out.println("therapy program null");
            return false;
        }

        therapistProgramAssignmentDTO.setTherapist(therapist);
        therapistProgramAssignmentDTO.setTherapyProgram(therapyProgram);
        TherapistProgramAssignmentFK id = new TherapistProgramAssignmentFK();
        id.setTherapist_id(therapist.getTherapistId());
        id.setTherapyProgram_id(therapyProgram.getTherapyId());
        therapistProgramAssignmentDTO.setId(id);

        System.out.println(therapistProgramAssignmentDTO.getTherapyProgram().getTherapyId()+therapistProgramAssignmentDTO.getTherapist().getTherapistId()+therapistProgramAssignmentDTO.getAssignmentStatus());
        return therapistProgramAssignmentDAO.add(DTOToEntity.ToTherapistProgramAssignmentEntity(therapistProgramAssignmentDTO));
    }

    @Override
    public boolean delete(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO) {

        TherapistProgramAssignmentFK therapistProgramAssignmentFK = new TherapistProgramAssignmentFK();
        therapistProgramAssignmentFK.setTherapist_id(therapistProgramAssignmentDTO.getTherapist().getTherapistId());

        TherapyProgram therapyProgram = therapyProgramDAO.searchByName(therapistProgramAssignmentDTO.getTherapyProgram().getName());
        therapistProgramAssignmentFK.setTherapyProgram_id(therapyProgram.getTherapyId());

        return therapistProgramAssignmentDAO.delete(therapistProgramAssignmentFK);
    }

    @Override
    public boolean changeStatus(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO) {

        TherapistProgramAssignmentFK therapistProgramAssignmentFK = new TherapistProgramAssignmentFK();
        therapistProgramAssignmentFK.setTherapist_id(therapistProgramAssignmentDTO.getTherapist().getTherapistId());

        TherapyProgram therapyProgram = therapyProgramDAO.searchByName(therapistProgramAssignmentDTO.getTherapyProgram().getName());
        therapistProgramAssignmentFK.setTherapyProgram_id(therapyProgram.getTherapyId());

        return therapistProgramAssignmentDAO.changeStatus(therapistProgramAssignmentFK,DTOToEntity.ToTherapistProgramAssignmentEntity(therapistProgramAssignmentDTO));
    }
}











