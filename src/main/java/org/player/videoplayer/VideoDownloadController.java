package org.player.videoplayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VideoDownloadController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label videoDownloadSceneBackButton;

    @FXML
    private Label videoDownloadSceneCentralLabel;

    @FXML
    private HBox videoDownloadSceneDownloadHBox;

    @FXML
    private ImageView videoDownloadSceneDownloadImageView;

    @FXML
    private Label videoDownloadSceneDownloadLabel;

    @FXML
    private ImageView videoDownloadSceneGifImageView;

    @FXML
    private VBox videoDownloadSceneRedVBox;

    @FXML
    private ImageView videoDownloadSceneInfoButton;

    @FXML
    private HBox videoDownloadSceneOnlineWatchHBox;

    @FXML
    private ImageView videoDownloadSceneOnlineWatchImageView;

    @FXML
    private Label videoDownloadSceneOnlineWatchLabel;

    @FXML
    private HBox videoDownloadSceneTopHBox;

    @FXML
    private Label videoDownloadSceneNameOfVideoLabel;

    @FXML
    private BorderPane videoDownloadSceneBorderPane;

    @FXML
    private VBox videoDownloadSceneInfoVBox;

    @FXML
    private Label videoDownloadSceneInfoTopLabel;

    @FXML
    private JFXTextArea videoDownloadSceneInfoTextArea;

    @FXML
    private Label videoDownloadSceneCloseInfoButton;

    @FXML
    private Label videoDownloadSceneSynchLabel;

    @FXML
    private ImageView videoDownloadSceneUpdate;

    @FXML
    private ImageView videoDownloadSceneSynchImageView;

    private static Stage currentStage;
    private static Scene currentScene;
    private static Scene newScene;

    @FXML
    private void switchingToTheVideoSelectionMenu(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
        currentStage.setScene(newScene);
    }

    @FXML
    private void openInfo() {
        videoDownloadSceneInfoVBox.setVisible(true);
        videoDownloadSceneInfoVBox.setDisable(false);
        videoDownloadSceneTopHBox.setDisable(true);
        videoDownloadSceneRedVBox.setDisable(true);
        videoDownloadSceneRedVBox.setVisible(false);
    }

    @FXML
    private void closeInfo() {
        videoDownloadSceneInfoVBox.setVisible(false);
        videoDownloadSceneInfoVBox.setDisable(true);
        videoDownloadSceneTopHBox.setDisable(false);
        videoDownloadSceneRedVBox.setDisable(false);
        videoDownloadSceneRedVBox.setVisible(true);
    }

    @FXML
    void initialize() {
        videoDownloadSceneSynchImageView.fitWidthProperty().bind(videoDownloadSceneBackButton.heightProperty());
        videoDownloadSceneSynchImageView.fitHeightProperty().bind(videoDownloadSceneBackButton.heightProperty());

        videoDownloadSceneUpdate.fitWidthProperty().bind(videoDownloadSceneBackButton.heightProperty());
        videoDownloadSceneUpdate.fitHeightProperty().bind(videoDownloadSceneBackButton.heightProperty());

        videoDownloadSceneInfoButton.fitWidthProperty().bind(videoDownloadSceneBackButton.heightProperty());
        videoDownloadSceneInfoButton.fitHeightProperty().bind(videoDownloadSceneBackButton.heightProperty());

        videoDownloadSceneCentralLabel.fontProperty().bind(videoDownloadSceneNameOfVideoLabel.fontProperty());

        videoDownloadSceneOnlineWatchLabel.fontProperty().bind(videoDownloadSceneDownloadLabel.fontProperty());
        videoDownloadSceneOnlineWatchLabel.prefWidthProperty().bind(videoDownloadSceneDownloadLabel.prefWidthProperty());

        videoDownloadSceneOnlineWatchImageView.fitWidthProperty().bind(videoDownloadSceneDownloadImageView.fitWidthProperty());
        videoDownloadSceneOnlineWatchImageView.fitHeightProperty().bind(videoDownloadSceneDownloadImageView.fitHeightProperty());

        videoDownloadSceneOnlineWatchHBox.prefWidthProperty().bind(videoDownloadSceneDownloadHBox.prefWidthProperty());
        videoDownloadSceneOnlineWatchHBox.prefHeightProperty().bind(videoDownloadSceneDownloadHBox.prefHeightProperty());

        videoDownloadSceneBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 600) {
                videoDownloadSceneTopHBox.setPrefHeight(39);

                videoDownloadSceneBackButton.setPrefWidth(132);
                videoDownloadSceneBackButton.setPrefHeight(27);
                videoDownloadSceneBackButton.setFont(Font.font("Arial Black", 14));

                videoDownloadSceneSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoDownloadSceneRedVBox.setPrefWidth(450);
                videoDownloadSceneRedVBox.setPrefHeight(250);

                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                videoDownloadSceneDownloadLabel.setPrefWidth(210);

                videoDownloadSceneDownloadImageView.setFitWidth(26);
                videoDownloadSceneDownloadImageView.setFitHeight(22);

                videoDownloadSceneDownloadHBox.setPrefWidth(233);
                videoDownloadSceneDownloadHBox.setPrefHeight(35);

                videoDownloadSceneGifImageView.setFitWidth(154.4);
                videoDownloadSceneGifImageView.setFitHeight(17);

                videoDownloadSceneInfoVBox.setPrefWidth(518);
                videoDownloadSceneInfoVBox.setPrefHeight(250);

                videoDownloadSceneInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoDownloadSceneInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoDownloadSceneCloseInfoButton.setPrefWidth(132);
                videoDownloadSceneCloseInfoButton.setPrefHeight(27);
                videoDownloadSceneCloseInfoButton.setFont(Font.font("Arial Black", 14));
            } else if (newValue.intValue() < 800) {
                videoDownloadSceneTopHBox.setPrefHeight(40);

                videoDownloadSceneBackButton.setPrefWidth(171);
                videoDownloadSceneBackButton.setPrefHeight(28);
                videoDownloadSceneBackButton.setFont(Font.font("Arial Black", 15));

                videoDownloadSceneSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoDownloadSceneRedVBox.setPrefWidth(523);
                videoDownloadSceneRedVBox.setPrefHeight(290);

                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                videoDownloadSceneDownloadLabel.setPrefWidth(241);

                videoDownloadSceneDownloadImageView.setFitWidth(28);
                videoDownloadSceneDownloadImageView.setFitHeight(24);

                videoDownloadSceneDownloadHBox.setPrefWidth(270);
                videoDownloadSceneDownloadHBox.setPrefHeight(37);

                videoDownloadSceneGifImageView.setFitWidth(179);
                videoDownloadSceneGifImageView.setFitHeight(27);

                videoDownloadSceneInfoVBox.setPrefWidth(572);
                videoDownloadSceneInfoVBox.setPrefHeight(276);

                videoDownloadSceneInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoDownloadSceneInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoDownloadSceneCloseInfoButton.setPrefWidth(146);
                videoDownloadSceneCloseInfoButton.setPrefHeight(28);
                videoDownloadSceneCloseInfoButton.setFont(Font.font("Arial Black", 15));
            } else if (newValue.intValue() < 950) {
                videoDownloadSceneTopHBox.setPrefHeight(41);

                videoDownloadSceneBackButton.setPrefWidth(210);
                videoDownloadSceneBackButton.setPrefHeight(29);
                videoDownloadSceneBackButton.setFont(Font.font("Arial Black", 16));

                videoDownloadSceneSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoDownloadSceneRedVBox.setPrefWidth(596);
                videoDownloadSceneRedVBox.setPrefHeight(331);

                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                videoDownloadSceneDownloadLabel.setPrefWidth(269);

                videoDownloadSceneDownloadImageView.setFitWidth(30);
                videoDownloadSceneDownloadImageView.setFitHeight(26);

                videoDownloadSceneDownloadHBox.setPrefWidth(308);
                videoDownloadSceneDownloadHBox.setPrefHeight(39);

                videoDownloadSceneGifImageView.setFitWidth(204);
                videoDownloadSceneGifImageView.setFitHeight(29);

                videoDownloadSceneInfoVBox.setPrefWidth(626);
                videoDownloadSceneInfoVBox.setPrefHeight(302);

                videoDownloadSceneInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoDownloadSceneInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoDownloadSceneCloseInfoButton.setPrefWidth(160);
                videoDownloadSceneCloseInfoButton.setPrefHeight(29);
                videoDownloadSceneCloseInfoButton.setFont(Font.font("Arial Black", 16));
            } else {
                videoDownloadSceneTopHBox.setPrefHeight(42);

                videoDownloadSceneBackButton.setPrefWidth(250);
                videoDownloadSceneBackButton.setPrefHeight(30);
                videoDownloadSceneBackButton.setFont(Font.font("Arial Black", 17));

                videoDownloadSceneSynchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoDownloadSceneRedVBox.setPrefWidth(669);
                videoDownloadSceneRedVBox.setPrefHeight(371);

                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                videoDownloadSceneDownloadLabel.setPrefWidth(308);

                videoDownloadSceneDownloadImageView.setFitWidth(32);
                videoDownloadSceneDownloadImageView.setFitHeight(28);

                videoDownloadSceneDownloadHBox.setPrefWidth(346);
                videoDownloadSceneDownloadHBox.setPrefHeight(41);

                videoDownloadSceneGifImageView.setFitWidth(229);
                videoDownloadSceneGifImageView.setFitHeight(47);

                videoDownloadSceneInfoVBox.setPrefWidth(679);
                videoDownloadSceneInfoVBox.setPrefHeight(327);

                videoDownloadSceneInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoDownloadSceneInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoDownloadSceneCloseInfoButton.setPrefWidth(173);
                videoDownloadSceneCloseInfoButton.setPrefHeight(30);
                videoDownloadSceneCloseInfoButton.setFont(Font.font("Arial Black", 17));
            }
        });
    }

}
