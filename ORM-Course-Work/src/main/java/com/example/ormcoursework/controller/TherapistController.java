package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.TherapistBO;
import com.example.ormcoursework.dto.TherapistDTO;
import com.example.ormcoursework.dto.TherapyProgramDTO;
import com.example.ormcoursework.entity.Therapist;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.util.UserInputValidation;
import com.example.ormcoursework.view.tm.TherapistTM;
import com.example.ormcoursework.view.tm.TherapyProgramTM;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapistController implements Initializable {

    @FXML
    private TableColumn<TherapistTM, String> actionCol;

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<TherapistTM, String> nameCol;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<TherapistTM, String> phoneNoCol;

    @FXML
    private TextField phoneNoField;

    @FXML
    private TextField searchBar;

    @FXML
    private TableColumn<TherapistTM, String> specializationCol;

    @FXML
    private TextField specializationField;

    @FXML
    private TableView<TherapistTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private TableColumn<TherapistTM, String> therapistIdCol;

    @FXML
    private Label therapistIdLabel;

    @FXML
    private Button updateBtn;

    private ObservableList<TherapistTM> tableData;
    private final TherapistBO therapistBO = BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST);


    @FXML
    void addBtnOnAction(ActionEvent event) {

        String id = therapistIdLabel.getText();
        String name = nameField.getText();
        String phoneNo = phoneNoField.getText();
        String specialization = specializationField.getText();

        if(id.isEmpty() || name.isEmpty() || phoneNo.isEmpty() || specialization.isEmpty()){

            notification("Please Fill In All Field To Add A New Therapist");
            return;
        }

        boolean nameValidation = UserInputValidation.checkNameValidation(name);
        boolean phoneNoValidation = UserInputValidation.checkPhoneNoValidation(phoneNo);
        boolean specializationValidation = UserInputValidation.checkTextValidation(specialization);

        userInputValidate(nameValidation,phoneNoValidation,specializationValidation);

        if(nameValidation && phoneNoValidation && specializationValidation){

            TherapistDTO therapistDTO = new TherapistDTO(id,name,phoneNo,specialization,true);

            boolean isSave = therapistBO.add(therapistDTO);

            if(!isSave){
                notification("Something Went Wrong With Adding A New Therapist, Please Try Again Later");
                clean();
                return;
            }

            notification("Successfully Added The New Therapist");
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

        ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            therapistTMS.add(tableData.get(i));
        }

        table.setItems(therapistTMS);
    }

    @FXML
    void refreshOnMouseClick(MouseEvent event) {

        clean();
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {

        TherapistTM selectedTherapist = table.getSelectionModel().getSelectedItem();

        if(selectedTherapist==null){
            return;
        }

        String id = therapistIdLabel.getText();
        String name = nameField.getText();
        String phoneNo = phoneNoField.getText();
        String specialization = specializationField.getText();

        if(id.isEmpty() || name.isEmpty() || phoneNo.isEmpty() || specialization.isEmpty()){

            notification("All Fields Must Be Filled In To Update The Selected Therapist.");
            return;
        }

        boolean nameValidation = UserInputValidation.checkNameValidation(name);
        boolean phoneNoValidation = UserInputValidation.checkPhoneNoValidation(phoneNo);
        boolean specializationValidation = UserInputValidation.checkTextValidation(specialization);

        userInputValidate(nameValidation,phoneNoValidation,specializationValidation);

        if(nameValidation && phoneNoValidation && specializationValidation) {

            TherapistDTO therapistDTO = new TherapistDTO(id,name,phoneNo,specialization,true);

            boolean isUpdate = therapistBO.update(therapistDTO);

            if(isUpdate){
                notification("Successfully Updated Therapist ID: "+id);
            }
            else{
                notification("Failed To Update Therapist ID: "+id+" Please Try Again Later.");
            }

            clean();
        }
        else{
            notification("Please Enter Valid Data Into The Fields To Update A Therapist");
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

        String newId = therapistBO.generateNewId();
        therapistIdLabel.setText(newId);
    }


    public void setTableColumn(){

        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        specializationCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<TherapistTM, String>, TableCell<TherapistTM, String>> cellFoctory = (TableColumn<TherapistTM, String> param) -> {

            final TableCell<TherapistTM, String> cell = new TableCell<TherapistTM, String>() {
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

                            TherapistTM selectedItem = table.getSelectionModel().getSelectedItem();

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Delete The Therapist ID: "+selectedItem.getTherapistId(),ButtonType.YES,ButtonType.CANCEL);
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.YES)){

                                TherapistDTO therapistDTO = TMToDTO.ToTherapistDTO(selectedItem);
                                boolean isDelete = therapistBO.delete(therapistDTO);

                                if(isDelete){
                                    notification("Successfully Deleted The Therapy Program ID: "+selectedItem.getTherapistId());
                                }
                                else{
                                    notification("Failed Delete The Therapy Program ID: "+selectedItem.getTherapistId());
                                }
                            }

                            clean();

                        });


                        update.setOnMouseClicked((MouseEvent event) -> {

                            TherapistTM selectedTherapist = table.getSelectionModel().getSelectedItem();

                            therapistIdLabel.setText(selectedTherapist.getTherapistId());
                            nameField.setText(selectedTherapist.getName());
                            phoneNoField.setText(selectedTherapist.getPhoneNo());
                            specializationField.setText(selectedTherapist.getSpecialization());

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

        List<TherapistDTO> therapistDTOS = therapistBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(TherapistDTO x : therapistDTOS){
            tableData.add(DTOToTM.ToTherapistTM(x));
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

        FilteredList<TherapistTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(therapist -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (therapist.getTherapistId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapist.getName().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapist.getPhoneNo().contains(lowerCaseFilter)) {
                    return true;
                } else if (therapist.getSpecialization().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<TherapistTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void userInputValidate(boolean nameValidation,boolean phoneNoValidation,boolean specializationValidation){

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
        if(!specializationValidation){
            specializationField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            specializationField.setStyle("-fx-border-color: #CFD8DC");
        }
    }


    public void clean(){

        generateNewId();
        nameField.clear();
        phoneNoField.clear();
        specializationField.clear();
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
