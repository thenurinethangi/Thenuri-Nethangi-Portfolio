package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.PaymentDAO;
import com.example.ormcoursework.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public String generateNewId() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            Payment payment = session.createQuery("from Payment order by paymentId desc", Payment.class).setMaxResults(1).uniqueResultOptional().orElse(null);

            if(payment==null){
                return "INV-0000001";
            }

            String[] str = payment.getPaymentId().split("-");
            int idNo = Integer.parseInt(str[1]);
            idNo++;
            String newId = String.format("INV-%07d", idNo);
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return "INV-0000001";
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean add(Payment payment,Session session) {

        try{
            session.persist(payment);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Payment> getAll() {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{
            List<Payment> payments = session.createQuery("from Payment",Payment.class).list();
            return payments;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            session.close();
        }
    }
}










