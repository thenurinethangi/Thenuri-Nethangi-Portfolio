package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.PaymentDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentBO extends SuperBO {

    String generateNewId();

    List<PaymentDTO> getAll();

    List<String> getPendingRegistrationIds();

    List<String> getPendingSessionIds();

    BigDecimal getUpFrontFee(String selectedId);

    boolean addLatePayment(PaymentDTO paymentDTO, String pendingPaymentId);

    BigDecimal getSessionFee(String selectedId);
}
