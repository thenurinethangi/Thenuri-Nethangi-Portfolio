package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.Therapist;

import java.util.List;

public interface TherapistDAO extends CrudDAO {

    String generateNewId();

    boolean delete(Therapist therapist);

    List<Therapist> getAll();

    boolean add(Therapist therapist);

    boolean update(Therapist therapist);

    Therapist search(String therapistId);

}
