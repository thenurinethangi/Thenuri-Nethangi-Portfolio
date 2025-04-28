package com.example.ormcoursework.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HomePageAdminController implements Initializable {

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
    void reportOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/therapist-program-assignment.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void scheduleOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/schedule-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void therapistOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/therapist-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void therapyProgramOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/therapy-program-page.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            // Bind the size of anchorPane to mainPane dynamically
            anchorPane.prefWidthProperty().bind(mainPane.widthProperty());
            anchorPane.prefHeightProperty().bind(mainPane.heightProperty());

            mainPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            e.printStackTrace(); // Or handle it properly based on your project needs
        }

    }
}

