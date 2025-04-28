package com.example.ormcoursework.view.tm;


import java.util.Date;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RegisterTM {

    private String registerId;
    private String patientId;
    private String therapy;
    private Date startDate;
    private Date endDate;
    private String paymentId;
    private int sessionsTaken;
    private String status;

}
