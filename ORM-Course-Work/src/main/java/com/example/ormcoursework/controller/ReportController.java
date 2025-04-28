package com.example.ormcoursework.controller;

import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.RegisterDAO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.entity.Register;
import com.example.ormcoursework.view.tm.RegisterTM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.*;

public class ReportController {


    @FXML
    private Button btn;

    @FXML
    private TextField id;

    RegisterDAO registerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REGISTER);


    @FXML
    void btnOnAction(ActionEvent event) {

        List<Register> register = registerDAO.getRegistrationDetailsByPatientId();
        List<RegisterTM> registerTMS = new ArrayList<>();

        for (Register x : register){
            RegisterTM registerTM = new RegisterTM();
            registerTM.setRegisterId(x.getRegistrationId());
            registerTM.setTherapy(x.getTherapy().getTherapyId());
            registerTM.setPaymentId(x.getPayment().getPaymentId());
            registerTM.setStatus(x.getStatus());
            registerTM.setEndDate(x.getEndDate());
            registerTM.setStartDate(x.getStartDate());
            registerTM.setSessionsTaken(x.getSessionsTaken());
            registerTM.setPatientId(x.getPatient().getPatientId());

            registerTMS.add(registerTM);
        }
        generateReport(registerTMS,"P-000002");

    }

    public static void generateReport(List<RegisterTM> registerList, String patientId) {
        try {
            // Load the .jrxml file
            InputStream reportStream = ReportController.class.getResourceAsStream("/assets/report/therapy_history_report.jrxml");

            if (reportStream == null) {
                System.err.println("Could not load JRXML file. Check the path!");
                return;
            }

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("patientId", patientId);
            parameters.put("reportDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            // Fill the report
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registerList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, "therapy_history_report.pdf");

            System.out.println("Report generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
