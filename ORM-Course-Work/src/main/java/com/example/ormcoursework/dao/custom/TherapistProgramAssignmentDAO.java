package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.TherapistProgramAssignment;
import com.example.ormcoursework.entity.TherapistProgramAssignmentFK;

import java.util.List;

public interface TherapistProgramAssignmentDAO extends CrudDAO {

    List<TherapistProgramAssignment> getAll();

    List<TherapistProgramAssignment> getSelectedTherapistAssignedProgramList(String therapistId);

    boolean add(TherapistProgramAssignment therapistProgramAssignment);

    boolean delete(TherapistProgramAssignmentFK therapistProgramAssignmentFK);

    boolean changeStatus(TherapistProgramAssignmentFK therapistProgramAssignmentFK,TherapistProgramAssignment therapistProgramAssignment);
}
