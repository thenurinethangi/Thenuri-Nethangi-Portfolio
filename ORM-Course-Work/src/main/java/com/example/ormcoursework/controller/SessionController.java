package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.SessionBO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.dto.SessionDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.util.UserInputValidation;
import com.example.ormcoursework.view.tm.PatientTM;
import com.example.ormcoursework.view.tm.ScheduleTM;
import com.example.ormcoursework.view.tm.SessionTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


public class SessionController implements Initializable {

    @FXML
    private TableColumn<SessionTM, String> actionCol;

    @FXML
    private ComboBox<String> dateCmb;

    @FXML
    private TableColumn<SessionTM, String> dateCol;

    @FXML
    private ComboBox<String> doctorCmb;

    @FXML
    private Label patientDetailsLabel;

    @FXML
    private TableColumn<SessionTM, String> patientIdCol;

    @FXML
    private TextField patientIdField;

    @FXML
    private Button payAndAddBtn;

    @FXML
    private Button payLaterAndAddBtn;

    @FXML
    private TableColumn<SessionTM, String> paymentCol;

    @FXML
    private TextField searchBar;

    @FXML
    private TableColumn<SessionTM, String> sessionIdCol;

    @FXML
    private Label sessionIdLabel;

    @FXML
    private TableColumn<SessionTM, String> statusCol;

    @FXML
    private TableView<SessionTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private TableColumn<SessionTM, String> therapistCol;

    @FXML
    private ComboBox<String> therapyCmb;

    @FXML
    private TableColumn<SessionTM, String> therapyCol;

    @FXML
    private Label therapyProgramDetailsLabel;

    @FXML
    private ComboBox<String> timeCmb;

    @FXML
    private TableColumn<SessionTM, String> timeCol;

    @FXML
    private Button updateBtn;

    private SessionTM selectedItemToUpdate;
    private ObservableList<SessionTM> tableData = FXCollections.observableArrayList();
    private final SessionBO sessionBO = BOFactory.getInstance().getBO(BOFactory.BOType.SESSION);


    @FXML
    void payAndAddBtnOnAction(ActionEvent event) {

        String sessionId = sessionIdLabel.getText();
        String patientId = patientIdField.getText();
        String therapyProgram = therapyCmb.getSelectionModel().getSelectedItem();
        String date = dateCmb.getSelectionModel().getSelectedItem();
        String time = timeCmb.getSelectionModel().getSelectedItem();
        String therapistId = doctorCmb.getSelectionModel().getSelectedItem();

        if(sessionId.isEmpty() || patientId.isEmpty() || therapyProgram==null || date==null || time==null || therapistId==null){

            notification("Please Enter All require Data To Add A New Therapy Session");
            return;
        }

        SessionDTO sessionDTO = new SessionDTO(sessionId,date,time,"Booked",new Patient(patientId),new TherapyProgram(therapyProgram,new BigDecimal("0.0")),new Therapist(therapistId),null);

        boolean isSave = sessionBO.addAndPay(sessionDTO);

        if(isSave){
            notification("Successfully Schedule A New Therapy Session For Patient ID: "+patientId+" For Therapy Program: "+therapyProgram);
        }
        else {
            notification("Failed To Add New Therapy Session For Patient ID: "+patientId+", Please Try Again Later");
        }

        clean();
    }


    @FXML
    void payLaterAndAddBtnOnAction(ActionEvent event) {

        String sessionId = sessionIdLabel.getText();
        String patientId = patientIdField.getText();
        String therapyProgram = therapyCmb.getSelectionModel().getSelectedItem();
        String date = dateCmb.getSelectionModel().getSelectedItem();
        String time = timeCmb.getSelectionModel().getSelectedItem();
        String therapistId = doctorCmb.getSelectionModel().getSelectedItem();

        if(sessionId.isEmpty() || patientId.isEmpty() || therapyProgram==null || date==null || time==null || therapistId==null){

            notification("Please Enter All require Data To Add A New Therapy Session");
            return;
        }

        SessionDTO sessionDTO = new SessionDTO(sessionId,date,time,"Booked",new Patient(patientId),new TherapyProgram(therapyProgram,new BigDecimal("0.0")),new Therapist(therapistId),null);

        boolean isSave = sessionBO.addAndPayLater(sessionDTO);

        if(isSave){
            notification("Successfully Schedule A New Therapy Session For Patient ID: "+patientId+" For Therapy Program: "+therapyProgram);
        }
        else {
            notification("Failed To Add New Therapy Session For Patient ID: "+patientId+", Please Try Again Later");
        }

        clean();

    }


    @FXML
    void refreshOnMouseClick(MouseEvent event) {

        clean();
    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer selectedNo = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(selectedNo==null){
            return;
        }

        ObservableList<SessionTM> patientTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            patientTMS.add(tableData.get(i));
        }

        table.setItems(patientTMS);
    }


    @FXML
    void updateBtnOnAction(ActionEvent event) {

        if(selectedItemToUpdate==null){
            return;
        }

        String sessionId = sessionIdLabel.getText();
        String patientId = patientIdField.getText();
        String therapyProgram = therapyCmb.getSelectionModel().getSelectedItem();
        String date = dateCmb.getSelectionModel().getSelectedItem();
        String time = timeCmb.getSelectionModel().getSelectedItem();
        String therapistId = doctorCmb.getSelectionModel().getSelectedItem();

        if(sessionId.isEmpty() || patientId.isEmpty() || therapyProgram==null || date==null || time==null || therapistId==null){

            notification("Please Enter All require Data To Reschedule Selected Therapy Session");
            return;
        }

        SessionDTO sessionDTO = new SessionDTO(sessionId,date,time,"Rescheduled",new Patient(patientId),new TherapyProgram(therapyProgram,new BigDecimal("0.0")),new Therapist(therapistId),null);

        boolean isSave = sessionBO.reschedule(sessionDTO,selectedItemToUpdate);

        if(isSave){
            notification("Successfully Rescheduled Selected Therapy Session For Patient ID: "+patientId+" For Therapy Program: "+therapyProgram);
        }
        else {
            notification("Failed To Rescheduled Selected Therapy Session For Patient ID: "+patientId+", Please Try Again Later");
        }

        selectedItemToUpdate = null;
        clean();

    }


    @FXML
    void searchPatientOnMouseClick(MouseEvent event) {

        String str = patientIdField.getText();

        if(str.isEmpty()){
            notification("Please Enter The Patient ID Or Phone Number To Schedule A Session For The Patient");
            return;
        }

        PatientDTO patientDTO;

        if(str.length()==8){

            patientDTO = sessionBO.getPatientById(str);

            if(patientDTO==null &&  str.startsWith("P-")){

                notification("This Patient ID Does Not Exist");
                return;
            }
            else if (patientDTO==null &&  !str.startsWith("P-")) {

                notification("Not A Valid Patient ID, Please Enter Valid ID");
                return;
            }

        }
        else if(str.length()==10){

            patientDTO = sessionBO.getPatientByPhoneNo(str);
            boolean bool = UserInputValidation.checkPhoneNoValidation(str);

            if(patientDTO==null && bool){
                notification("No Patient Is Registered With This Phone Number");
                return;
            }
            else if(patientDTO==null && !bool){
                notification("Not A Valid Phone Number!");
                return;
            }
        }

        else{

            notification("Not A Valid Patient Id Or Phone Number, Please Enter Valid Data");
            return;
        }

        String patientDetails = "Name: "+patientDTO.getName()+"   Age: "+ patientDTO.getAge()+"   Gender: "+patientDTO.getGender();
        patientDetailsLabel.setText(patientDetails);

        List<TherapyProgramDTO> therapyProgramDTOS = sessionBO.getEnrolledTherapyProgramList(patientDTO.getPatientId());

        if(therapyProgramDTOS.isEmpty()){
            notification("This Patient Currently Does Not Have Any Active Enrollment In Any Therapy Program.");
            return;
        }

        ObservableList<String> therapyPrograms = FXCollections.observableArrayList();
        for(TherapyProgramDTO x : therapyProgramDTOS){
            therapyPrograms.add(x.getName());
        }
        therapyCmb.setItems(therapyPrograms);

    }


    @FXML
    void therapyProgramCmbOnAction(ActionEvent event) {

        String therapy = therapyCmb.getSelectionModel().getSelectedItem();
        String patientId = patientIdField.getText();

        if(therapy==null || patientId.isEmpty()){
            return;
        }

        RegisterDTO registerDTO = sessionBO.checkIsStillLeftSessions(therapy,patientId);

        if(registerDTO==null){
            notification("Something Went Wrong, Please Try Again Later!");
            return;
        }

        boolean result = registerDTO.getSessionsTaken()>=registerDTO.getTotalSessionsCountCanTake();

        if(result){
            notification("Patient ID: "+patientId+ "Exceeded Session Limit For The Selected Therapy Program, Unable To Book A Session For This Therapy Program.");
            therapyCmb.getSelectionModel().clearSelection();
            return;
        }

        TherapyProgramDTO therapyProgramDTO = sessionBO.getSelectedTherapyDetails(therapy);
        String therapyDetails = "ID: "+therapyProgramDTO.getTherapyId()+"  Name: "+therapyProgramDTO.getName()+"  Session Left: "+(registerDTO.getTotalSessionsCountCanTake()-registerDTO.getSessionsTaken());
        therapyProgramDetailsLabel.setText(therapyDetails);
    }


    @FXML
    void dateCmbOnAction(ActionEvent event) {

        String date = dateCmb.getSelectionModel().getSelectedItem();
        String patientId = patientIdField.getText();
        String therapy = therapyCmb.getSelectionModel().getSelectedItem();

        if(date==null || patientId.isEmpty() || therapy==null){
            return;
        }

        List<String> therapistIds = sessionBO.getAvailableTherapistList(therapy,date);
        ObservableList<String> ids = FXCollections.observableArrayList(therapistIds);
        if(ids.isEmpty()){
            notification("No Therapist Available For Therapy Program: "+therapy+" For Date :"+date+", Please Book A Session For Another Date");
        }
        doctorCmb.setItems(ids);

    }


    @FXML
    void doctorCmbOnAction(ActionEvent event) {

        String patientId = patientIdField.getText();
        String therapy = therapyCmb.getSelectionModel().getSelectedItem();
        String date = dateCmb.getSelectionModel().getSelectedItem();
        String therapistId = doctorCmb.getSelectionModel().getSelectedItem();

        if(date==null || patientId.isEmpty() || therapy==null || therapistId==null){
            return;
        }

        List<String> timeList = sessionBO.checkAvailableTimeSlotListForSelectedTherapist(date,therapistId);
        ObservableList<String> times = FXCollections.observableArrayList(timeList);

        if(times.isEmpty()){
            notification("Therapist ID: "+therapistId+" Is Fully Booked On "+date);
        }
        timeCmb.setItems(times);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewId();
        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setDateCmbValues();
        setTableActionColumn();
        tableSearch();
        clean();
    }


    public void generateNewId(){

        String newId = sessionBO.generateNewId();
        sessionIdLabel.setText(newId);
    }


    public void setTableColumn(){

        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        therapyCol.setCellValueFactory(new PropertyValueFactory<>("therapy"));
        therapistCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<SessionTM, String>, TableCell<SessionTM, String>> cellFoctory = (TableColumn<SessionTM, String> param) -> {

            final TableCell<SessionTM, String> cell = new TableCell<SessionTM, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        SessionTM sessionTM = getTableView().getItems().get(getIndex());

                        Image image1 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/eye.png");
                        Image image2 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/trash.png");
                        Image image3 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/edit.png");

                        Image crossImg = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/close.png");
                        Image rightImg = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/check (1).png");

                        ImageView complete = new ImageView();
                        complete.setFitHeight(17);
                        complete.setFitWidth(17);

                        ImageView cancel = new ImageView();
                        cancel.setFitHeight(17);
                        cancel.setFitWidth(17);

                        boolean isAvailable = "Booked".equals(sessionTM.getStatus()) || "Rescheduled".equals(sessionTM.getStatus());

                        if(isAvailable){
                            complete.setImage(rightImg);
                            cancel.setImage(crossImg);
                        }

                        ImageView delete = new ImageView();
                        delete.setImage(image2);
                        delete.setFitHeight(14);
                        delete.setFitWidth(14);

                        ImageView update = new ImageView();
                        update.setImage(image3);
                        update.setFitHeight(17);
                        update.setFitWidth(17);

                        cancel.setStyle(" -fx-cursor: hand ;");
                        complete.setStyle(" -fx-cursor: hand ;");
                        delete.setStyle(" -fx-cursor: hand ;");
                        update.setStyle(" -fx-cursor: hand ;");


                        cancel.setOnMouseClicked((MouseEvent event) -> {

                            SessionTM selectedItem = table.getSelectionModel().getSelectedItem();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Cancel The Session ID: "+selectedItem.getSessionId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                boolean isCanceled = sessionBO.cancelSession(TMToDTO.ToSessionDTO(selectedItem));

                                if(isCanceled){
                                    notification("Successfully Canceled The Session ID: "+selectedItem.getSessionId());

                                }
                                else {
                                    notification("Failed To Canceled The Session ID: "+selectedItem.getSessionId()+", Please Try Again Later!");
                                }

                            }

                            clean();

                        });

                        complete.setOnMouseClicked((MouseEvent event) -> {

                            SessionTM selectedItem = table.getSelectionModel().getSelectedItem();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Complete The Session ID: "+selectedItem.getSessionId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                boolean isComplete = sessionBO.completeSession(TMToDTO.ToSessionDTO(selectedItem));

                                if(isComplete && selectedItem.getPaymentId().equals("Pending")){
                                    notification("Successfully Completed The Session ID: "+selectedItem.getSessionId()+", But Payment Not completed. Please finalize It For This Session.");

                                }
                                else if(isComplete && !selectedItem.getPaymentId().equals("Pending")){
                                    notification("Successfully Completed The Session ID: "+selectedItem.getSessionId());
                                }
                                else {
                                    notification("Failed To Complete The Session ID: "+selectedItem.getSessionId()+", Please Try Again Later!");
                                }

                            }

                            clean();

                        });

                        delete.setOnMouseClicked((MouseEvent event) -> {

                            SessionTM selectedItem = table.getSelectionModel().getSelectedItem();

                            if(!selectedItem.getStatus().equals("Canceled")){
                                notification("Can't Delete Booked/Rescheduled/Completed Therapy Sessions");
                                return;
                            }

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete The Session ID: "+selectedItem.getSessionId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                SessionDTO sessionDTO = TMToDTO.ToSessionDTO(selectedItem);
                                boolean isDelete = sessionBO.delete(sessionDTO);

                                if(isDelete){
                                    notification("Successfully Deleted The Session ID: "+selectedItem.getSessionId());
                                }
                                else{
                                    notification("Failed Delete The Session ID: "+selectedItem.getSessionId());
                                }
                            }

                            clean();

                        });


                        update.setOnMouseClicked((MouseEvent event) -> {

                            selectedItemToUpdate = table.getSelectionModel().getSelectedItem();

                            if(selectedItemToUpdate.getStatus().equals("Completed") || selectedItemToUpdate.getStatus().equals("Canceled")){
                                notification("Can't Reschedule Completed Or Canceled Sessions");
                                selectedItemToUpdate = null;
                                return;
                            }

                            sessionIdLabel.setText(selectedItemToUpdate.getSessionId());

                            patientIdField.setText(selectedItemToUpdate.getPatientId());
                            patientIdField.setDisable(true);
                            PatientDTO patientDTO = sessionBO.getPatientById(selectedItemToUpdate.getPatientId());
                            patientDetailsLabel.setText("Name: "+patientDTO.getName()+" Age: "+patientDTO.getAge()+" Gender: "+patientDTO.getGender());

                            therapyCmb.getSelectionModel().select(selectedItemToUpdate.getTherapy());
                            therapyCmb.setDisable(true);
                            TherapyProgramDTO therapyProgramDTO = sessionBO.getSelectedTherapyDetails(selectedItemToUpdate.getTherapy());
                            therapyProgramDetailsLabel.setText("ID: "+therapyProgramDTO.getTherapyId()+" Name: "+therapyProgramDTO.getName());

                            dateCmb.getSelectionModel().select(selectedItemToUpdate.getDate());
                            doctorCmb.getSelectionModel().select(selectedItemToUpdate.getTherapistId());
                            timeCmb.getSelectionModel().select(selectedItemToUpdate.getTime());

                            updateBtn.setDisable(false);
                            payLaterAndAddBtn.setDisable(true);
                            payAndAddBtn.setDisable(true);

                        });

                        HBox manageBtn = new HBox(complete,cancel,delete,update);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(4);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(complete, new Insets(2, 2, 0, 3));
                        HBox.setMargin(cancel, new Insets(2, 2, 0, 3));
                        HBox.setMargin(delete, new Insets(2, 2, 0, 3));
                        HBox.setMargin(update, new Insets(2, 3, 0, 3));
                        setGraphic(manageBtn);

                        setText(null);

                    }
                }
            };

            return cell;
        };

        actionCol.setCellFactory(cellFoctory);
        loadTable();

    }


    public void loadTable(){

        List<SessionDTO> sessionDTOS = sessionBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(SessionDTO x : sessionDTOS){
            SessionTM sessionTM = DTOToTM.ToSessionTM(x);

            if(sessionTM.getPaymentId()==null){
                sessionTM.setPaymentId("pending");
            }
            tableData.add(sessionTM);
        }

        table.setItems(tableData);
    }


    public void setTableRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();

        for (int i = 0; i < tableData.size(); i++) {
            rows.add(i+1);
        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();
    }


    public void setDateCmbValues(){

        ObservableList<String> dates = FXCollections.observableArrayList();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d", Locale.ENGLISH);

        LocalDate endDate = today.plusDays(6);

        while (!today.isAfter(endDate)) {
            String date = today.format(formatter);
            dates.add(date);
            today = today.plusDays(1);
        }

        dateCmb.setItems(dates);
    }


    public void tableSearch() {

        FilteredList<SessionTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(therapySession -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (therapySession.getSessionId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapySession.getPatientId().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapySession.getTherapy().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapySession.getTherapistId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapySession.getDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapySession.getTime().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapySession.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (therapySession.getPaymentId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<SessionTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void clean(){

        generateNewId();
        patientIdField.clear();
        patientDetailsLabel.setText("");
        therapyProgramDetailsLabel.setText("");
        doctorCmb.setItems(FXCollections.observableArrayList());
        timeCmb.setItems(FXCollections.observableArrayList());
        therapyCmb.setItems(FXCollections.observableArrayList());
        dateCmb.getSelectionModel().clearSelection();
        setDateCmbValues();
        loadTable();
        setTableRowCmbValues();
        table.getSelectionModel().clearSelection();
        updateBtn.setDisable(true);
        payAndAddBtn.setDisable(false);
        payLaterAndAddBtn.setDisable(false);
        patientIdField.setDisable(false);
        therapyCmb.setDisable(false);
        selectedItemToUpdate = null;

    }

    public void notification(String message){

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(message);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(4));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }
}







