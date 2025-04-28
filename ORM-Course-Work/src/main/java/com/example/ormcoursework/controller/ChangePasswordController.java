package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.util.LogInData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ImageView eyeImageView1;

    @FXML
    private ImageView eyeImageView2;

    @FXML
    private PasswordField passwordField1;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @FXML
    void cancelOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void changePasswordOnAction(ActionEvent event) {

        String currentPassword = passwordField1.getText();
        String newPassword = passwordField2.getText();
        String confirmedPassword = confirmPasswordField.getText();

        UserDTO userDTO = userBO.search(LogInData.getUserName());

        boolean result = BCrypt.checkpw(currentPassword,userDTO.getPassword());
        System.out.println(result);

        if(!result){
            notification("Current Password Is Incorrect!");
            return;
        }

        if(!newPassword.equals(confirmedPassword)){
            notification("The confirmed password does not match the entered password. Please check your new password.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        UserDTO userDTO1 = new UserDTO(LogInData.getUserName(),hashedPassword);

        boolean isUpdate = userBO.updatePassword(userDTO1);

        if(!isUpdate){
            notification("Failed To Reset Password, Try Again Later");
        }
        else{
            notification("Successfully Reset The Password");
        }

        passwordField1.clear();
        passwordField2.clear();
        confirmPasswordField.clear();
        textField1.clear();
        textField2.clear();

    }

    @FXML
    void eyeImageViewOneOnMouseClick(MouseEvent event) {

        System.out.println("one");

        Image img = eyeImageView1.getImage();

        Image show = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/show (1).png");
        Image hide = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/invisible.png");


        if (img.getUrl().equals(show.getUrl())) {

            eyeImageView1.setImage(hide);

            passwordField1.setVisible(true);
            passwordField1.setManaged(true);

            textField1.setVisible(false);
            textField1.setManaged(false);

            passwordField1.setText(textField1.getText());
        }
        else if(img.getUrl().equals(hide.getUrl())) {

            eyeImageView1.setImage(show);

            textField1.setVisible(true);
            textField1.setManaged(true);

            passwordField1.setVisible(false);
            passwordField1.setManaged(false);

            String dotedPassword = passwordField1.getText();
            textField1.setText(dotedPassword);

        }
    }

    @FXML
    void eyeImageViewTwoOnMouseClick(MouseEvent event) {

        System.out.println("two");

        Image img = eyeImageView2.getImage();

        Image show = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/show (1).png");
        Image hide = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/invisible.png");


        if (img.getUrl().equals(show.getUrl())) {

            eyeImageView2.setImage(hide);

            passwordField2.setVisible(true);
            passwordField2.setManaged(true);

            textField2.setVisible(false);
            textField2.setManaged(false);

            passwordField2.setText(textField2.getText());
        }
        else if(img.getUrl().equals(hide.getUrl())) {

            eyeImageView2.setImage(show);

            textField2.setVisible(true);
            textField2.setManaged(true);

            passwordField2.setVisible(false);
            passwordField2.setManaged(false);

            String dotedPassword = passwordField2.getText();
            textField2.setText(dotedPassword);

        }
    }


    @FXML
    void showPasswordFieldOnKeyReleasedOne(KeyEvent event) {

        passwordField1.setText(textField1.getText());
    }

    @FXML
    void showPasswordFieldOnKeyReleasedTwo(KeyEvent event) {

        passwordField2.setText(textField2.getText());
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image hide = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/invisible.png");
        eyeImageView1.setImage(hide);
        eyeImageView2.setImage(hide);
    }
}




