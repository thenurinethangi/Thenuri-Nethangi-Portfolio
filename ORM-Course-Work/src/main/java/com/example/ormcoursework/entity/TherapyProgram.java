package com.example.ormcoursework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table
public class TherapyProgram {

    @Id
    @Column(updatable = false)
    private String therapyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal upfrontPayment;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal feeForASingleSession;

    @Column(nullable = false)
    private boolean isActive = true;


    public TherapyProgram(String therapyId) {
        this.therapyId = therapyId;
    }

    public TherapyProgram(String name,BigDecimal upfrontPayment) {

        this.name = name;
        this.upfrontPayment = upfrontPayment;
    }
}









