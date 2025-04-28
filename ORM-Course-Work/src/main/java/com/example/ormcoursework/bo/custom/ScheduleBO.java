package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.ScheduleDTO;
import com.example.ormcoursework.dto.TherapistDTO;

import java.util.List;

public interface ScheduleBO extends SuperBO {

    String generateNewId();

    boolean delete(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> getAll();

    boolean add(ScheduleDTO scheduleDTO);

    boolean update(ScheduleDTO scheduleDTO);

    boolean isExist(ScheduleDTO scheduleDTO);

    List<TherapistDTO> getAllTherapist();

    boolean changeAvailability(ScheduleDTO scheduleDTO);
}
