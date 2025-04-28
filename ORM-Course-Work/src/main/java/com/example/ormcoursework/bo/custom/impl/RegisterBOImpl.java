package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.RegisterBO;
import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.PatientDAO;
import com.example.ormcoursework.dao.custom.PaymentDAO;
import com.example.ormcoursework.dao.custom.RegisterDAO;
import com.example.ormcoursework.dao.custom.TherapyProgramDAO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Register;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;
import com.example.ormcoursework.util.PaymentMethod;
import com.example.ormcoursework.util.PaymentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterBOImpl implements RegisterBO {

    private final RegisterDAO registerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REGISTER);
    private final PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    private final TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);

    @Override
    public String generateNewId() {

        return registerDAO.generateNewId();
    }

    @Override
    public List<RegisterDTO> getAll() {

        List<Register> registers = registerDAO.getAll();
        List<RegisterDTO> registerDTOS = new ArrayList<>();

        for (Register x : registers){
            Date endDate = x.getEndDate();
            LocalDate endLocalDate = ((java.sql.Date) endDate).toLocalDate();
            LocalDate today = LocalDate.now();

            if(endLocalDate.isBefore(today)){
                x.setStatus("Expired");
                boolean isUpdate = registerDAO.update(x);
            }

            if(x.getSessionsTaken()>=x.getTotalSessionsCountCanTake()){
                x.setStatus("Completed");
                boolean isUpdate = registerDAO.update(x);
            }

            registerDTOS.add(EntityToDTO.ToRegisterDTO(x));
        }

        return registerDTOS;
    }

    @Override
    public PatientDTO getPatientById(String id) {

        Patient patient = patientDAO.search(id);

        if(patient==null){
            return null;
        }
        return EntityToDTO.ToPatientDTO(patient);
    }

    @Override
    public PatientDTO getPatientByPhoneNo(String phoneNo) {

        Patient patient = patientDAO.getPatientByPhoneNo(phoneNo);

        if(patient==null){
            return null;
        }
        return EntityToDTO.ToPatientDTO(patient);
    }

    @Override
    public List<TherapyProgramDTO> getAvailableTherapyProgramForSelectedPatient(PatientDTO patientDTO) {

        List<Register> registers = registerDAO.getAvailableTherapyProgramForSelectedPatient(DTOToEntity.ToPatientEntity(patientDTO));
        List<TherapyProgram> therapyPrograms = therapyProgramDAO.getAll();

        List<TherapyProgramDTO> therapyProgramDTOS = new ArrayList<>();

        if(registers==null || registers.isEmpty()){

            for (TherapyProgram x : therapyPrograms){
                therapyProgramDTOS.add(EntityToDTO.ToTherapyProgramDTO(x));
            }

            return therapyProgramDTOS;
        }


 L1:    for(TherapyProgram x : therapyPrograms){

 L2:       for(Register y : registers){

                TherapyProgram therapyProgram = y.getTherapy();

                if(x.getTherapyId().equals(therapyProgram.getTherapyId())){
                    continue L1;
                }
            }
            therapyProgramDTOS.add(EntityToDTO.ToTherapyProgramDTO(x));
        }

        return therapyProgramDTOS;
    }

    @Override
    public boolean delete(RegisterDTO registerDTO) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            boolean isDeleted = registerDAO.delete(session,DTOToEntity.ToRegisterEntity(registerDTO));

            if(!isDeleted){
                transaction.rollback();
                return false;
            }

            Patient patient = patientDAO.search(registerDTO.getPatient().getPatientId());

            if (patient == null) {
                return false;
            }

            boolean isUpdatePatient =  patientDAO.updateEnrolledTherapyProgramCount(patient,session,"minus");

            if(!isUpdatePatient){
                transaction.rollback();
                return false;
            }

            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean addAndPayLater(RegisterDTO registerDTO) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(registerDTO.getTherapy().getName());

            if (therapyProgram == null) {
                return false;
            }

            String duration = therapyProgram.getDuration();
            Date endDate = calculateEndDate(duration);
            int totalSessionCanTake = calculateTotalSessionCountCanTake(duration);
            int sessionTaken = 0;

            Patient patient = patientDAO.search(registerDTO.getPatient().getPatientId());

            if (patient == null) {
                return false;
            }

            Register register = new Register(registerDTO.getRegistrationId(), registerDTO.getStartDate(), endDate, totalSessionCanTake, sessionTaken, registerDTO.getStatus(), therapyProgram, patient, null);
            boolean isSave = registerDAO.addPaymentUncompletedRegistration(register,session);

            if(!isSave){
                transaction.rollback();
                return false;
            }

            boolean isUpdatePatient =  patientDAO.updateEnrolledTherapyProgramCount(patient,session,"plus");

            if(!isUpdatePatient){
                transaction.rollback();
                return false;
            }

            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }

    }


    @Override
    public boolean addAndPay(RegisterDTO registerDTO) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{

            TherapyProgram therapyProgram = therapyProgramDAO.getTherapyProgramByName(registerDTO.getTherapy().getName());

            if(therapyProgram==null){
                return false;
            }

            String duration = therapyProgram.getDuration();
            Date endDate = calculateEndDate(duration);
            int totalSessionCanTake = calculateTotalSessionCountCanTake(duration);
            int sessionTaken = 0;

            Patient patient = patientDAO.search(registerDTO.getPatient().getPatientId());

            if(patient==null){
                return false;
            }

            String newPaymentId = paymentDAO.generateNewId();
            Payment payment = new Payment(newPaymentId,new Date(),registerDTO.getPayment().getAmount(), PaymentMethod.REGISTRATION_PAYMENT, PaymentStatus.COMPLETE);

            boolean isSavePayment = paymentDAO.add(payment,session);

            if(!isSavePayment){
                transaction.rollback();
                return false;
            }

            Register register = new Register(registerDTO.getRegistrationId(),registerDTO.getStartDate(),endDate,totalSessionCanTake,sessionTaken,registerDTO.getStatus(),therapyProgram,patient,payment);
            boolean isSave =  registerDAO.addPaymentCompletedRegistration(register,session);

            if(!isSave){
                transaction.rollback();
                return false;
            }


            boolean isUpdatePatient =  patientDAO.updateEnrolledTherapyProgramCount(patient,session,"plus");

            if(!isUpdatePatient){
                transaction.rollback();
                return false;
            }

            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }


    private int calculateTotalSessionCountCanTake(String duration){

        String[] dur = duration.split(" ");

        int count = 0;

        if(dur[1].equals("week") || dur[1].equals("weeks")){

            count = Integer.parseInt(dur[0]);
        }
        else if(dur[1].equals("month") || dur[1].equals("months")){

            int x = Integer.parseInt(dur[0]);
            int y = x*4;

            count = y;
        }
        else if(dur[1].equals("year") || dur[1].equals("years")){

            int x = Integer.parseInt(dur[0]);
            int y = x*12*4;

            count = y;
        }

        return count;
    }


    private Date calculateEndDate(String duration){

        LocalDate today = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        String[] dur = duration.split(" ");

        if(dur[1].equals("week") || dur[1].equals("weeks")){

            endDate = today.plus(Long.parseLong(dur[0]), ChronoUnit.WEEKS);
        }
        else if(dur[1].equals("month") || dur[1].equals("months")){

            endDate = today.plus(Long.parseLong(dur[0]), ChronoUnit.MONTHS);
        }
        else if(dur[1].equals("year") || dur[1].equals("years")){

            endDate = today.plus(Long.parseLong(dur[0]), ChronoUnit.YEARS);
        }

        Date d = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return d;
    }
}











