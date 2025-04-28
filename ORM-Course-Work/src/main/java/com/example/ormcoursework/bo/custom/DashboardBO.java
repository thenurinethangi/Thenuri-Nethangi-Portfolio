package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.UserDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardBO extends SuperBO {
    
    int getTotalPatientCount();

    int getTotalTherapyPrograms();

    int getTotalTherapist();

    UserDTO getCurrentUserData(String userName);

    List<Integer> getThisMonthSessions();

    BigDecimal getTotalIncome();

    List<TherapistDTO> getAvailableDoctors();

    int getTodaySessionCount();
}
