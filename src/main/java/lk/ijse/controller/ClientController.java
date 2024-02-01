package lk.ijse.controller;


import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.client.Client;
import lk.ijse.dto.LoginDto;
import lk.ijse.model.LoginModel;
import javafx.scene.control.*;
import lk.ijse.server.ClientHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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
    private Client client;
    @FXML
    public AnchorPane emojiAnchorpane;

    @FXML
    public GridPane emojiGridpane;
    private final String[] emojis = {
            "\uD83D\uDE00", // 😀
            "\uD83D\uDE01", // 😁
            "\uD83D\uDE02", // 😂
            "\uD83D\uDE03", // 🤣
            "\uD83D\uDE04", // 😄
            "\uD83D\uDE05", // 😅
            "\uD83D\uDE06", // 😆
            "\uD83D\uDE07", // 😇
            "\uD83D\uDE08", // 😈
            "\uD83D\uDE09", // 😉
            "\uD83D\uDE0A", // 😊
            "\uD83D\uDE0B", // 😋
            "\uD83D\uDE0C", // 😌
            "\uD83D\uDE0D", // 😍
            "\uD83D\uDE0E", // 😎
            "\uD83D\uDE0F", // 😏
            "\uD83D\uDE10", // 😐
            "\uD83D\uDE11", // 😑
            "\uD83D\uDE12", // 😒
            "\uD83D\uDE13"  // 😓
    };



    private JFXButton createEmojiButton(String emoji) {
        JFXButton button = new JFXButton(emoji);
        button.getStyleClass().add("emoji-button");
        button.setOnAction(this::emojiButtonAction);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(button, true);
        GridPane.setFillHeight(button, true);
        button.setStyle("-fx-font-size: 15; -fx-text-fill: black; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
        return button;
    }
    @FXML
    private void emojiButtonAction(ActionEvent event) {
        JFXButton button = (JFXButton) event.getSource();
        txtUserMessage.appendText(button.getText());
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username = LoginController.username;
        System.out.println("username: "+username);
        loadDateandTime();
        setDetails();
        emojiAnchorpane.setVisible(false);
        int buttonIndex = 0;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                if (buttonIndex < emojis.length) {
                    String emoji = emojis[buttonIndex];
                    JFXButton emojiButton = createEmojiButton(emoji);
                    emojiGridpane.add(emojiButton, column, row);
                    buttonIndex++;
                } else {
                    break;
                }
            }
        }
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
    @FXML
    void btnAttachmentOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                HBox hBox = new HBox();
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");


                ImageView imageView = new ImageView(new Image(new FileInputStream(selectedFile)));
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(180);
                imageView.setFitWidth(100);

                hBox.getChildren().addAll(imageView);
                vBox.getChildren().add(hBox);

                client.sendImage(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @FXML
    void btnSendOnAction(ActionEvent event) throws IOException {
        String text = txtUserMessage.getText();
        if (text != null){
            sendMessage(text);
            try {
                client.sendMessage(text);
                txtUserMessage.clear();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong : server down").show();
            }
        } else{
            new Alert(Alert.AlertType.INFORMATION, "message is empty").show();
        }
    }

    private void sendMessage(String text) throws IOException {
        ClientHandler.broadcast(text);
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(text);
        messageLbl.setStyle("-fx-background-color:  #70e000;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vBox.getChildren().add(hBox);
        txtUserMessage.clear();
    }

    public void writeMessage(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #70e000;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setImage(byte[] bytes, String sender) {
        HBox hBox = new HBox();
        Label messageLbl = new Label(sender);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; " + (sender.equals(client.getName()) ? "-fx-alignment: center-right;" : "-fx-alignment: center-left;"));

        Platform.runLater(() -> {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(bytes)));
            imageView.setStyle("-fx-padding: 10px;");
            imageView.setFitHeight(180);
            imageView.setFitWidth(100);

            hBox.getChildren().addAll(messageLbl, imageView);
            vBox.getChildren().add(hBox);
        });
    }
    @FXML
    void testOnaction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"))));
        stage.setTitle("Login Form");
        stage.centerOnScreen();
        stage.show();
    }
}
