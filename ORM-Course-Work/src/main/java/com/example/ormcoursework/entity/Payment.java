package com.example.ormcoursework.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.example.ormcoursework.util.PaymentMethod;
import com.example.ormcoursework.util.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class Payment {

    @Id
    private String paymentId;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    public Payment(String paymentId) {
        this.paymentId = paymentId;
    }

    public Payment(BigDecimal amount) {
        this.amount = amount;
    }
}





