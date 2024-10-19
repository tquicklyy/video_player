package org.player.videoplayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private BorderPane mainMenuBorderPane;

    @FXML
    private Label mainMenuFirstLabel;

    @FXML
    private Label mainMenuSecondLabel;

    @FXML
    private Label mainMenuThirdLabel;

    private Scene newScene;

    private Stage currentStage;

    @FXML
    private void switchingToTheVideoSelectionMenu(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
        currentStage.setScene(newScene);
    }

    @FXML
    void initialize() {
        mainMenuBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 530) {
                mainMenuFirstLabel.setPrefHeight(54);
                mainMenuFirstLabel.setPrefWidth(300);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 44));

                mainMenuSecondLabel.setPrefHeight(32);
                mainMenuSecondLabel.setPrefWidth(300);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

                mainMenuThirdLabel.setPrefHeight(32);
                mainMenuThirdLabel.setPrefWidth(300);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            } else if (newValue.intValue() < 660) {
                mainMenuFirstLabel.setPrefHeight(67);
                mainMenuFirstLabel.setPrefWidth(345);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 54));

                mainMenuSecondLabel.setPrefHeight(34);
                mainMenuSecondLabel.setPrefWidth(345);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15.4));

                mainMenuThirdLabel.setPrefHeight(34);
                mainMenuThirdLabel.setPrefWidth(345);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15.4));
            } else if (newValue.intValue() < 790) {
                mainMenuFirstLabel.setPrefHeight(78);
                mainMenuFirstLabel.setPrefWidth(390);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 64));

                mainMenuSecondLabel.setPrefHeight(36);
                mainMenuSecondLabel.setPrefWidth(390);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

                mainMenuThirdLabel.setPrefHeight(36);
                mainMenuThirdLabel.setPrefWidth(390);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));
            }  else if (newValue.intValue() < 920){
                mainMenuFirstLabel.setPrefHeight(89);
                mainMenuFirstLabel.setPrefWidth(445);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 74));

                mainMenuSecondLabel.setPrefHeight(38);
                mainMenuSecondLabel.setPrefWidth(445);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20.2));

                mainMenuThirdLabel.setPrefHeight(38);
                mainMenuThirdLabel.setPrefWidth(445);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20.2));
            } else if (newValue.intValue() < 1050){
                mainMenuFirstLabel.setPrefHeight(100);
                mainMenuFirstLabel.setPrefWidth(505);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 84));

                mainMenuSecondLabel.setPrefHeight(40);
                mainMenuSecondLabel.setPrefWidth(505);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22.6));

                mainMenuThirdLabel.setPrefHeight(40);
                mainMenuThirdLabel.setPrefWidth(505);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22.6));
            } else {
                mainMenuFirstLabel.setPrefHeight(111);
                mainMenuFirstLabel.setPrefWidth(565);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 94));

                mainMenuSecondLabel.setPrefHeight(42);
                mainMenuSecondLabel.setPrefWidth(565);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));

                mainMenuThirdLabel.setPrefHeight(42);
                mainMenuThirdLabel.setPrefWidth(565);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
            }
        });
    }

}
