package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.TherapistProgramAssignmentBO;
import com.example.ormcoursework.dto.*;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.entity.TherapistProgramAssignment;
import com.example.ormcoursework.entity.TherapyProgram;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.view.tm.PatientTM;
import com.example.ormcoursework.view.tm.TherapistProgramAssignmentTM;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapistProgramAssignmentController implements Initializable {

    @FXML
    private TableColumn<TherapistProgramAssignmentTM, String> actionCol;

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<TherapistProgramAssignmentTM, Date> dateCol;

    @FXML
    private TextField searchBar;

    @FXML
    private TableColumn<TherapistProgramAssignmentTM, String> statusCol;

    @FXML
    private TableView<TherapistProgramAssignmentTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private Label therapistDeatilsLabel;

    @FXML
    private ComboBox<String> therapistIdCmb;

    @FXML
    private TableColumn<TherapistProgramAssignmentTM, String> therapistIdCol;

    @FXML
    private ComboBox<String> therapyCmb;

    @FXML
    private TableColumn<TherapistProgramAssignmentTM, String> therapyCol;

    @FXML
    private Label therapyDetailsLabel;

    private ObservableList<TherapistProgramAssignmentTM> tableData = FXCollections.observableArrayList();
    private final TherapistProgramAssignmentBO therapistProgramAssignmentBO = BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST_PROGRAM_ASSIGNMENT);


    @FXML
    void addBtnOnAction(ActionEvent event) {

        String therapistId = therapistIdCmb.getSelectionModel().getSelectedItem();
        String therapy = therapyCmb.getSelectionModel().getSelectedItem();

        if(therapistId==null || therapy==null){
            notification("Please Select Therapist And Therapy Program To Assign Therapist To Therapy Program");
            return;
        }

        TherapistProgramAssignmentDTO therapistProgramAssignmentDTO = new TherapistProgramAssignmentDTO(new Therapist(therapistId),new TherapyProgram(therapy,new BigDecimal("0.0")),new Date(),"Active");
        boolean isSave = therapistProgramAssignmentBO.add(therapistProgramAssignmentDTO);

        if(isSave){
            notification("Successfully Assigned Therapist ID: "+therapistId+" To Therapy Program: "+therapy);
        }
        else{
            notification("Failed To Assign A Therapy Program To Therapist ID: "+therapistId+" ,Please Try Again Later");
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

        ObservableList<TherapistProgramAssignmentTM> patientTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            patientTMS.add(tableData.get(i));
        }

        table.setItems(patientTMS);
    }



    @FXML
    void therapistIdCmbOnAction(ActionEvent event) {

        String therapistId = therapistIdCmb.getSelectionModel().getSelectedItem();

        if(therapistId==null){
            return;
        }

        TherapistDTO therapistDTO = therapistProgramAssignmentBO.getSelectedTherapistDetails(therapistId);
        if(therapistDTO!=null) {
            String details = therapistDTO.getName() + "-" + therapistDTO.getSpecialization();
            therapistDeatilsLabel.setText(details);
        }

        List<TherapyProgramDTO> therapyProgramDTOSList  = therapistProgramAssignmentBO.getTherapyProgramList(therapistId);

        ObservableList<String> therapyPrograms = FXCollections.observableArrayList();
        for(TherapyProgramDTO x: therapyProgramDTOSList){
            therapyPrograms.add(x.getName());
        }

        therapyCmb.setItems(therapyPrograms);
    }



    @FXML
    void therapyCmbOnAction(ActionEvent event) {

        String therapy = therapyCmb.getSelectionModel().getSelectedItem();

        if(therapy==null){
            return;
        }

        TherapyProgramDTO therapyProgramDTO = therapistProgramAssignmentBO.getSelectedTherapyDetails(therapy);
        if(therapyProgramDTO!=null) {
            String details = therapyProgramDTO.getName() + "-" + therapyProgramDTO.getDuration();
            therapyDetailsLabel.setText(details);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setTherapistIdCmbValues();
        setTableActionColumn();
        tableSearch();
        table.getSelectionModel().clearSelection();
        clean();
    }


    public void setTableColumn(){

        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        therapyCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgram"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("assignmentDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("assignmentStatus"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<TherapistProgramAssignmentTM, String>, TableCell<TherapistProgramAssignmentTM, String>> cellFoctory = (TableColumn<TherapistProgramAssignmentTM, String> param) -> {

            final TableCell<TherapistProgramAssignmentTM, String> cell = new TableCell<TherapistProgramAssignmentTM, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        TherapistProgramAssignmentTM therapistProgramAssignmentTM = getTableView().getItems().get(getIndex());

                        Image image1 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/eye.png");
                        Image image2 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/trash.png");

                        Image crossImg = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/close.png");
                        Image rightImg = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/check (1).png");

                        ImageView availability = new ImageView();
                        availability.setFitHeight(17);
                        availability.setFitWidth(17);

                        boolean isAvailable = "Active".equals(therapistProgramAssignmentTM.getAssignmentStatus());
                        availability.setImage(isAvailable ? crossImg : rightImg);

                        ImageView show = new ImageView();
                        show.setImage(image1);
                        show.setFitHeight(18);
                        show.setFitWidth(19);

                        ImageView delete = new ImageView();
                        delete.setImage(image2);
                        delete.setFitHeight(14);
                        delete.setFitWidth(14);


                        show.setStyle(" -fx-cursor: hand ;");
                        delete.setStyle(" -fx-cursor: hand ;");
                        availability.setStyle(" -fx-cursor: hand ;");

                        show.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("show");

                        });


                        delete.setOnMouseClicked((MouseEvent event) -> {

                            TherapistProgramAssignmentTM selectedItem = table.getSelectionModel().getSelectedItem();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete Selected Therapy Program Assignment For Therapist ID: "+selectedItem.getTherapistId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                TherapistProgramAssignmentDTO therapistProgramAssignmentDTO = TMToDTO.ToTherapistProgramAssignmentDTO(selectedItem);
                                boolean isDelete = therapistProgramAssignmentBO.delete(therapistProgramAssignmentDTO);

                                if(isDelete){
                                    notification("Successfully Deleted The Therapy Program Assignment");
                                }
                                else{
                                    notification("Failed To Delete!, Please Try Again Later");
                                }
                            }

                            clean();

                        });


                        availability.setOnMouseClicked((MouseEvent event) -> {

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to mark selected assignment "+(isAvailable ? " as Inactive?" : " as Active?"), ButtonType.YES, ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.YES) {

                                TherapistProgramAssignmentDTO therapistProgramAssignmentDTO = TMToDTO.ToTherapistProgramAssignmentDTO(therapistProgramAssignmentTM);

                                String newStatus = isAvailable ? "Inactive" : "Active";
                                therapistProgramAssignmentDTO.setAssignmentStatus(newStatus);
                                boolean isChanged = therapistProgramAssignmentBO.changeStatus(therapistProgramAssignmentDTO);

                                if (isChanged) {
                                    therapistProgramAssignmentTM.setAssignmentStatus(newStatus);
                                    availability.setImage("Available".equals(newStatus) ? rightImg : crossImg);
                                    table.refresh();
                                    notification("Change Assignment Status Successfully!");
                                } else {
                                    notification("Failed to Change Assignment Status, Please Try Again Later");
                                }
                            }

                            clean();

                        });

                        HBox manageBtn = new HBox(show,delete,availability);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(4);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(show, new Insets(2, 2, 0, 3));
                        HBox.setMargin(delete, new Insets(2, 2, 0, 3));
                        HBox.setMargin(availability, new Insets(2, 3, 0, 3));
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

        List<TherapistProgramAssignmentDTO> all = therapistProgramAssignmentBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(TherapistProgramAssignmentDTO x : all){
            tableData.add(DTOToTM.ToTherapistProgramAssignmentTM(x));
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


    public void setTherapistIdCmbValues(){

        ObservableList<String> therapistIds = FXCollections.observableArrayList();
        List<TherapistDTO> therapistDTOS = therapistProgramAssignmentBO.getAllTherapists();

        for(TherapistDTO x : therapistDTOS){
            therapistIds.add(x.getTherapistId());
        }

        therapistIdCmb.setItems(therapistIds);

    }


    public void tableSearch() {

        FilteredList<TherapistProgramAssignmentTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(assignment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (assignment.getTherapistId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (assignment.getTherapyProgram().contains(lowerCaseFilter)) {
                    return true;
                } else if (assignment.getAssignmentStatus().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(assignment.getAssignmentDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<TherapistProgramAssignmentTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void clean(){

        therapistIdCmb.getSelectionModel().clearSelection();
        therapyCmb.setItems(FXCollections.observableArrayList());
        setTherapistIdCmbValues();
        therapistDeatilsLabel.setText("'");
        therapyDetailsLabel.setText("");
        loadTable();
        setTableRowCmbValues();
        table.getSelectionModel().clearSelection();
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
