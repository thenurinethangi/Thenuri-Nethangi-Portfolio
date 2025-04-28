package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.TherapyProgram;

import java.util.List;

public interface TherapyProgramDAO extends CrudDAO {

    boolean add(TherapyProgram therapyProgram);

    String generateNewId();

    List<TherapyProgram> getAll();

    boolean delete(TherapyProgram therapyProgram);

    boolean update(TherapyProgram therapyProgram);

    TherapyProgram getTherapyProgramByName(String name);

    TherapyProgram searchByName(String therapy);
}
