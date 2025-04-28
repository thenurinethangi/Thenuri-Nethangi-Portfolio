package com.example.ormcoursework.dao.custom;

import com.example.ormcoursework.dao.CrudDAO;
import com.example.ormcoursework.entity.Payment;
import org.hibernate.Session;

import java.util.List;

public interface PaymentDAO extends CrudDAO {

    String generateNewId();

    boolean add(Payment payment, Session session);

    List<Payment> getAll();
}
