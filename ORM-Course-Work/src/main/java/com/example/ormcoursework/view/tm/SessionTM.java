package com.example.ormcoursework.view.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SessionTM {

    private String sessionId;
    private String date;
    private String time;
    private String status;
    private String patientId;
    private String therapy;
    private String therapistId;
    private String paymentId;
}
