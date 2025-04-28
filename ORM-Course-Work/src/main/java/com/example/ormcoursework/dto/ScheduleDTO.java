package com.example.ormcoursework.dto;


import com.example.ormcoursework.entity.Therapist;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ScheduleDTO {

    private String scheduleNo;
    private String date;
    private int bookedSessionCount;
    private String available;
    private Therapist therapist;

    public ScheduleDTO(String scheduleNo, String date,Therapist therapist) {
        this.scheduleNo = scheduleNo;
        this.date = date;
        this.therapist = therapist;
    }
}
