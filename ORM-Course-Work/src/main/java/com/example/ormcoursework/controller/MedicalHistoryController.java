package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.MedicalHistoryBO;
import com.example.ormcoursework.bo.custom.PatientBO;
import com.example.ormcoursework.dto.MedicalHistoryDTO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.view.tm.MedicalHistoryTM;
import com.example.ormcoursework.view.tm.PatientTM;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MedicalHistoryController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<MedicalHistoryTM, String> actionCol;

    @FXML
    private TableColumn<MedicalHistoryTM, String> bPressureCol;

    @FXML
    private TextField bloodPressureField;

    @FXML
    private ComboBox<String> conditionCmb;

    @FXML
    private TableColumn<MedicalHistoryTM, String> conditionCol;

    @FXML
    private TableColumn<MedicalHistoryTM, Date> dateCol;

    @FXML
    private TableColumn<MedicalHistoryTM, String> diagnosisCol;

    @FXML
    private TextField diagnosisField;

    @FXML
    private Label diagnosisTextField;

    @FXML
    private TableColumn<MedicalHistoryTM, String> historyIdCol;

    @FXML
    private Label medicalHistoryIdLabel;

    @FXML
    private Label patientDetailsLabel;

    @FXML
    private ComboBox<String> patientIdCmb;

    @FXML
    private TableColumn<MedicalHistoryTM, String> patientIdCol;

    @FXML
    private ComboBox<String> responseCmb;

    @FXML
    private TableColumn<MedicalHistoryTM, String> responseCol;

    @FXML
    private TextField searchBar;

    @FXML
    private ComboBox<String> sleepingPatternCmb;

    @FXML
    private TableColumn<MedicalHistoryTM, String> sleepingPatternCol;

    @FXML
    private TableView<MedicalHistoryTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private Button updateBtn;

    private ObservableList<MedicalHistoryTM> tableData = FXCollections.observableArrayList();
    private final MedicalHistoryBO medicalHistoryBO = BOFactory.getInstance().getBO(BOFactory.BOType.MEDICAL_HISTORY);

    @FXML
    void addBtnOnAction(ActionEvent event) {

    }

    @FXML
    void patientIdCmbOnAction(ActionEvent event) {

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

        ObservableList<MedicalHistoryTM> historyTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            historyTMS.add(tableData.get(i));
        }

        table.setItems(historyTMS);
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewId();
        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setSleepingPatternCmbValues();
        setResponseCmbValues();
        setConditionCmbValues();
        setPatientIdCmbValues();
        setTableActionColumn();
        tableSearch();
        table.getSelectionModel().clearSelection();
        clean();
    }


    public void generateNewId(){

        String newId = medicalHistoryBO.generateNewId();
        medicalHistoryIdLabel.setText(newId);
    }


    public void setTableColumn(){

        historyIdCol.setCellValueFactory(new PropertyValueFactory<>("medicalHistoryId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("primaryDiagnosis"));
        bPressureCol.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        sleepingPatternCol.setCellValueFactory(new PropertyValueFactory<>("sleepingHabit"));
        responseCol.setCellValueFactory(new PropertyValueFactory<>("therapyResponse"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<MedicalHistoryTM, String>, TableCell<MedicalHistoryTM, String>> cellFoctory = (TableColumn<MedicalHistoryTM, String> param) -> {

            final TableCell<MedicalHistoryTM, String> cell = new TableCell<MedicalHistoryTM, String>() {
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

//                            PatientTM selectedItem = table.getSelectionModel().getSelectedItem();
//
//                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete The Patient ID: "+selectedItem.getPatientId(),ButtonType.YES,ButtonType.CANCEL);
//                            Optional<ButtonType> result = alert.showAndWait();
//
//                            if(result.isPresent() && (result.get() == ButtonType.YES)){
//
//                                PatientDTO patientDTO = TMToDTO.ToPatientDTO(selectedItem);
//                                boolean isDelete = patientBO.delete(patientDTO);
//
//                                if(isDelete){
//                                    notification("Successfully Deleted The Patient ID: "+selectedItem.getPatientId());
//                                }
//                                else{
//                                    notification("Failed Delete The Patient ID: "+selectedItem.getPatientId());
//                                }
//                            }
//
//                            clean();

                        });


                        update.setOnMouseClicked((MouseEvent event) -> {

//                            PatientTM selectedItem = table.getSelectionModel().getSelectedItem();
//
//                            patientIdLabel.setText(selectedItem.getPatientId());
//                            nameField.setText(selectedItem.getName());
//                            phoneNoField.setText(selectedItem.getPhoneNo());
//                            ageField.getValueFactory().setValue(selectedItem.getAge());
//                            genderCmb.getSelectionModel().select(selectedItem.getGender());
//
//                            updateBtn.setDisable(false);
//                            addBtn.setDisable(true);

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

        List<MedicalHistoryDTO> medicalHistoryDTOS = medicalHistoryBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(MedicalHistoryDTO x : medicalHistoryDTOS){
            tableData.add(DTOToTM.ToMedicalHistoryTM(x));
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


    public void setPatientIdCmbValues(){

        List<PatientDTO> patientDTOS = medicalHistoryBO.getAllPatient();
        ObservableList<String> patientIds = FXCollections.observableArrayList();

        for (PatientDTO x : patientDTOS){
            patientIds.add(x.getPatientId());
        }
        patientIdCmb.setItems(patientIds);
    }


    public void setSleepingPatternCmbValues(){

        ObservableList<String> patterns = FXCollections.observableArrayList("Normal Sleep","Insomnia","Hypersomnia","Sleep Apnea","Narcolepsy","Restless Leg Syndrome","Sleepwalking","Night Terrors","Delayed Sleep Phase Syndrome","Non-Restorative Sleep","Sleep Paralysis");
        sleepingPatternCmb.setItems(patterns);
    }


    public void setResponseCmbValues(){

        ObservableList<String> sleepingHabits = FXCollections.observableArrayList("Improved","No Change","Worsened","Not Started");
        responseCmb.setItems(sleepingHabits);
    }


    public void setConditionCmbValues(){

        ObservableList<String> conditions = FXCollections.observableArrayList("Stable", "Under Observation", "In Crisis", "Improving", "Declining");
        conditionCmb.setItems(conditions);
    }


    public void tableSearch() {

        FilteredList<MedicalHistoryTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(history -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (history.getMedicalHistoryId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (history.getPatientId().contains(lowerCaseFilter)) {
                    return true;
                } else if (history.getBloodPressure().contains(lowerCaseFilter)) {
                    return true;
                } else if (history.getSleepingHabit().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (history.getPrimaryDiagnosis().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (history.getTherapyResponse().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (history.getCondition().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (String.valueOf(history.getDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<MedicalHistoryTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void userInputValidate(boolean diagnosisValidation,boolean bPressureValidation){

        if(!diagnosisValidation){
            diagnosisField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            diagnosisField.setStyle("-fx-border-color: #CFD8DC");
        }
        if(!bPressureValidation){
            bloodPressureField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            bloodPressureField.setStyle("-fx-border-color: #CFD8DC");
        }
    }


    public void clean(){

        generateNewId();
        patientIdCmb.getSelectionModel().clearSelection();
        diagnosisField.clear();
        bloodPressureField.clear();
        sleepingPatternCmb.getSelectionModel().clearSelection();
        responseCmb.getSelectionModel().clearSelection();
        conditionCmb.getSelectionModel().clearSelection();
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
