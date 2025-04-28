package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.TherapistProgramAssignmentDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;

import java.util.List;

public interface TherapistProgramAssignmentBO extends SuperBO {

    List<TherapistProgramAssignmentDTO> getAll();

    List<TherapistDTO> getAllTherapists();

    TherapistDTO getSelectedTherapistDetails(String therapistId);

    TherapyProgramDTO getSelectedTherapyDetails(String therapy);

    List<TherapyProgramDTO> getTherapyProgramList(String therapistId);

    boolean add(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO);

    boolean delete(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO);

    boolean changeStatus(TherapistProgramAssignmentDTO therapistProgramAssignmentDTO);
}
