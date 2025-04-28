package com.example.ormcoursework.controller;

import com.example.ormcoursework.bo.BOFactory;
import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.util.LogInData;
import com.example.ormcoursework.util.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fullNameField;

    @FXML
    private Label logInBtn;

    @FXML
    private Label logoTitle;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField userNameField;

    @FXML
    private Button exitbtn;

    @FXML
    private ComboBox<String> roleCmb;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @FXML
    void logInBtnOnAction(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/sign-in.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().setAll(anchorPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void signUpBtnOnAction(ActionEvent event) {

        String userName = userNameField.getText();
        String password = passwordField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String role = roleCmb.getSelectionModel().getSelectedItem();

        if (userName.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty() || role==null){

            notification("No Field Can Be Empty!");
            return;
        }

        boolean userNameValidation = UserInputValidation.checkUserNameValidation(userName);
        boolean passwordValidation = UserInputValidation.checkPasswordValidation(password);
        boolean fullNameValidation = UserInputValidation.checkNameValidation(fullName);
        boolean emailValidation = UserInputValidation.checkEmailValidation(email);

        userInputValidate(userNameValidation,passwordValidation,fullNameValidation,emailValidation);

        if(userNameValidation && passwordValidation && fullNameValidation && emailValidation){

            String encryptedPassword = encryptPassword(password);
//            System.out.println(encryptedPassword);

            UserDTO userDTO = new UserDTO(userName,encryptedPassword,fullName,email,role);

            boolean isUserExist = userBO.isExist(userDTO);

            if(isUserExist){
                notification("This User Name Already exist Pleases Log In!");
                clean();
                return;
            }

            boolean isSave = userBO.add(userDTO);

            if(isSave){

                LogInData.setUserName(userName);
                FXMLLoader fxmlLoader = null;

                if(role.equals("Admin")){
                    fxmlLoader = new FXMLLoader(getClass().getResource("/view/home-page-admin.fxml"));
                }
                else if(role.equals("Receptionist")){
                    fxmlLoader = new FXMLLoader(getClass().getResource("/view/home-page-receptionist.fxml"));
                }

                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    bodyPane.getChildren().setAll(anchorPane);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setRolesCmbValues();
    }


    public void setRolesCmbValues(){

        ObservableList<String> roles = FXCollections.observableArrayList("Admin","Receptionist");
        roleCmb.setItems(roles);
    }


    public String encryptPassword(String password){

        return BCrypt.hashpw(password, BCrypt.gensalt(12));
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
        if(!fullNameValidation){
            fullNameField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            fullNameField.setStyle("-fx-border-color: #E0E0E0");
        }
        if(!emailValidation){
            emailField.setStyle("-fx-border-color: #EF9A9A");
        }
        else{
            emailField.setStyle("-fx-border-color: #E0E0E0");
        }
    }


    public void clean(){

        userNameField.clear();
        passwordField.clear();
        fullNameField.clear();
        emailField.clear();
        roleCmb.getSelectionModel().clearSelection();
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
