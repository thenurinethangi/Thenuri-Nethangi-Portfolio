package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.entity.Therapist;

import java.util.List;

public interface TherapistBO extends SuperBO {

    String generateNewId();

    boolean delete(TherapistDTO therapistDTO);

    List<TherapistDTO> getAll();

    boolean add(TherapistDTO therapistDTO);

    boolean update(TherapistDTO therapistDTO);
}
