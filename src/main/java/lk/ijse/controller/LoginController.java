package lk.ijse.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.client.Client;
import lk.ijse.dto.LoginDto;
import lk.ijse.model.LoginModel;
import lk.ijse.server.Server;
import lk.ijse.util.SystemAlert;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Label lblTime;

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;
    public static String username = "";
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Server serverSocket = Server.getServerSocket();
        Thread thread = new Thread(serverSocket);
        thread.start();
        loadDateandTime();
    }
    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, IOException {
        if (txtUserName.getText().isEmpty()||txtPassword.getText().isEmpty()){
            new SystemAlert(Alert.AlertType.WARNING,"warning!","please enter the all details! !", ButtonType.OK).show();
            return;
        }
        username = txtUserName.getText();
        String txtUserNameText = txtUserName.getText();
        String passwordText = txtPassword.getText();
       boolean check = new LoginModel().checkCredentional(new LoginDto(txtUserNameText,passwordText));
       if (check){
           Client client = new Client(txtUserNameText);
           txtPassword.clear();
           txtUserName.clear();

           new Thread(client).start();

       }else {
           new SystemAlert(Alert.AlertType.WARNING,"warning!","invalid username or password !", ButtonType.OK).show();
       }
    }
    @FXML
    void btnSignUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/signUp_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage =(Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SignUp Page");
        stage.centerOnScreen();
    }
    private void loadDateandTime() {


        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            DateTimeFormatter format2 = DateTimeFormatter.ofPattern("HH:mm");
            lblTime.setText(LocalTime.now().format(format2));
        }), new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


}
