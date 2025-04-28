package com.example.ormcoursework.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TherapistDTO {

    private String therapistId;
    private String name;
    private String phoneNo;
    private String specialization;
    private boolean isActive;

    public TherapistDTO(String therapistId) {
        this.therapistId = therapistId;
    }
}
