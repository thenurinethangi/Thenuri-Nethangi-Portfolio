package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.PaymentBO;
import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.PaymentDAO;
import com.example.ormcoursework.dao.custom.QueryDAO;
import com.example.ormcoursework.dao.custom.RegisterDAO;
import com.example.ormcoursework.dao.custom.SessionDAO;
import com.example.ormcoursework.dto.PaymentDTO;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Register;
import com.example.ormcoursework.entity.TherapySession;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;
import com.example.ormcoursework.util.PaymentMethod;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    private final RegisterDAO registerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REGISTER);
    private final QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);
    private final SessionDAO sessionDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SESSION);

    @Override
    public String generateNewId() {

        return paymentDAO.generateNewId();
    }

    @Override
    public List<PaymentDTO> getAll() {

        List<Payment> payments = paymentDAO.getAll();
        List<PaymentDTO> paymentDTOS = new ArrayList<>();

        for(Payment x : payments){
            paymentDTOS.add(EntityToDTO.ToPaymentDTO(x));
        }

        return paymentDTOS;
    }

    @Override
    public List<String> getPendingRegistrationIds() {

        List<Register> registers = registerDAO.getAll();
        List<String> ids = new ArrayList<>();

        for(Register x : registers){
            if(x.getPayment()==null){
                ids.add(x.getRegistrationId());
            }
        }

        return ids;
    }

    @Override
    public List<String> getPendingSessionIds() {

        List<TherapySession> therapySessions = sessionDAO.getPendingSessions();
        List<String> idList = new ArrayList<>();

        for (TherapySession x : therapySessions){
            idList.add(x.getSessionId());
        }

        return idList;
    }

    @Override
    public BigDecimal getUpFrontFee(String selectedId) {

        BigDecimal amount = queryDAO.getUpFrontFee(selectedId);
        return amount;
    }

    @Override
    public boolean addLatePayment(PaymentDTO paymentDTO, String pendingPaymentId) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            boolean isSave = paymentDAO.add(DTOToEntity.ToPaymentEntity(paymentDTO),session);

            if(!isSave){
                transaction.rollback();
                return false;
            }

            if(paymentDTO.getPaymentMethod()== PaymentMethod.REGISTRATION_PAYMENT){

                boolean isUpdate = registerDAO.completePayment(DTOToEntity.ToPaymentEntity(paymentDTO),pendingPaymentId,session);

                if(!isUpdate){
                    transaction.rollback();
                    return false;
                }

                transaction.commit();
                return true;
            }
            else{

                boolean isUpdate = sessionDAO.completePayment(DTOToEntity.ToPaymentEntity(paymentDTO),pendingPaymentId,session);

                if(!isUpdate){
                    transaction.rollback();
                    return false;
                }

                transaction.commit();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }

    @Override
    public BigDecimal getSessionFee(String selectedId) {

        return queryDAO.getSessionFee(selectedId);
    }
}


