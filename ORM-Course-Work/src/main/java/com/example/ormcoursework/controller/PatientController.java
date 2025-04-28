package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.PatientBO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.util.UserInputValidation;
import com.example.ormcoursework.view.tm.PatientTM;
import com.example.ormcoursework.view.tm.TherapistTM;
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

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML
    private TableColumn<PatientTM, String> actionCol;

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<PatientTM, Integer> ageCol;

    @FXML
    private Spinner<Integer> ageField;

    @FXML
    private ComboBox<String> genderCmb;

    @FXML
    private TableColumn<PatientTM, String> genderCol;

    @FXML
    private TableColumn<PatientTM, String> nameCol;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<PatientTM, String> patientIdCol;

    @FXML
    private TableColumn<PatientTM, String> phoneNoCol;

    @FXML
    private TableColumn<PatientTM, Integer> enrolledTherapyProgramCountCol;

    @FXML
    private TextField phoneNoField;

    @FXML
    private Label patientIdLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<PatientTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private Button updateBtn;

    private ObservableList<PatientTM> tableData = FXCollections.observableArrayList();
    private final PatientBO patientBO = BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);

    @FXML
    void addBtnOnAction(ActionEvent event) {

        String id = patientIdLabel.getText();
        String name = nameField.getText();
        String phoneNo = phoneNoField.getText();
        Integer age = ageField.getValueFactory().getValue();
        String gender = genderCmb.getSelectionModel().getSelectedItem();

        if(id.isEmpty() || name.isEmpty() || phoneNo.isEmpty() || age==null || gender==null){

            notification("Please Fill In All Field To Add A New Patient");
            return;
        }

        boolean nameValidation = UserInputValidation.checkNameValidation(name);
        boolean phoneNoValidation = UserInputValidation.checkPhoneNoValidation(phoneNo);

        userInputValidate(nameValidation,phoneNoValidation);

        if(nameValidation && phoneNoValidation){

            PatientDTO patientDTO = new PatientDTO(id,name,phoneNo,age,gender,0,true);

            boolean isSave = patientBO.add(patientDTO);

            if(!isSave){
                notification("Something Went Wrong With Adding A New Patient, Please Try Again Later");
                clean();
                return;
            }

            notification("Successfully Added The New Patient");
            clean();

        }
        else{
            notification("Please Enter Valid Data Into The Fields To Add New Patient");
        }
    }

    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer selectedNo = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(selectedNo==null){
            return;
        }

        ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            patientTMS.add(tableData.get(i));
        }

        table.setItems(patientTMS);
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {

        PatientTM selectedPatient = table.getSelectionModel().getSelectedItem();

        if(selectedPatient==null){
            return;
        }

        String id = patientIdLabel.getText();
        String name = nameField.getText();
        String phoneNo = phoneNoField.getText();
        Integer age = ageField.getValueFactory().getValue();
        String gender = genderCmb.getSelectionModel().getSelectedItem();

        if(id.isEmpty() || name.isEmpty() || phoneNo.isEmpty() || age==null || gender==null){

            notification("Please Fill In All Field To Update A Selected Patient");
            return;
        }

        boolean nameValidation = UserInputValidation.checkNameValidation(name);
        boolean phoneNoValidation = UserInputValidation.checkPhoneNoValidation(phoneNo);

        userInputValidate(nameValidation,phoneNoValidation);

        if(nameValidation && phoneNoValidation) {

            PatientDTO patientDTO = new PatientDTO(id,name,phoneNo,age,gender);

            boolean isUpdate = patientBO.update(patientDTO);

            if(isUpdate){
                notification("Successfully Updated patient ID: "+id);
            }
            else{
                notification("Failed To Update Patient ID: "+id+" Please Try Again Later.");
            }

            clean();
        }
        else{
            notification("Please Enter Valid Data Into The Fields To Update A Patient");
        }
    }


    @FXML
    void refreshOnMouseClick(MouseEvent event) {

        clean();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewId();
        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setGenderCmbValues();
        setAgeSpinnerValues();
        setTableActionColumn();
        tableSearch();
        table.getSelectionModel().clearSelection();
        clean();
    }

    public void generateNewId(){

        String newId = patientBO.generateNewId();
        patientIdLabel.setText(newId);
    }


    public void setTableColumn(){

        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        enrolledTherapyProgramCountCol.setCellValueFactory(new PropertyValueFactory<>("enrolledTherapyProgramCount"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<PatientTM, String>, TableCell<PatientTM, String>> cellFoctory = (TableColumn<PatientTM, String> param) -> {

            final TableCell<PatientTM, String> cell = new TableCell<PatientTM, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/eye.png");
                        Image image2 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/trash.png");
                        Image image3 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/edit.png");


                        ImageView show = new ImageView();
                        show.setImage(image1);
                        show.setFitHeight(18);
                        show.setFitWidth(19);

                        ImageView delete = new ImageView();
                        delete.setImage(image2);
                        delete.setFitHeight(14);
                        delete.setFitWidth(14);

                        ImageView update = new ImageView();
                        update.setImage(image3);
                        update.setFitHeight(17);
                        update.setFitWidth(17);

                        show.setStyle(" -fx-cursor: hand ;");
                        delete.setStyle(" -fx-cursor: hand ;");
                        update.setStyle(" -fx-cursor: hand ;");


                        show.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("show");

                        });


                        delete.setOnMouseClicked((MouseEvent event) -> {

                            PatientTM selectedItem = table.getSelectionModel().getSelectedItem();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete The Patient ID: "+selectedItem.getPatientId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                PatientDTO patientDTO = TMToDTO.ToPatientDTO(selectedItem);
                                boolean isDelete = patientBO.delete(patientDTO);

                                if(isDelete){
                                    notification("Successfully Deleted The Patient ID: "+selectedItem.getPatientId());
                                }
                                else{
                                    notification("Failed Delete The Patient ID: "+selectedItem.getPatientId());
                                }
                            }

                            clean();

                        });


                        update.setOnMouseClicked((MouseEvent event) -> {

                            PatientTM selectedItem = table.getSelectionModel().getSelectedItem();

                            patientIdLabel.setText(selectedItem.getPatientId());
                            nameField.setText(selectedItem.getName());
                            phoneNoField.setText(selectedItem.getPhoneNo());
                            ageField.getValueFactory().setValue(selectedItem.getAge());
                            genderCmb.getSelectionModel().select(selectedItem.getGender());

                            updateBtn.setDisable(false);
                            addBtn.setDisable(true);

                        });

                        HBox manageBtn = new HBox(show,delete,update);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(4);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(show, new Insets(2, 2, 0, 3));
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

        List<PatientDTO> patientDTOS = patientBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(PatientDTO x : patientDTOS){
            tableData.add(DTOToTM.ToPatientTM(x));
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


    public void setGenderCmbValues(){

        ObservableList<String> gender = FXCollections.observableArrayList("Male","Female");
        genderCmb.setItems(gender);
    }


    public void setAgeSpinnerValues(){


        ageField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1));

        ageField.getValueFactory().setValue(20);

    }


    public void tableSearch() {

        FilteredList<PatientTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (patient.getPatientId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (patient.getName().contains(lowerCaseFilter)) {
                    return true;
                } else if (patient.getPhoneNo().contains(lowerCaseFilter)) {
                    return true;
                } else if (patient.getGender().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(patient.getAge()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(patient.getEnrolledTherapyProgramCount()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<PatientTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void userInputValidate(boolean nameValidation,boolean phoneNoValidation){

        if(!nameValidation){
            nameField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            nameField.setStyle("-fx-border-color: #CFD8DC");
        }
        if(!phoneNoValidation){
            phoneNoField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            phoneNoField.setStyle("-fx-border-color: #CFD8DC");
        }
    }


    public void clean(){

        generateNewId();
        nameField.clear();
        phoneNoField.clear();
        ageField.getValueFactory().setValue(20);
        genderCmb.getSelectionModel().clearSelection();
        loadTable();
        setTableRowCmbValues();
        table.getSelectionModel().clearSelection();
        updateBtn.setDisable(true);
        addBtn.setDisable(false);

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
