package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.RegisterBO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.RegisterDTO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.Patient;
import com.example.ormcoursework.entity.Payment;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.view.tm.PatientTM;
import com.example.ormcoursework.view.tm.RegisterTM;
import com.example.ormcoursework.view.tm.ScheduleTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RegisterController implements Initializable {

    @FXML
    private TableColumn<RegisterTM, String> actionCol;

    @FXML
    private TableColumn<RegisterTM, Date> endDateCol;

    @FXML
    private Label feeLabel;

    @FXML
    private Label patientDetailsLabel;

    @FXML
    private TableColumn<RegisterTM, String> patientIdCol;

    @FXML
    private TextField patientIdField;

    @FXML
    private Button payAndRegisterBtn;

    @FXML
    private Button payLaterAndRegisterBtn;

    @FXML
    private TableColumn<RegisterTM, String> paymentIdCol;

    @FXML
    private TableColumn<RegisterTM, String> registrationIdCol;

    @FXML
    private Label registrationIdLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private TableColumn<RegisterTM, Integer> sessionCountCol;

    @FXML
    private TableColumn<RegisterTM, Date> startDateCol;

    @FXML
    private TableColumn<RegisterTM, String> statusCol;

    @FXML
    private TableView<RegisterTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private TableColumn<RegisterTM, String> therapyCol;

    @FXML
    private ComboBox<String> therapyProgramCmb;

    private final RegisterBO registerBO = BOFactory.getInstance().getBO(BOFactory.BOType.REGISTER);
    private ObservableList<RegisterTM> tableData = FXCollections.observableArrayList();

    private List<TherapyProgramDTO> availablePrograms = new ArrayList<>();
    private ObservableList<String> therapyProgramCmbValues = FXCollections.observableArrayList();


    @FXML
    void patientSearchOnMouseClick(MouseEvent event) {

        String str = patientIdField.getText();
        PatientDTO patientDTO;

        if(str.length()==8){

            patientDTO = registerBO.getPatientById(str);

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

            patientDTO = registerBO.getPatientByPhoneNo(str);

            if(patientDTO==null){

                notification("No Patient Is Registered With This Phone Number.");
                return;
            }

            String patientDetails = patientDTO.getName()+ " "+ patientDTO.getAge()+" "+patientDTO.getGender();
            patientDetailsLabel.setText(patientDetails);
        }

        else{

            notification("Not A Valid Patient Id Or Phone Number, Please Enter Valid Data");
            return;
        }

        String patientDetails = patientDTO.getName()+ "   "+ patientDTO.getAge()+"   "+patientDTO.getGender();
        patientDetailsLabel.setText(patientDetails);

        availablePrograms = registerBO.getAvailableTherapyProgramForSelectedPatient(patientDTO);

        if(availablePrograms.isEmpty()){
            notification("This Patient Is Already Registered For All Available Therapy Programs, So No Therapy Program Available To Register");
            patientDetailsLabel.setText("");
            return;
        }

        ObservableList<String> availableTherapyPrograms = FXCollections.observableArrayList();

        for(TherapyProgramDTO x : availablePrograms){

            availableTherapyPrograms.add(x.getName());
        }

        therapyProgramCmbValues = availableTherapyPrograms;
        therapyProgramCmb.setItems(therapyProgramCmbValues);
    }


    @FXML
    void payAndRegisterBtnOnAction(ActionEvent event) {

        String id = registrationIdLabel.getText();
        String patientId = patientIdField.getText();
        String therapy = therapyProgramCmb.getSelectionModel().getSelectedItem();
        BigDecimal upFrontPayment = new BigDecimal(feeLabel.getText());

        if(id.isEmpty() || patientId.isEmpty() || therapy==null){

            notification("Please Fill In All Field To Add A Registration");
            return;
        }

        RegisterDTO registerDTO = new RegisterDTO(id, new Date(),"Active",new TherapyProgram(therapy,upFrontPayment),new Patient(patientId),new Payment(upFrontPayment));
        boolean isSave = registerBO.addAndPay(registerDTO);

        if(isSave){
            notification("Successfully Add A New Registration For patient ID: "+patientId+" For Therapy Program : "+therapy);
        }
        else{
            notification("Failed To Add A New Registration For Patient ID: "+patientId+" Please Try Again Later.");
        }

        clean();

    }


    @FXML
    void payLaterAndRegisterBtnOnAction(ActionEvent event) {

        String id = registrationIdLabel.getText();
        String patientId = patientIdField.getText();
        String therapy = therapyProgramCmb.getSelectionModel().getSelectedItem();
        BigDecimal upFrontPayment = new BigDecimal(feeLabel.getText());

        if(id.isEmpty() || patientId.isEmpty() || therapy==null){

            notification("Please Fill In All Field To Add A Registration");
            return;
        }

        RegisterDTO registerDTO = new RegisterDTO(id, new Date(),"Active",new TherapyProgram(therapy,upFrontPayment),new Patient(patientId));
        boolean isSave = registerBO.addAndPayLater(registerDTO);

        if(isSave){
            notification("Successfully Add A New Registration For patient ID: "+patientId+" For Therapy Program : "+therapy);
        }
        else{
            notification("Failed To Add A New Registration For Patient ID: "+patientId+" Please Try Again Later.");
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

        ObservableList<RegisterTM> registerTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            registerTMS.add(tableData.get(i));
        }

        table.setItems(registerTMS);
    }


    @FXML
    void therapyProgramCmbOnAction(ActionEvent event) {

        if(patientDetailsLabel.getText().isEmpty()){
//            notification("Please First Select Patient First");
            return;
        }

        String program = therapyProgramCmb.getSelectionModel().getSelectedItem();

        if(program==null){
            return;
        }

        for (TherapyProgramDTO x : availablePrograms){

            if(program.equals(x.getName())){
                feeLabel.setText(String.valueOf(x.getFee()));
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewId();
        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setTableActionColumn();
        tableSearch();
        table.getSelectionModel().clearSelection();
        clean();
    }


    public void generateNewId(){

        String newId = registerBO.generateNewId();
        registrationIdLabel.setText(newId);
    }


    public void setTableColumn(){

        registrationIdCol.setCellValueFactory(new PropertyValueFactory<>("registerId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        therapyCol.setCellValueFactory(new PropertyValueFactory<>("therapy"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        sessionCountCol.setCellValueFactory(new PropertyValueFactory<>("sessionsTaken"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<RegisterTM, String>, TableCell<RegisterTM, String>> cellFoctory = (TableColumn<RegisterTM, String> param) -> {

            final TableCell<RegisterTM, String> cell = new TableCell<RegisterTM, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        RegisterTM registerTM = getTableView().getItems().get(getIndex());

                        Image image1 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/eye.png");
                        Image image2 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/trash.png");

                        Image active = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/morning-routine.png");
                        Image expired = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/expired (1).png");

                        ImageView expireOrNot = new ImageView();
                        expireOrNot.setFitHeight(22);
                        expireOrNot.setFitWidth(22);

                        boolean res = "Expired".equals(registerTM.getStatus());
                        expireOrNot.setImage(res ? expired : active);

                        ImageView show = new ImageView();
                        show.setImage(image1);
                        show.setFitHeight(19);
                        show.setFitWidth(20);

                        ImageView delete = new ImageView();
                        delete.setImage(image2);
                        delete.setFitHeight(14);
                        delete.setFitWidth(14);

                        show.setStyle(" -fx-cursor: hand ;");
                        delete.setStyle(" -fx-cursor: hand ;");


                        show.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("show");

                        });


                        delete.setOnMouseClicked((MouseEvent event) -> {

                            RegisterTM selectedItem = table.getSelectionModel().getSelectedItem();
                            Optional<ButtonType> result;

                            if(selectedItem.getStatus().equals("Expired")){

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"The Registration Time Has Expired, Recommend To Delete This Registration, Are You Sure You Want To Proceed With The Deletion?",ButtonType.YES,ButtonType.CANCEL);
                                result = alert.showAndWait();
                            }
                            else if(selectedItem.getStatus().equals("Completed")){

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"The Sessions Taken Have Exceeded The Session Count, Recommend To Delete This Registration, Are You Sure You Want To Proceed With The Deletion?",ButtonType.YES,ButtonType.CANCEL);
                                result = alert.showAndWait();
                            }
                            else {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Selected Registration Is Currently Active. Are you sure you want to delete this registration ID: "+selectedItem.getRegisterId(),ButtonType.YES,ButtonType.CANCEL);
                                result = alert.showAndWait();
                            }


                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                RegisterDTO registerDTO = TMToDTO.ToRegisterDTO(selectedItem);
                                boolean isDelete = registerBO.delete(registerDTO);

                                if(isDelete){
                                    notification("Successfully Deleted The Registration ID: "+selectedItem.getPatientId());
                                }
                                else{
                                    notification("Failed Delete The Registration ID: "+selectedItem.getPatientId()+", Try Again Later");
                                }
                            }

                            clean();

                        });


                        HBox manageBtn = new HBox(expireOrNot,show,delete);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(4);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(expireOrNot, new Insets(2, 3, 0, 3));
                        HBox.setMargin(show, new Insets(2, 2, 0, 3));
                        HBox.setMargin(delete, new Insets(2, 2, 0, 3));
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

        List<RegisterDTO> registerDTOS = registerBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(RegisterDTO x : registerDTOS){
            tableData.add(DTOToTM.ToRegisterTM(x));
        }

        for(RegisterTM x : tableData){
            if(x.getPaymentId()==null){
                x.setPaymentId("Pending");
            }
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


    public void tableSearch() {

        FilteredList<RegisterTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(register -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (register.getRegisterId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (register.getPatientId().contains(lowerCaseFilter)) {
                    return true;
                } else if (register.getTherapy().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(register.getStartDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(register.getEndDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(register.getSessionsTaken()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (register.getPaymentId().contains(lowerCaseFilter)) {
                    return true;
                }else if (register.getStatus().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<RegisterTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void clean(){

        generateNewId();
        patientIdField.clear();
        patientDetailsLabel.setText("");
        feeLabel.setText("");
        therapyProgramCmbValues.clear();
        loadTable();
        setTableRowCmbValues();
        table.getSelectionModel().clearSelection();

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




