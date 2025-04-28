package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.util.LogInData;
import com.example.ormcoursework.util.UserInputValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserProfileEditController {

    @FXML
    private TextField emailtxt;

    @FXML
    private Button exitFromEditbtn;

    @FXML
    private TextField fnametxt;

    @FXML
    private Button savebtn;

    @FXML
    private TextField unametxt;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOFactory.BOType.USER);


    @FXML
    void changePasswordOnMouseClick(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/changePassword.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    @FXML
    void exitFromEditBtnOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfile.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }

    @FXML
    void saveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        String userName = unametxt.getText();
        String name = fnametxt.getText();
        String email = emailtxt.getText();

        String[] ar = {userName,name,email};
        String[] data  = new String[0];
        String[] column = new String[0];

        for (int i = 0; i < ar.length; i++) {
            if(!ar[i].isEmpty()){

                if(i==0){
                    Boolean bool = UserInputValidation.checkUserNameValidation(ar[i]);
                    System.out.println(bool);
                    if(!bool) {
                        notification("Your user name not valid, user name should have at least 7 characters including special characters and numbers");
                        clear();
                        return;
                    }
                }
                else if(i==1){
                    Boolean bool = UserInputValidation.checkNameValidation(ar[i]);
                    System.out.println(bool);
                    if(!bool) {
                        notification("This name not valid, please enter your full name");
                        clear();
                        return;
                    }
                }
                else if(i==2){
                    Boolean bool = UserInputValidation.checkEmailValidation(ar[i]);
                    System.out.println(bool);
                    if(!bool) {
                        notification("This email not valid, please enter valid email address");
                        clear();
                        return;
                    }
                }
            }
        }


        for (int i = 0; i < ar.length; i++) {
            if(!ar[i].isEmpty()){
                data = grow(data,ar[i]);

                if(i==0){
                    column = grow(column,"userName");
                }
                else if(i==1){
                    column = grow(column,"fullName");
                }
                else if(i==2){
                    column = grow(column,"email");
                }
            }

        }

        int count = data.length;

        switch (count){


            case 1 -> {
                boolean result = userBO.changeUserDetailsOne(data, column);
                if(result){
                    notification("Successfully updated your profile");
                }
                else{
                    notification("something went wrong, failed to update your profile, try again later");
                }
                if(result && column[0].equals("userName")){
                    LogInData.setUserName(data[0]);
                }
            }
            case 2 -> {
                boolean result = userBO.changeUserDetailsTwo(data, column);
                if(result){
                    notification("Successfully updated your profile");
                }
                else{
                    notification("something went wrong, failed to update your profile, try again later");
                }
                int index = 0;
                boolean bool = false;
                for (int i = 0; i < column.length; i++) {
                    if(column[i].equals("userName")){
                        index = i;
                        bool = true;

                    }
                }
                if(result && bool){
                    LogInData.setUserName(data[index]);
                }
            }
            case 3 -> {
                boolean result = userBO.changeUserDetailsThree(data, column);
                if(result){
                    notification("Successfully updated your profile");
                }
                else{
                    notification("something went wrong, failed to update your profile, try again later");
                }

                int index = 0;
                boolean bool = false;
                for (int i = 0; i < column.length; i++) {
                    if(column[i].equals("userName")){
                        index = i;
                        bool = true;

                    }
                }
                if(result && bool){
                    LogInData.setUserName(data[index]);
                }
            }
//            case 4 -> {
//                String result = userBO.changeUserDetailsFour(data, column);
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, result);
//                alert.showAndWait();
//
//                int index = 0;
//                boolean bool = false;
//                for (int i = 0; i < column.length; i++) {
//                    if(column[i].equals("userName")){
//                        index = i;
//                        bool = true;
//
//                    }
//                }
//                if(result.equals("Successfully updated your profile") && bool){
//                    LogInData.setUserName(data[index]);
//                }
//            }
            default -> {
                return;
            }
        }

        clear();


    }



    public String[] grow(String[] ar,String x){

        String[] temp = new String[ar.length+1];

        for (int i = 0; i < ar.length; i++) {

            temp[i] = ar[i];
        }

        temp[temp.length-1] = x;
        ar = temp;
        return ar;

    }

    public void clear(){

        unametxt.setText("");
        fnametxt.setText("");
        emailtxt.setText("");
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
