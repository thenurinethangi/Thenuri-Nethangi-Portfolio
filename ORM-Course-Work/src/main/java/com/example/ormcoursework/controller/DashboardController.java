package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.DashboardBO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.util.LogInData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Label doctor1Label;

    @FXML
    private Label doctor2Label;

    @FXML
    private Label doctor4Label;

    @FXML
    private Label doctor5Label;

    @FXML
    private Label incomeLabel;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private Label roleLabel;

    @FXML
    private Label roleLabel2;

    @FXML
    private TextField searchBar;

    @FXML
    private Label specialization1Label;

    @FXML
    private Label specialization2Label;

    @FXML
    private Label specialization3Label;

    @FXML
    private Label specialization4Label;

    @FXML
    private Label totalPatientLabel;

    @FXML
    private Label totalSessionLabel;

    @FXML
    private Label totalTherapistLabel;

    @FXML
    private Label totalTherapyLabel;

    @FXML
    private Label userNameLabel;


    private final DashboardBO dashboardBO = BOFactory.getInstance().getBO(BOFactory.BOType.DASHBOARD);

    @FXML
    void userNameOnMouseClick(MouseEvent event) {

        loadUserProfile();
    }

    @FXML
    void userProfileOnMouseClick(MouseEvent event) {

       loadUserProfile();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchBar.setFocusTraversable(false);

        UserDTO userDTO = dashboardBO.getCurrentUserData(LogInData.getUserName());
        userNameLabel.setText(userDTO.getUserName());
        roleLabel.setText(userDTO.getRole());

        int totalPatient = dashboardBO.getTotalPatientCount();
        totalPatientLabel.setText(String.valueOf(totalPatient));

        int therapyProgramCount = dashboardBO.getTotalTherapyPrograms();
        totalTherapyLabel.setText(String.valueOf(therapyProgramCount));

        int therapistCount = dashboardBO.getTotalTherapist();
        totalTherapistLabel.setText(String.valueOf(therapistCount));

        int todaySessionCount = dashboardBO.getTodaySessionCount();
        totalSessionLabel.setText(String.valueOf(todaySessionCount));

        setChart();
        setIncome();
        setTodayAvailableDoctors();

    }


    private void setIncome(){

        BigDecimal total = dashboardBO.getTotalIncome();
        incomeLabel.setText("Rs."+total);
    }


    private void setTodayAvailableDoctors(){

        List<TherapistDTO> therapistDTOList = dashboardBO.getAvailableDoctors();

        if(therapistDTOList==null){
            return;
        }

        if(therapistDTOList.size()>0){
            doctor1Label.setText("Dr. "+therapistDTOList.get(0).getName());
            specialization1Label.setText(therapistDTOList.get(0).getSpecialization());
        }
        if(therapistDTOList.size()>1){
            doctor2Label.setText("Dr. "+therapistDTOList.get(1).getName());
            specialization2Label.setText(therapistDTOList.get(1).getSpecialization());
        }
        if(therapistDTOList.size()>2){
            doctor4Label.setText("Dr. "+therapistDTOList.get(2).getName());
            specialization3Label.setText(therapistDTOList.get(2).getSpecialization());
        }
        if(therapistDTOList.size()>3){
            doctor5Label.setText("Dr. "+therapistDTOList.get(3).getName());
            specialization4Label.setText(therapistDTOList.get(3).getSpecialization());
        }

    }


    private void loadUserProfile(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfile.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void searchTxtOnMouseEntered(MouseEvent event) {

        searchBar.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                String page = searchBar.getText();
                page = page.toLowerCase();
                System.out.println(page);


                if(page.equals("patient") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/patient-page.fxml");
                }

                else if(page.equals("register") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/register-page.fxml");
                }

                else if(page.equals("session") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/patient-page.fxml");
                }

                else if(page.equals("therapy record") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/register-page.fxml");
                }

                else if(page.equals("payment") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/payment-page.fxml");
                }

                else if(page.equals("patient history") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/register-page.fxml");
                }

                else if(page.equals("report") && roleLabel.getText().equals("Receptionist")){
                    loanPage("/view/register-page.fxml");
                }

                else if(page.equals("therapy program") && roleLabel.getText().equals("Admin")){
                    loanPage("/view/therapy-program-page.fxml");
                }

                else if(page.equals("therapist") && roleLabel.getText().equals("Admin")){
                    loanPage("/view/therapist-page.fxml");
                }

                else if((page.equals("schedule") || page.equals("therapist schedule")) && roleLabel.getText().equals("Admin")){
                    loanPage("/view/schedule-page.fxml");
                }

                else if(page.equals("report") && roleLabel.getText().equals("Admin")){
                    loanPage("/view/register-page.fxml");
                }

                searchBar.clear();
            }
        });

    }


    private void loanPage(String url){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void setChart(){

        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();

        xAxis.setLowerBound(1);
        xAxis.setUpperBound(31);
        xAxis.setTickUnit(5);
        xAxis.setMinorTickCount(4);
        xAxis.setMinorTickVisible(true);

//        xAxis.setLabel("Day of the Month");

        XYChart.Series series = new XYChart.Series();

        LocalDate today = LocalDate.now();
        Month month = today.getMonth();
        series.setName(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

        List<Integer> countList = dashboardBO.getThisMonthSessions();//

        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
        series.getData().add(new XYChart.Data(13, 23));
        series.getData().add(new XYChart.Data(14, 14));
        series.getData().add(new XYChart.Data(15, 15));
        series.getData().add(new XYChart.Data(16, 24));
        series.getData().add(new XYChart.Data(17, 34));
        series.getData().add(new XYChart.Data(18, 36));
        series.getData().add(new XYChart.Data(19, 22));
        series.getData().add(new XYChart.Data(20, 45));
        series.getData().add(new XYChart.Data(21, 43));
        series.getData().add(new XYChart.Data(22, 17));
        series.getData().add(new XYChart.Data(23, 29));
        series.getData().add(new XYChart.Data(24, 25));
        series.getData().add(new XYChart.Data(25, 23));
        series.getData().add(new XYChart.Data(26, 14));
        series.getData().add(new XYChart.Data(27, 15));
        series.getData().add(new XYChart.Data(28, 24));
        series.getData().add(new XYChart.Data(29, 34));
        series.getData().add(new XYChart.Data(30, 36));
        series.getData().add(new XYChart.Data(31, 22));


        lineChart.getData().add(series);

    }
}





