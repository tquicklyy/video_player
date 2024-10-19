package org.player.videoplayer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VideoSelectionMenuController {

    @FXML
    private BorderPane videoSelectionMenuBorderPane;

    @FXML
    private FlowPane videoSelectionMenuFlowPane;

    @FXML
    private Label videoSelectionMenuNameOfVideoLabel;

    @FXML
    private Label videoSelectionMenuLabelForWatch;

    @FXML
    private ImageView videoSelectionMenuImageViewForDelete;

    @FXML
    private ImageView videoSelectionMenuImageView;

    @FXML
    private ComboBox<String> videoSelectionMenuLeftComboBox;

    @FXML
    private Label videoSelectionMenuLeftLabelUnderComboBox;

    @FXML
    private ComboBox<String> videoSelectionMenuRightComboBox;

    @FXML
    private Label videoSelectionMenuRightLabelForComboBox;

    @FXML
    private ScrollPane videoSelectionMenuScrollPane;


    @FXML
    void initialize() {
        videoSelectionMenuFlowPane.prefWidthProperty().bind(videoSelectionMenuScrollPane.widthProperty());
        videoSelectionMenuFlowPane.prefHeightProperty().bind(videoSelectionMenuScrollPane.heightProperty());

        videoSelectionMenuBorderPane.widthProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 978) {
                videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 13));

                videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 13));

                videoSelectionMenuLeftComboBox.setPrefWidth(246);
                videoSelectionMenuLeftComboBox.setPrefHeight(27);
                videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 13px;");

                videoSelectionMenuRightComboBox.setPrefWidth(246);
                videoSelectionMenuRightComboBox.setPrefHeight(27);
                videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 13px;");

                videoSelectionMenuImageView.setFitHeight(150);
                videoSelectionMenuImageView.setFitWidth(280);

                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

                videoSelectionMenuLabelForWatch.setPrefWidth(214);
                videoSelectionMenuLabelForWatch.setPrefHeight(30);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 13));

                videoSelectionMenuImageViewForDelete.setFitHeight(31);
                videoSelectionMenuImageViewForDelete.setFitWidth(37);

            } else if (newValue.intValue() < 1251) {
                videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));

                videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));

                videoSelectionMenuLeftComboBox.setPrefWidth(371);
                videoSelectionMenuLeftComboBox.setPrefHeight(32);
                videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 14.6px;");

                videoSelectionMenuRightComboBox.setPrefWidth(371);
                videoSelectionMenuRightComboBox.setPrefHeight(32);
                videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 14.6px;");

                videoSelectionMenuImageView.setFitHeight(206);
                videoSelectionMenuImageView.setFitWidth(358);

                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));

                videoSelectionMenuLabelForWatch.setPrefWidth(297);
                videoSelectionMenuLabelForWatch.setPrefHeight(30);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));

                videoSelectionMenuImageViewForDelete.setFitHeight(32);
                videoSelectionMenuImageViewForDelete.setFitWidth(38);
            } else if (newValue.intValue() < 1624) {
                videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));

                videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));

                videoSelectionMenuLeftComboBox.setPrefWidth(496);
                videoSelectionMenuLeftComboBox.setPrefHeight(34.4);
                videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 16.2px;");

                videoSelectionMenuRightComboBox.setPrefWidth(496);
                videoSelectionMenuRightComboBox.setPrefHeight(34.4);
                videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 16.2px;");

                videoSelectionMenuImageView.setFitHeight(278);
                videoSelectionMenuImageView.setFitWidth(482);

                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));

                videoSelectionMenuLabelForWatch.setPrefWidth(418);
                videoSelectionMenuLabelForWatch.setPrefHeight(31);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));

                videoSelectionMenuImageViewForDelete.setFitHeight(33);
                videoSelectionMenuImageViewForDelete.setFitWidth(39);
            } else {
                videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

                videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

                videoSelectionMenuLeftComboBox.setPrefWidth(621);
                videoSelectionMenuLeftComboBox.setPrefHeight(36.8);
                videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 17.8px;");

                videoSelectionMenuRightComboBox.setPrefWidth(621);
                videoSelectionMenuRightComboBox.setPrefHeight(36.8);
                videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 17.8px;");

                videoSelectionMenuImageView.setFitHeight(418);
                videoSelectionMenuImageView.setFitWidth(707);

                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

                videoSelectionMenuLabelForWatch.setPrefWidth(648);
                videoSelectionMenuLabelForWatch.setPrefHeight(32);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

                videoSelectionMenuImageViewForDelete.setFitHeight(34);
                videoSelectionMenuImageViewForDelete.setFitWidth(40);
            }
        });
    }

}
