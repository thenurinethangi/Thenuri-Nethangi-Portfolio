package com.example.ormcoursework.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageReceptionistController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    void logOutOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/sign-in.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void homeOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void medicalHistoryOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/medical-history-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void patientOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/patient-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void paymentOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/payment-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void registerOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/register-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void reportOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/report.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void sessionOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/session-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void therapyRecordOnMouseClick(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}










