package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.util.LogInData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Button editbtn;

    @FXML
    private Label emailLabel;

    @FXML
    private Label nameLabel;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @FXML
    void editOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfileEdit.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfileDetails.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UserDTO userDTO = userBO.search(LogInData.getUserName());

        if(userDTO!=null){
            nameLabel.setText(userDTO.getFullName());
            emailLabel.setText(userDTO.getEmail());
        }

    }
}
