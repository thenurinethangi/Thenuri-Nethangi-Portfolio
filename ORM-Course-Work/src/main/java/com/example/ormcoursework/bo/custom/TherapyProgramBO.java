package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.TherapyProgramDTO;

import java.util.ArrayList;
import java.util.List;

public interface TherapyProgramBO extends SuperBO {

    boolean add(TherapyProgramDTO therapyProgramDTO);

    String generateNewId();

    List<TherapyProgramDTO> getAll();

    boolean delete(TherapyProgramDTO therapyProgramDTO);

    boolean update(TherapyProgramDTO therapyProgramDTO);
}
