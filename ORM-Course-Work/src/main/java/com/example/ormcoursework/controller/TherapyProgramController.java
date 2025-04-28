package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.TherapyProgramBO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.util.UserInputValidation;
import com.example.ormcoursework.view.tm.TherapyProgramTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapyProgramController implements Initializable {

    @FXML
    private TableColumn<TherapyProgramTM, String> actionCol;

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<TherapyProgramTM, String> durationCol;

    @FXML
    private TextField durationField;

    @FXML
    private TableColumn<TherapyProgramTM, BigDecimal> feeCol;

    @FXML
    private TextField feeField;

    @FXML
    private TableColumn<TherapyProgramTM, String> nameCol;

    @FXML
    private TextField nameField;

    @FXML
    private Label programIdLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<TherapyProgramTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private TableColumn<TherapyProgramTM, String> therapyIdCol;

    @FXML
    private Button updateBtn;

    private ObservableList<TherapyProgramTM> tableData;
    private final TherapyProgramBO therapyProgramBO = BOFactory.getInstance().getBO(BOFactory.BOType.THERAPY_PROGRAM);

    @FXML
    void addBtnOnAction(ActionEvent event) {

        String id = programIdLabel.getText();
        String name = nameField.getText();
        String duration = durationField.getText().toLowerCase();
        String fee = feeField.getText();

        if(id.isEmpty() || name.isEmpty() || duration.isEmpty() || fee.isEmpty()){

            notification("Please Fill In All Field To Add A New Therapy Program");
            return;
        }

        boolean nameValidation = UserInputValidation.checkNameValidation(name);
        boolean durationValidation = UserInputValidation.checkDurationValidation(duration);
        boolean feeValidation = UserInputValidation.checkDecimalValidation(fee);

        userInputValidate(nameValidation,durationValidation,feeValidation);

        if(nameValidation && durationValidation && feeValidation){

            TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO(id,name,duration,BigDecimal.valueOf(Double.parseDouble(fee)));

            boolean isSave = therapyProgramBO.add(therapyProgramDTO);

            if(!isSave){
                notification("Something Went Wrong With Adding A New Therapy Program, Please Try Again Later");
                clean();
                return;
            }

            notification("Successfully Added The New Therapy Program");
            clean();

        }
        else{
            notification("Please Enter Valid Data Into The Fields To Add New Therapy Program");
        }

    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer selectedNo = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(selectedNo==null){
           return;
        }

        ObservableList<TherapyProgramTM> therapyProgramTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            therapyProgramTMS.add(tableData.get(i));
        }

        table.setItems(therapyProgramTMS);
    }


    @FXML
    void refreshOnMouseClick(MouseEvent event) {

        clean();
    }


    @FXML
    void updateBtnOnAction(ActionEvent event) {

        TherapyProgramTM selectedTherapyProgram = table.getSelectionModel().getSelectedItem();

        if(selectedTherapyProgram==null){
            return;
        }

        String id = programIdLabel.getText();
        String name = nameField.getText();
        String duration = durationField.getText().toLowerCase();
        String fee = feeField.getText();

        if(id.isEmpty() || name.isEmpty() || duration.isEmpty() || fee.isEmpty()){

            notification("All Fields Must Be Filled In To Update The Selected Therapy Program.");
            return;
        }

        boolean nameValidation = UserInputValidation.checkNameValidation(name);
        boolean durationValidation = UserInputValidation.checkDurationValidation(duration);
        boolean feeValidation = UserInputValidation.checkDecimalValidation(fee);

        userInputValidate(nameValidation,durationValidation,feeValidation);

        if(nameValidation && durationValidation && feeValidation) {

            TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO(id,name,duration,new BigDecimal(fee));

            boolean isUpdate = therapyProgramBO.update(therapyProgramDTO);

            if(isUpdate){
                notification("Successfully Updated Therapy Program ID: "+id);
            }
            else{
                notification("Failed To Update Therapy Program ID: "+id+" Please Try Again Later.");
            }

            clean();
        }
        else{
            notification("Please Enter Valid Data Into The Fields To Update A Therapy Program");
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

        String newId = therapyProgramBO.generateNewId();
        programIdLabel.setText(newId);
    }


    public void setTableColumn(){

        therapyIdCol.setCellValueFactory(new PropertyValueFactory<>("therapyId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        feeCol.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<TherapyProgramTM, String>, TableCell<TherapyProgramTM, String>> cellFoctory = (TableColumn<TherapyProgramTM, String> param) -> {

            final TableCell<TherapyProgramTM, String> cell = new TableCell<TherapyProgramTM, String>() {
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

                            TherapyProgramTM selectedItem = table.getSelectionModel().getSelectedItem();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete The Therapy program ID: "+selectedItem.getTherapyId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                TherapyProgramDTO therapyProgramDTO = TMToDTO.ToTherapyProgramDTO(selectedItem);
                                boolean isDelete = therapyProgramBO.delete(therapyProgramDTO);

                                if(isDelete){
                                    notification("Successfully Deleted The Therapy Program ID: "+selectedItem.getTherapyId());
                                }
                                else{
                                    notification("Failed Delete The Therapy Program ID: "+selectedItem.getTherapyId());
                                }
                            }

                            clean();

                        });


                        update.setOnMouseClicked((MouseEvent event) -> {

                            TherapyProgramTM therapyProgram = table.getSelectionModel().getSelectedItem();

                            programIdLabel.setText(therapyProgram.getTherapyId());
                            nameField.setText(therapyProgram.getName());
                            durationField.setText(therapyProgram.getDuration());
                            feeField.setText(String.valueOf(therapyProgram.getFee()));

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

        List<TherapyProgramDTO> therapyProgramDTOS = therapyProgramBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(TherapyProgramDTO x : therapyProgramDTOS){
            tableData.add(DTOToTM.ToTherapyProgramTM(x));
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

        FilteredList<TherapyProgramTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(therapy -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (therapy.getTherapyId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapy.getName().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapy.getDuration().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(therapy.getFee()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<TherapyProgramTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void userInputValidate(boolean nameValidation,boolean durationValidation,boolean feeValidation){

        if(!nameValidation){
            nameField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            nameField.setStyle("-fx-border-color: #CFD8DC");
        }
        if(!durationValidation){
            durationField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            durationField.setStyle("-fx-border-color: #CFD8DC");
        }
        if(!feeValidation){
            feeField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            feeField.setStyle("-fx-border-color: #CFD8DC");
        }
    }


    public void clean(){

        generateNewId();
        nameField.clear();
        durationField.clear();
        feeField.clear();
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
