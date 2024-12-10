package org.player.videoplayer;

import javafx.application.Platform;
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

    @FXML
    private Label mainMenuFourthLabel;

    private Scene newScene;

    public static Stage currentStage;

    private VideoPlayerController videoPlayerControllerWhenSwitch;
    private InfoAboutPlayer infoAboutPlayerCurrentController;

    @FXML
    private void switchingToTheVideoSelectionMenu(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());

        VideoSelectionMenuController.currentController = fxmlLoader.getController();
        currentStage.setScene(newScene);
        Platform.runLater(() -> VideoSelectionMenuController.currentController.startSynchronize());
    }

    @FXML
    private void switchingToTheVideoPlayerScene(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-player-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());

        videoPlayerControllerWhenSwitch = fxmlLoader.getController();
        videoPlayerControllerWhenSwitch.previousScene = "main-menu-scene.fxml";
        currentStage.setScene(newScene);

        Platform.runLater(() -> {
            videoPlayerControllerWhenSwitch.videoPlayerSceneBackButton.setText("На главную");

            VideoPlayerController.trackInTimeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneTimeSlider.lookup(".track");
            VideoPlayerController.trackInVolumeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneVolumeSlider.lookup(".track");
            VideoPlayerController.thumbInTimeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneTimeSlider.lookup(".thumb");
            VideoPlayerController.thumbInVolumeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneVolumeSlider.lookup(".thumb");

            videoPlayerControllerWhenSwitch.updateSizes(newScene.getHeight());
            videoPlayerControllerWhenSwitch.openNewVideo();
        });
    }

    @FXML
    private void switchingToTheInfoAboutPlayer(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("info-about-player-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());

        infoAboutPlayerCurrentController = fxmlLoader.getController();
        currentStage.setScene(newScene);
        Platform.runLater(() -> infoAboutPlayerCurrentController.updateSizes(newScene.getWidth()));
    }

    @FXML
    void initialize() {
        mainMenuThirdLabel.prefHeightProperty().bind(mainMenuSecondLabel.prefHeightProperty());
        mainMenuThirdLabel.prefWidthProperty().bind(mainMenuSecondLabel.prefWidthProperty());
        mainMenuThirdLabel.fontProperty().bind(mainMenuSecondLabel.fontProperty());

        mainMenuFourthLabel.prefHeightProperty().bind(mainMenuSecondLabel.prefHeightProperty());
        mainMenuFourthLabel.prefWidthProperty().bind(mainMenuSecondLabel.prefWidthProperty());
        mainMenuFourthLabel.fontProperty().bind(mainMenuSecondLabel.fontProperty());

        mainMenuSecondLabel.prefWidthProperty().bind(mainMenuFirstLabel.prefWidthProperty());

        mainMenuBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 530) {
                mainMenuFirstLabel.setPrefHeight(54);
                mainMenuFirstLabel.setPrefWidth(300);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 44));

                mainMenuSecondLabel.setPrefHeight(32);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            } else if (newValue.intValue() < 660) {
                mainMenuFirstLabel.setPrefHeight(67);
                mainMenuFirstLabel.setPrefWidth(345);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 54));

                mainMenuSecondLabel.setPrefHeight(34);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15.4));
            } else if (newValue.intValue() < 790) {
                mainMenuFirstLabel.setPrefHeight(78);
                mainMenuFirstLabel.setPrefWidth(390);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 64));

                mainMenuSecondLabel.setPrefHeight(36);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));
            }  else if (newValue.intValue() < 920){
                mainMenuFirstLabel.setPrefHeight(89);
                mainMenuFirstLabel.setPrefWidth(445);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 74));

                mainMenuSecondLabel.setPrefHeight(38);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20.2));
            } else if (newValue.intValue() < 1050){
                mainMenuFirstLabel.setPrefHeight(100);
                mainMenuFirstLabel.setPrefWidth(505);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 84));

                mainMenuSecondLabel.setPrefHeight(40);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22.6));
            } else {
                mainMenuFirstLabel.setPrefHeight(111);
                mainMenuFirstLabel.setPrefWidth(565);
                mainMenuFirstLabel.setFont(Font.font("Arial", FontWeight.BOLD, 94));

                mainMenuSecondLabel.setPrefHeight(42);
                mainMenuSecondLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
            }
        });
    }

}