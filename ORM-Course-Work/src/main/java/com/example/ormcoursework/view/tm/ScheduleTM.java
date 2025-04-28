package com.example.ormcoursework.view.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ScheduleTM {

    private String scheduleNo;
    private String therapistId;
    private String date;
    private int bookedSessionCount;
    private String available;
}
