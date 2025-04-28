package com.example.ormcoursework.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.example.ormcoursework.util.PaymentMethod;
import com.example.ormcoursework.util.PaymentStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PaymentDTO {

    private String paymentId;
    private Date date;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
}
