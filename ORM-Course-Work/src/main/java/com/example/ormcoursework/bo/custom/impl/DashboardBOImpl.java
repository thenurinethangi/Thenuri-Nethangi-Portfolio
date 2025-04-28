package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.DashboardBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.*;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.entity.*;
import com.example.ormcoursework.util.EntityToDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardBOImpl implements DashboardBO {

    private final PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    private final TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    private final TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    private final UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    private final QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);
    private final SessionDAO sessionDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SESSION);

    @Override
    public int getTotalPatientCount() {

        List<Patient> patients = patientDAO.getAll();
        return patients.size();
    }

    @Override
    public int getTotalTherapyPrograms() {

        List<TherapyProgram> therapyPrograms = therapyProgramDAO.getAll();
        return therapyPrograms.size();
    }

    @Override
    public int getTotalTherapist() {

        List<Therapist> therapists = therapistDAO.getAll();
        return therapists.size();
    }

    @Override
    public UserDTO getCurrentUserData(String userName) {

        return EntityToDTO.ToUserDTO(userDAO.search(userName));
    }

    @Override
    public List<Integer> getThisMonthSessions() {

        return new ArrayList<>();
    }

    @Override
    public BigDecimal getTotalIncome() {

        List<Payment> payments = paymentDAO.getAll();

        BigDecimal total = new BigDecimal("0.0");

        for(Payment x : payments){
            total = total.add(x.getAmount());
        }

        return total;
    }

    @Override
    public List<TherapistDTO> getAvailableDoctors() {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");

        String dayName = today.format(formatter);

        List<Therapist> therapists = queryDAO.getAvailableTherapistList(dayName);
        List<TherapistDTO> therapistDTOList = new ArrayList<>();

        for(Therapist x : therapists){
            therapistDTOList.add(EntityToDTO.ToTherapistDTO(x));
        }
        return therapistDTOList;
    }

    @Override
    public int getTodaySessionCount() {

        List<TherapySession> therapySessions = sessionDAO.getAllTodaySessions();
        return therapySessions.size();
    }
}










