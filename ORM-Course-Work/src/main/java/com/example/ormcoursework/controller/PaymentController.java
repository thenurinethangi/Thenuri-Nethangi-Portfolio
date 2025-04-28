package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.PaymentBO;
import com.example.ormcoursework.dto.PatientDTO;
import com.example.ormcoursework.dto.PaymentDTO;
import com.example.ormcoursework.util.DTOToTM;
import com.example.ormcoursework.util.PaymentMethod;
import com.example.ormcoursework.util.PaymentStatus;
import com.example.ormcoursework.util.TMToDTO;
import com.example.ormcoursework.view.tm.PatientTM;
import com.example.ormcoursework.view.tm.PaymentTM;
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

public class PaymentController implements Initializable {

    @FXML
    private TableColumn<PaymentTM, String> actionCol;

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<PaymentTM, BigDecimal> amountCol;

    @FXML
    private Label amountLabel;

    @FXML
    private TableColumn<PaymentTM, Date> dateCol;

    @FXML
    private TableColumn<PaymentTM, String> paymentIdCol;

    @FXML
    private Label paymentIdLabel;

    @FXML
    private TableColumn<PaymentTM, PaymentMethod> paymentMethodCol;

    @FXML
    private ComboBox<PaymentMethod> paymentTypeCmb;

    @FXML
    private ComboBox<String> pendingPaymentIdsCmb;

    @FXML
    private TextField searchBar;

    @FXML
    private TableColumn<PaymentTM, PaymentStatus> statusCol;

    @FXML
    private TableView<PaymentTM> table;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    private final PaymentBO paymentBO = BOFactory.getInstance().getBO(BOFactory.BOType.PAYMENT);
    private ObservableList<PaymentTM> tableData = FXCollections.observableArrayList();


    @FXML
    void addBtnOnAction(ActionEvent event) {

        String id = paymentIdLabel.getText();
        PaymentMethod paymentType = paymentTypeCmb.getSelectionModel().getSelectedItem();
        String pendingPaymentId = pendingPaymentIdsCmb.getSelectionModel().getSelectedItem();
        BigDecimal amount = new BigDecimal(amountLabel.getText());

        if(id.isEmpty() || paymentType==null || pendingPaymentId==null || amountLabel.getText().isEmpty()){

            notification("Please Fill In All Field To Add A Payment");
            return;
        }

        PaymentDTO paymentDTO = new PaymentDTO(id,new Date(),amount,paymentType,PaymentStatus.COMPLETE);
        boolean isSave = paymentBO.addLatePayment(paymentDTO,pendingPaymentId);

        if(isSave){
            notification("Successfully Added Payment For Pending Registration Payment Or Session Payment");
        }
        else{
            notification("Failed To Add A Payment, Please Try Again Later.");
        }

        clean();

    }

    @FXML
    void paymentTypeCmbOnAction(ActionEvent event) {

        PaymentMethod paymentMethod = paymentTypeCmb.getSelectionModel().getSelectedItem();

        if(paymentMethod==null){
            return;
        }

        if(paymentMethod==PaymentMethod.REGISTRATION_PAYMENT){

            List<String> registrationIds = paymentBO.getPendingRegistrationIds();
            ObservableList<String> idList = FXCollections.observableArrayList();

            idList.addAll(registrationIds);
            pendingPaymentIdsCmb.setItems(idList);

            if(idList.isEmpty()){
                notification("There Are Currently No Pending Payment Registrations.");
            }
        }
        else{

            List<String> sessionIds = paymentBO.getPendingSessionIds();
            ObservableList<String> idList = FXCollections.observableArrayList();

            idList.addAll(sessionIds);
            pendingPaymentIdsCmb.setItems(idList);

            if(idList.isEmpty()){
                notification("There Are Currently No Pending Payment Sessions.");
            }
        }
    }

    @FXML
    void pendingPaymentsCmbOnAction(ActionEvent event) {

        String selectedId = pendingPaymentIdsCmb.getSelectionModel().getSelectedItem();

        if(selectedId==null || paymentTypeCmb.getSelectionModel().getSelectedItem()==null){
            return;
        }

        if(paymentTypeCmb.getSelectionModel().getSelectedItem()==PaymentMethod.REGISTRATION_PAYMENT){
            BigDecimal amount = paymentBO.getUpFrontFee(selectedId);
            amountLabel.setText(String.valueOf(amount));
        }
        else if(paymentTypeCmb.getSelectionModel().getSelectedItem()==PaymentMethod.SESSION_PAYMENT){
            BigDecimal amount = paymentBO.getSessionFee(selectedId);
            amountLabel.setText(String.valueOf(amount));
        }

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

        ObservableList<PaymentTM> patientTMS = FXCollections.observableArrayList();

        for (int i = 0; i < selectedNo; i++) {
            patientTMS.add(tableData.get(i));
        }

        table.setItems(patientTMS);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewId();
        setTableColumn();
        loadTable();
        setTableRowCmbValues();
        setPaymentTypeCmbValues();
        setTableActionColumn();
        tableSearch();
        clean();
    }


    public void generateNewId(){

        String newId = paymentBO.generateNewId();
        paymentIdLabel.setText(newId);
    }


    public void setTableColumn(){

        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentMethodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    public void setTableActionColumn(){

        Callback<TableColumn<PaymentTM, String>, TableCell<PaymentTM, String>> cellFoctory = (TableColumn<PaymentTM, String> param) -> {

            final TableCell<PaymentTM, String> cell = new TableCell<PaymentTM, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/eye.png");
//                        Image image2 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/trash.png");
//                        Image image3 = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/edit.png");


                        ImageView show = new ImageView();
                        show.setImage(image1);
                        show.setFitHeight(18);
                        show.setFitWidth(19);

                        show.setStyle(" -fx-cursor: hand ;");


                        show.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("show");

                        });


                        HBox manageBtn = new HBox(show);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(4);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(show, new Insets(2, 2, 0, 3));
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

        List<PaymentDTO> paymentDTOS = paymentBO.getAll();
        tableData = FXCollections.observableArrayList();

        for(PaymentDTO x : paymentDTOS){
            tableData.add(DTOToTM.ToPaymentTM(x));
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


    public void setPaymentTypeCmbValues(){

        ObservableList<PaymentMethod> paymentMethods = FXCollections.observableArrayList(PaymentMethod.REGISTRATION_PAYMENT,PaymentMethod.SESSION_PAYMENT);
        paymentTypeCmb.setItems(paymentMethods);
    }


    public void tableSearch() {

        FilteredList<PaymentTM> filteredData = new FilteredList<>(tableData, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(payment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (payment.getPaymentId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(payment.getDate()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(payment.getAmount()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(payment.getPaymentMethod()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(payment.getStatus()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<PaymentTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void clean(){

        generateNewId();
        amountLabel.setText("");
        paymentTypeCmb.getSelectionModel().clearSelection();
        pendingPaymentIdsCmb.setItems(FXCollections.observableArrayList());
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
