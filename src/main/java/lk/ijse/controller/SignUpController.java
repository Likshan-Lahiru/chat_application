package lk.ijse.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dto.LoginDto;
import lk.ijse.model.LoginModel;
import lk.ijse.util.SystemAlert;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    private File file;
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtVerifiedPassword;

    @FXML
    private Label lblTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateandTime();
    }

    @FXML
    void btnImageChooserOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtUserName.getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                imageView.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void btnCreateAccOnAction(ActionEvent event) throws FileNotFoundException, SQLException {
         if (txtPassword.getText()==txtVerifiedPassword.getText()){
             new SystemAlert(Alert.AlertType.WARNING,"Warning!","Password Not Match!", ButtonType.OK).show();
             return;

         }if (txtVerifiedPassword.getText().isEmpty()||txtPassword.getText().isEmpty()||txtUserName.getText().isEmpty()){
            new SystemAlert(Alert.AlertType.WARNING,"Warning!","Please Enter the All details!!", ButtonType.OK).show();
            return;
        }

        String txtUserNameText = txtUserName.getText();
        String passwordText = txtPassword.getText();
        InputStream inputStream = new FileInputStream(file);
       boolean isSaved = new LoginModel().createAcoount( new LoginDto(txtUserNameText,passwordText,inputStream));
       if (isSaved){

           new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation!","Account create success!", ButtonType.OK).show();
       }else {

           new SystemAlert(Alert.AlertType.ERROR,"Error!","Something Went Wrong! !", ButtonType.OK).show();
           return;
       }
        txtUserName.clear();
        txtPassword.clear();
        txtVerifiedPassword.clear();

        imageView.setImage(new Image(new FileInputStream(file)));

    }

    @FXML
    void btnSignInOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
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
