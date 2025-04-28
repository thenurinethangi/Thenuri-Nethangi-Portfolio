package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.util.LogInData;
import com.example.ormcoursework.util.UserInputValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class SignInController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Button logInBtn;

    @FXML
    private Label logoTitle;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showPasswordField;

    @FXML
    private Label signUpBtn;

    @FXML
    private TextField userNameField;

    @FXML
    private Button exitbtn;

    @FXML
    private ImageView eyeImageView;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @FXML
    void logInBtnOnAction(ActionEvent event) {

        String userName = userNameField.getText();
        String password = passwordField.getText();

        if(userName.isEmpty() || password.isEmpty()){
            notification("No Field Can Be Empty!");
            return;
        }

        boolean userNameValidation = UserInputValidation.checkUserNameValidation(userName);

        if(!userNameValidation){
            notification("Not A Valid User Name!");
            return;
        }

        UserDTO userValidate = userBO.search(userName);

        if(userValidate==null){
            notification("This User Name Does Not Exist, Please Sign Up!");
            clean();
            return;
        }

        boolean isMatch = verifyPassword(password,userValidate.getPassword());

        if(!isMatch){
            notification("Password Is Incorrect!");
            return;
        }

        LogInData.setUserName(userName);
        FXMLLoader fxmlLoader = null;

        if(userValidate.getRole().equals("Admin")){
            fxmlLoader = new FXMLLoader(getClass().getResource("/view/home-page-admin.fxml"));
        }
        else if(userValidate.getRole().equals("Receptionist")){
            fxmlLoader = new FXMLLoader(getClass().getResource("/view/home-page-receptionist.fxml"));
        }

        try {
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().setAll(anchorPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void signUpBtnOnAction(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/sign-up.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            anchorPane.prefWidthProperty().bind(bodyPane.widthProperty());
            anchorPane.prefHeightProperty().bind(bodyPane.heightProperty());

            bodyPane.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void exitOnAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure You Want To Exist From The Application",ButtonType.YES,ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.YES)){

            System.exit(0);
        }
    }

    @FXML
    void eyeImageViewOnMouseClick(MouseEvent event) {

        Image img = eyeImageView.getImage();
//        System.out.println(img.getUrl());

        Image show = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/show (1).png");
        Image hide = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/invisible.png");


        if (img.getUrl().equals(show.getUrl())) {

            eyeImageView.setImage(hide);

            passwordField.setVisible(true);
            passwordField.setManaged(true);

            showPasswordField.setVisible(false);
            showPasswordField.setManaged(false);

            passwordField.setText(showPasswordField.getText());
        }
        else if(img.getUrl().equals(hide.getUrl())) {

            eyeImageView.setImage(show);

            showPasswordField.setVisible(true);
            showPasswordField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            String dotedPassword = passwordField.getText();
            showPasswordField.setText(dotedPassword);

        }

    }


    @FXML
    void showPasswordFieldOnKeyReleased(KeyEvent event) {

        passwordField.setText(showPasswordField.getText());
        //System.out.println(showPasswordField.getText());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showPasswordField.setVisible(false);
        showPasswordField.setManaged(false);

        Image image = new Image("file:/C:/Users/PCWORLD/IdeaProjects/ORM-Course-Work/src/main/resources/assets/image/invisible.png");
        eyeImageView.setImage(image);

    }


    public boolean verifyPassword(String textPassword,String hashPassword){

        return BCrypt.checkpw(textPassword,hashPassword);
    }



    public void userInputValidate(boolean userNameValidation,boolean passwordValidation,boolean fullNameValidation,boolean emailValidation){

        if(!userNameValidation){
            userNameField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            userNameField.setStyle("-fx-border-color: #E0E0E0");
        }
        if(!passwordValidation){
            passwordField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            passwordField.setStyle("-fx-border-color: #E0E0E0");
        }
    }


    public void clean(){

        userNameField.clear();
        passwordField.clear();
        showPasswordField.clear();

        showPasswordField.setVisible(false);
        showPasswordField.setManaged(false);

        passwordField.setVisible(true);
        passwordField.setManaged(true);
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
