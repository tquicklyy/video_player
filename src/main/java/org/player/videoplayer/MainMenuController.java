package org.player.videoplayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainMenuController {

    @FXML
    private BorderPane mainMenuBorderPane;

    @FXML
    private Label mainMenuFirstLabel;

    @FXML
    private Label mainMenuSecondLabel;

    @FXML
    private Label mainMenuThirdLabel;

    @FXML
    void initialize() {
        mainMenuBorderPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 598) {
                mainMenuFirstLabel.setPrefHeight(54);
                mainMenuFirstLabel.setPrefWidth(300);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 44));

                mainMenuSecondLabel.setPrefHeight(32);
                mainMenuSecondLabel.setPrefWidth(300);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

                mainMenuThirdLabel.setPrefHeight(32);
                mainMenuThirdLabel.setPrefWidth(300);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            } else if (newValue.intValue() < 758) {
                mainMenuFirstLabel.setPrefHeight(56);
                mainMenuFirstLabel.setPrefWidth(325);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 45));

                mainMenuSecondLabel.setPrefHeight(34);
                mainMenuSecondLabel.setPrefWidth(325);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                mainMenuThirdLabel.setPrefHeight(34);
                mainMenuThirdLabel.setPrefWidth(325);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            } else if (newValue.intValue() < 918) {
                mainMenuFirstLabel.setPrefHeight(58);
                mainMenuFirstLabel.setPrefWidth(350);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 45));

                mainMenuSecondLabel.setPrefHeight(36);
                mainMenuSecondLabel.setPrefWidth(350);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                mainMenuThirdLabel.setPrefHeight(36);
                mainMenuThirdLabel.setPrefWidth(350);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            }  else {
                mainMenuFirstLabel.setPrefHeight(60);
                mainMenuFirstLabel.setPrefWidth(375);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 46));

                mainMenuSecondLabel.setPrefHeight(38);
                mainMenuSecondLabel.setPrefWidth(375);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                mainMenuThirdLabel.setPrefHeight(38);
                mainMenuThirdLabel.setPrefWidth(375);
                mainMenuThirdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            }
        });
    }

}
