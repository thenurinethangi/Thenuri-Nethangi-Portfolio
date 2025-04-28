package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.ScheduleBO;
import com.example.ormcoursework.dto.ScheduleDTO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.view.tm.ScheduleTM;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private TableColumn<ScheduleTM, String> actionCol;

    @FXML
    private Button addBtn;

    @FXML
    private ComboBox<String> dateCmb;

    @FXML
    private TableColumn<ScheduleTM, String> dateCol;

    @FXML
    private TableColumn<ScheduleTM, String> isAvailableCol;

    @FXML
    private TableColumn<ScheduleTM, String> scheduleIdCol;

    @FXML
    private TableColumn<ScheduleTM, Integer> sessionCountCol;

    @FXML
    private Label scheduleIdLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<ScheduleTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private ComboBox<String> therapistIdCmb;

    @FXML
    private TableColumn<TherapistTM, String> therapistIdCol;

    @FXML
    private Button updateBtn;

    private ObservableList<ScheduleTM> tableData = FXCollections.observableArrayList();
    private final ScheduleBO scheduleBO = BOFactory.getInstance().getBO(BOFactory.BOType.SCHEDULE);


    @FXML
    void addBtnOnAction(ActionEvent event) {

        String id = scheduleIdLabel.getText();
        String therapistId = therapistIdCmb.getSelectionModel().getSelectedItem();
        String date = dateCmb.getSelectionModel().getSelectedItem();

        if(id.isEmpty() || therapistId==null || date==null){

            notification("Please Fill In All Field To Add A New Therapist Schedule");
            return;
        }

        ScheduleDTO scheduleDTO = new ScheduleDTO(id,date,0,"Available",new Therapist(therapistId));

        boolean isExist = scheduleBO.isExist(scheduleDTO);

        if(isExist){
            notification("This Schedule For Therapist ID :"+therapistId+ " Is Already Exist");
            return;
        }

        boolean isSave = scheduleBO.add(scheduleDTO);

        if(!isSave){
            notification("Something Went Wrong With Adding A New Therapist Schedule, Please Try Again Later");
        }
        else{
            notification("Successfully Added The New Therapist Schedule");
        }

        clean();

    }

    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer selectedNo = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(selectedNo==null){
            return;
        }

        ObservableList<ScheduleTM> scheduleTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            scheduleTMS.add(tableData.get(i));
        }

        table.setItems(scheduleTMS);
    }

    @FXML
    void refreshOnMouseClick(MouseEvent event) {

        clean();
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {

        ScheduleTM selectedSchedule = table.getSelectionModel().getSelectedItem();

        if(selectedSchedule==null){
            return;
        }

        String id = scheduleIdLabel.getText();
        String therapistId = therapistIdCmb.getSelectionModel().getSelectedItem();
        String date = dateCmb.getSelectionModel().getSelectedItem();

        if(id.isEmpty() || therapistId==null || date==null){

            notification("All Fields Must Be Filled In To Update The Selected Schedule.");
            return;
        }

        ScheduleDTO scheduleDTO = new ScheduleDTO(id,date,new Therapist(therapistId));

        boolean isUpdate = scheduleBO.update(scheduleDTO);

        if(isUpdate){
            notification("Successfully Updated Schedule ID: "+id);
        }
        else{
            notification("Failed To Update Schedule ID: "+id+" Please Try Again Later.");
        }

        clean();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewId();
        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setTherapistIdCmbValues();
        setDateCmbValues();
        setTableActionColumn();
        tableSearch();
        table.getSelectionModel().clearSelection();
        clean();
    }


    public void generateNewId(){

        String newId = scheduleBO.generateNewId();
        scheduleIdLabel.setText(newId);
    }


    public void setTableColumn(){

        scheduleIdCol.setCellValueFactory(new PropertyValueFactory<>("scheduleNo"));
        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        sessionCountCol.setCellValueFactory(new PropertyValueFactory<>("bookedSessionCount"));
        isAvailableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<ScheduleTM, String>, TableCell<ScheduleTM, String>> cellFoctory = (TableColumn<ScheduleTM, String> param) -> {

            final TableCell<ScheduleTM, String> cell = new TableCell<ScheduleTM, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        ScheduleTM schedule = getTableView().getItems().get(getIndex());

                        Image image1 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/eye.png");
                        Image image2 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/trash.png");
                        Image image3 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/edit.png");
                        Image image4 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/close.png");

                        Image crossImg = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/close.png");
                        Image rightImg = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/check (1).png");

                        ImageView availability = new ImageView();
                        availability.setFitHeight(17);
                        availability.setFitWidth(17);

                        boolean isAvailable = "Available".equals(schedule.getAvailable());
                        availability.setImage(isAvailable ? crossImg : rightImg);

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
                        availability.setStyle(" -fx-cursor: hand ;");


                        show.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("show");

                        });


                        delete.setOnMouseClicked((MouseEvent event) -> {

                            ScheduleTM selectedItem = table.getSelectionModel().getSelectedItem();

                            if(selectedItem.getBookedSessionCount()>0){

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete The Schedule ID: "+selectedItem.getScheduleNo()+" There Is A Booked Sessions For Scheduled Date. If You Delete Schedule, Session Appointment Will Be Canceled",ButtonType.YES,ButtonType.CANCEL);
                                Optional<ButtonType> result = alert.showAndWait();

                                if(result.isPresent() && (result.get() == ButtonType.YES)){

                                    ScheduleDTO scheduleDTO = TMToDTO.ToScheduleDTO(selectedItem);
                                    boolean isDelete = scheduleBO.delete(scheduleDTO);

                                    if(isDelete){
                                        notification("Successfully Deleted The Schedule ID: "+selectedItem.getScheduleNo());
                                    }
                                    else{
                                        notification("Failed Delete The Schedule ID: "+selectedItem.getScheduleNo());
                                    }
                                }
                            }

                            else {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure You Want To Delete The Schedule ID: " + selectedItem.getScheduleNo(), ButtonType.YES, ButtonType.CANCEL);
                                Optional<ButtonType> result = alert.showAndWait();

                                if (result.isPresent() && (result.get() == ButtonType.YES)) {

                                    ScheduleDTO scheduleDTO = TMToDTO.ToScheduleDTO(selectedItem);
                                    boolean isDelete = scheduleBO.delete(scheduleDTO);

                                    if(isDelete){
                                        notification("Successfully Deleted The Schedule ID: "+selectedItem.getScheduleNo());
                                    }
                                    else{
                                        notification("Failed Delete The Schedule ID: "+selectedItem.getScheduleNo());
                                    }
                                }
                            }

                            clean();

                        });


                        update.setOnMouseClicked((MouseEvent event) -> {

                            ScheduleTM selectedSchedule = table.getSelectionModel().getSelectedItem();

                            scheduleIdLabel.setText(selectedSchedule.getScheduleNo());
                            therapistIdCmb.getSelectionModel().select(selectedSchedule.getTherapistId());
                            dateCmb.getSelectionModel().select(selectedSchedule.getDate());

                            updateBtn.setDisable(false);
                            addBtn.setDisable(true);

                        });


                        availability.setOnMouseClicked(event -> {

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to mark Schedule No: " + schedule.getScheduleNo() + (isAvailable ? " as Unavailable?" : " as Available?"), ButtonType.YES, ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.YES) {

                                ScheduleDTO scheduleDTO = TMToDTO.ToScheduleDTO(schedule);

                                String newStatus = isAvailable ? "Unavailable" : "Available";
                                scheduleDTO.setAvailable(newStatus);
                                boolean isChanged = scheduleBO.changeAvailability(scheduleDTO);

                                if (isChanged) {
                                    schedule.setAvailable(newStatus);
                                    availability.setImage("Available".equals(newStatus) ? rightImg : crossImg);
                                    table.refresh();
                                    notification("Schedule No: " + schedule.getScheduleNo() +
                                            " is now " + newStatus);
                                } else {
                                    notification("Failed to update Schedule No: " + schedule.getScheduleNo());
                                }
                            }

                            clean();
                        });


                        HBox manageBtn = new HBox(show,delete,update,availability);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(4);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(show, new Insets(2, 2, 0, 3));
                        HBox.setMargin(delete, new Insets(2, 2, 0, 3));
                        HBox.setMargin(update, new Insets(2, 3, 0, 3));
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

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd");

        List<ScheduleDTO> scheduleDTOS = scheduleBO.getAll();
        tableData = FXCollections.observableArrayList();

        for (ScheduleDTO x : scheduleDTOS) {
            String dayName = x.getDate();
            DayOfWeek targetDay = null;

            switch (dayName) {
                case "Monday": targetDay = DayOfWeek.MONDAY; break;
                case "Tuesday": targetDay = DayOfWeek.TUESDAY; break;
                case "Wednesday": targetDay = DayOfWeek.WEDNESDAY; break;
                case "Thursday": targetDay = DayOfWeek.THURSDAY; break;
                case "Friday": targetDay = DayOfWeek.FRIDAY; break;
                case "Saturday": targetDay = DayOfWeek.SATURDAY; break;
                case "Sunday": targetDay = DayOfWeek.SUNDAY; break;
            }

            if (targetDay != null) {
                LocalDate scheduleDate = today.with(TemporalAdjusters.nextOrSame(targetDay));

                if (!scheduleDate.isAfter(today.plusDays(7))) {
                    String formattedDate = scheduleDate.format(formatter);
                    x.setDate(formattedDate);
                    tableData.add(DTOToTM.ToScheduleTM(x));
                }
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


    public void setTherapistIdCmbValues(){

        ObservableList<String> therapistIds = FXCollections.observableArrayList();
        List<TherapistDTO> therapistDTOS = scheduleBO.getAllTherapist();

        for(TherapistDTO x : therapistDTOS){
            therapistIds.add(x.getTherapistId());
        }

        therapistIdCmb.setItems(therapistIds);
    }


    public void setDateCmbValues(){

        ObservableList<String> dates = FXCollections.observableArrayList("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        dateCmb.setItems(dates);
    }


    public void tableSearch() {

        FilteredList<ScheduleTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(schedule -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (schedule.getScheduleNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (schedule.getTherapistId().contains(lowerCaseFilter)) {
                    return true;
                } else if (schedule.getDate().contains(lowerCaseFilter)) {
                    return true;
                } else if (schedule.getAvailable().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(schedule.getBookedSessionCount()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<ScheduleTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void clean(){

        generateNewId();
        loadTable();
        therapistIdCmb.getSelectionModel().clearSelection();
        dateCmb.getSelectionModel().clearSelection();
        setTableRowCmbValues();
        setTherapistIdCmbValues();
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
