package lk.ijse.controller;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lk.ijse.dto.LoginDto;
import lk.ijse.model.LoginModel;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private Label lblTime;

    @FXML
    private TextField txtUserMessage;

    @FXML
    private ImageView imgUserImg;

    @FXML
    private Label lblUserName;

    @FXML
    private ScrollPane ScrollPane;

    @FXML
    private VBox vBox;

    public static String username = "";

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username = LoginController.username;
        System.out.println("username: "+username);
        loadDateandTime();
        setDetails();
    }

    private void loadDateandTime() {
        /*Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(format.format(date));*/

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            DateTimeFormatter format2 = DateTimeFormatter.ofPattern("HH:mm");
            lblTime.setText(LocalTime.now().format(format2));
        }), new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setDetails(){
        try {
            LoginDto user = LoginModel.getUser(username);
            if (user != null) {
                lblUserName.setText(user.getUserName());
                System.out.println(username);
                if (user.getImg() != null){
                    imgUserImg.setImage(new Image(user.getImg()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
