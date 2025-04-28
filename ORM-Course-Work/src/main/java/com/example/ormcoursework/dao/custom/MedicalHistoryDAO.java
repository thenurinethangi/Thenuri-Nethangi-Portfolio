package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.MedicalHistory;

import java.util.List;

public interface MedicalHistoryDAO extends CrudDAO {

    String generateNewId();

    List<MedicalHistory> getAll();
}
