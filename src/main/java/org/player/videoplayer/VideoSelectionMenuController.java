package org.player.videoplayer;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;


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
    private Label videoSelectionMenuBackButton;

    @FXML
    private HBox videoSelectionMenuTopHBox;

    @FXML
    private Label videoSelectionMenuLeftLabelUnderComboBox;

    @FXML
    private ComboBox<String> videoSelectionMenuRightComboBox;

    @FXML
    private Label videoSelectionMenuRightLabelForComboBox;

    @FXML
    private ImageView videoSelectionMenuUpdate;

    @FXML
    private ScrollPane videoSelectionMenuScrollPane;

    @FXML
    private ImageView videoSelectionMenuInfoButton;

    @FXML
    private VBox videoSelectionMenuInfoVBox;

    @FXML
    private Label videoSelectionMenuInfoTopLabel;

    @FXML
    private JFXTextArea videoSelectionMenuInfoTextArea;

    @FXML
    private Label videoSelectionMenuCloseInfoButton;

    @FXML
    private StackPane videoSelectionMenuStackPane;

    @FXML
    private Label videoSelectionMenuSynchLabel;

    @FXML
    private ImageView videoSelectionMenuSynchImageView;

    private static Stage currentStage;
    private static Scene currentScene;
    private static Scene newScene;

    @FXML
    private void switchingToTheMainMenu(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("main-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
        currentStage.setScene(newScene);
    }

    @FXML
    private void openInfo() {
        videoSelectionMenuInfoVBox.setVisible(true);
        videoSelectionMenuInfoVBox.setDisable(false);
        videoSelectionMenuBorderPane.setDisable(true);
    }

    @FXML
    private void closeInfo() {
        videoSelectionMenuInfoVBox.setVisible(false);
        videoSelectionMenuInfoVBox.setDisable(true);
        videoSelectionMenuBorderPane.setDisable(false);
    }

    @FXML
    void initialize() {
        videoSelectionMenuBorderPane.prefWidthProperty().bind(videoSelectionMenuStackPane.widthProperty());
        videoSelectionMenuBorderPane.prefHeightProperty().bind(videoSelectionMenuStackPane.heightProperty());

        videoSelectionMenuSynchImageView.fitWidthProperty().bind(videoSelectionMenuBackButton.heightProperty());
        videoSelectionMenuSynchImageView.fitHeightProperty().bind(videoSelectionMenuBackButton.heightProperty());

        videoSelectionMenuUpdate.fitWidthProperty().bind(videoSelectionMenuBackButton.heightProperty());
        videoSelectionMenuUpdate.fitHeightProperty().bind(videoSelectionMenuBackButton.heightProperty());

        videoSelectionMenuFlowPane.prefWidthProperty().bind(videoSelectionMenuScrollPane.widthProperty());
        videoSelectionMenuFlowPane.prefHeightProperty().bind(videoSelectionMenuScrollPane.heightProperty());

        videoSelectionMenuInfoButton.fitHeightProperty().bind(videoSelectionMenuImageViewForDelete.fitHeightProperty());
        videoSelectionMenuInfoButton.fitWidthProperty().bind(videoSelectionMenuImageViewForDelete.fitWidthProperty());

        videoSelectionMenuBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 600) {
                videoSelectionMenuTopHBox.setPrefHeight(39);

                videoSelectionMenuBackButton.setPrefWidth(132);
                videoSelectionMenuBackButton.setPrefHeight(27);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 14));

                videoSelectionMenuSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoSelectionMenuInfoVBox.setPrefWidth(518);
                videoSelectionMenuInfoVBox.setPrefHeight(250);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoSelectionMenuCloseInfoButton.setPrefWidth(132);
                videoSelectionMenuCloseInfoButton.setPrefHeight(27);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 14));
            } else if (newValue.intValue() < 800) {
                videoSelectionMenuTopHBox.setPrefHeight(40);

                videoSelectionMenuBackButton.setPrefWidth(171);
                videoSelectionMenuBackButton.setPrefHeight(28);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 15));

                videoSelectionMenuSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoSelectionMenuInfoVBox.setPrefWidth(572);
                videoSelectionMenuInfoVBox.setPrefHeight(276);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoSelectionMenuCloseInfoButton.setPrefWidth(146);
                videoSelectionMenuCloseInfoButton.setPrefHeight(28);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 15));
            } else if (newValue.intValue() < 950) {
                videoSelectionMenuTopHBox.setPrefHeight(41);

                videoSelectionMenuBackButton.setPrefWidth(210);
                videoSelectionMenuBackButton.setPrefHeight(29);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 16));

                videoSelectionMenuSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoSelectionMenuInfoVBox.setPrefWidth(626);
                videoSelectionMenuInfoVBox.setPrefHeight(302);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoSelectionMenuCloseInfoButton.setPrefWidth(160);
                videoSelectionMenuCloseInfoButton.setPrefHeight(29);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 16));
            } else {
                videoSelectionMenuTopHBox.setPrefHeight(42);

                videoSelectionMenuBackButton.setPrefWidth(250);
                videoSelectionMenuBackButton.setPrefHeight(30);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 17));

                videoSelectionMenuSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoSelectionMenuInfoVBox.setPrefWidth(679);
                videoSelectionMenuInfoVBox.setPrefHeight(327);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoSelectionMenuCloseInfoButton.setPrefWidth(173);
                videoSelectionMenuCloseInfoButton.setPrefHeight(30);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 17));
            }
        });

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

                videoSelectionMenuLabelForWatch.setPrefWidth(179);
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

                videoSelectionMenuLabelForWatch.setPrefWidth(268);
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

                videoSelectionMenuLabelForWatch.setPrefWidth(390);
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

                videoSelectionMenuLabelForWatch.setPrefWidth(613);
                videoSelectionMenuLabelForWatch.setPrefHeight(32);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

                videoSelectionMenuImageViewForDelete.setFitHeight(34);
                videoSelectionMenuImageViewForDelete.setFitWidth(40);
            }
        });
    }

}
