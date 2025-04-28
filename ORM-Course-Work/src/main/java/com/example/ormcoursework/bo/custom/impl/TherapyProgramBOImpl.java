package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.TherapyProgramBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.TherapyProgramDAO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TherapyProgramBOImpl implements TherapyProgramBO {

    private final TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public boolean add(TherapyProgramDTO therapyProgramDTO) {

        BigDecimal upFrontFee = calculateUpFrontPayment(therapyProgramDTO.getFee());
        BigDecimal remainFee = therapyProgramDTO.getFee().subtract(upFrontFee);

        BigDecimal chargeForPerSession = calculateChargeForPerSession(remainFee,therapyProgramDTO.getDuration());

        TherapyProgramDTO updatedTherapyDTO = new TherapyProgramDTO(therapyProgramDTO.getTherapyId(),therapyProgramDTO.getName(),therapyProgramDTO.getDuration(),therapyProgramDTO.getFee(),upFrontFee,chargeForPerSession);

        return therapyProgramDAO.add(DTOToEntity.ToTherapyProgramEntity(updatedTherapyDTO));

    }


    private BigDecimal calculateUpFrontPayment(BigDecimal fee){

        return fee.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
    }


    private BigDecimal calculateChargeForPerSession(BigDecimal remainFee,String duration){

        BigDecimal chargeForPerSession = new BigDecimal("0.0");

        String[] dur = duration.split(" ");

        int scale = 2;
        int roundingMode = BigDecimal.ROUND_HALF_UP;

        if(dur[1].equals("week") || dur[1].equals("weeks")){

            chargeForPerSession = remainFee.divide(new BigDecimal(dur[0]), scale, roundingMode);
        }
        else if(dur[1].equals("month") || dur[1].equals("months")){

            int x = Integer.parseInt(dur[0]);
            int y = x*4;

            chargeForPerSession = remainFee.divide(new BigDecimal(y), scale, roundingMode);
        }
        else if(dur[1].equals("year") || dur[1].equals("years")){

            int x = Integer.parseInt(dur[0]);
            int y = x*12*4;

            chargeForPerSession = remainFee.divide(new BigDecimal(y), scale, roundingMode);
        }

        return chargeForPerSession;
    }


    @Override
    public String generateNewId() {

        return therapyProgramDAO.generateNewId();
    }


    @Override
    public List<TherapyProgramDTO> getAll() {

        List<TherapyProgram> therapyPrograms = therapyProgramDAO.getAll();
        List<TherapyProgramDTO> therapyProgramDTOS = new ArrayList<>();

        for(TherapyProgram x : therapyPrograms){

            therapyProgramDTOS.add(EntityToDTO.ToTherapyProgramDTO(x));
        }

        return therapyProgramDTOS;
    }


    @Override
    public boolean delete(TherapyProgramDTO therapyProgramDTO) {

        therapyProgramDTO.setActive(false);
        return therapyProgramDAO.delete(DTOToEntity.ToTherapyProgramEntity(therapyProgramDTO));
    }


    @Override
    public boolean update(TherapyProgramDTO therapyProgramDTO) {

        BigDecimal upFrontPayment = calculateUpFrontPayment(therapyProgramDTO.getFee());
        BigDecimal remain = therapyProgramDTO.getFee().subtract(upFrontPayment);
        BigDecimal chargeForPerSession = calculateChargeForPerSession(remain,therapyProgramDTO.getDuration());

        therapyProgramDTO.setUpfrontPayment(upFrontPayment);
        therapyProgramDTO.setFeeForASingleSession(chargeForPerSession);

        return therapyProgramDAO.update(DTOToEntity.ToTherapyProgramEntity(therapyProgramDTO));
    }
}









