package com.example.ormcoursework.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TherapyProgramDTO {

    private String therapyId;
    private String name;
    private String duration;
    private BigDecimal fee;
    private BigDecimal upfrontPayment;
    private BigDecimal feeForASingleSession;
    private boolean isActive = true;


    public TherapyProgramDTO(String therapyId, String name, String duration, BigDecimal fee) {
        this.therapyId = therapyId;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
    }

    public TherapyProgramDTO(String therapyId, String name, String duration, BigDecimal fee, BigDecimal upfrontPayment, BigDecimal feeForASingleSession) {
        this.therapyId = therapyId;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
        this.upfrontPayment = upfrontPayment;
        this.feeForASingleSession = feeForASingleSession;
    }
}
