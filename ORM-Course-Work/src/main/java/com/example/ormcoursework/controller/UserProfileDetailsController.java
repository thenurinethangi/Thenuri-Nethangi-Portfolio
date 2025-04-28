package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.util.LogInData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileDetailsController implements Initializable {

    @FXML
    private Label email;

    @FXML
    private Button exitbtn;

    @FXML
    private Label name;

    @FXML
    private Label admin;

    @FXML
    private Label userName;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @FXML
    void exitOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UserDTO userDTO = userBO.search(LogInData.getUserName());

        if(userDTO!=null) {
            userName.setText(userDTO.getUserName());
            name.setText(userDTO.getFullName());
            email.setText(userDTO.getEmail());
            admin.setText(userDTO.getRole());
        }
    }
}
