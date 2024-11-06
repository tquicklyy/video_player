package org.player.videoplayer;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VideoDownloadController {

    @FXML
    private Label videoDownloadSceneBackButton;

    @FXML
    private Label videoDownloadSceneOfflineModeButton;

    @FXML
    public Label videoDownloadSceneCentralLabel;

    @FXML
    private HBox videoDownloadSceneDownloadHBox;

    @FXML
    private ImageView videoDownloadSceneDownloadImageView;

    @FXML
    public Label videoDownloadSceneDownloadLabel;

    @FXML
    public ImageView videoDownloadSceneGifImageView;

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

    public Label getVideoDownloadSceneNameOfVideoLabel() {
        return videoDownloadSceneNameOfVideoLabel;
    }

    @FXML
    private Label videoDownloadSceneNameOfVideoLabel;

    @FXML
    private BorderPane videoDownloadSceneBorderPane;

    @FXML
    private VBox videoDownloadSceneInfoVBox;

    @FXML
    private Label videoDownloadSceneInfoTopLabel;

    public JFXTextArea getVideoDownloadSceneInfoTextArea() {
        return videoDownloadSceneInfoTextArea;
    }

    @FXML
    private JFXTextArea videoDownloadSceneInfoTextArea;

    @FXML
    private Label videoDownloadSceneCloseInfoButton;

    private static Stage currentStage;
    private static Scene currentScene;
    private static Scene newScene;

    public String linkForWatch;
    public String subject;
    public String topic;
    public String subtopic;

    VideoPlayerController videoPlayerControllerWhenSwitch;
    VideoDownloadController videoDownloadControllerCurrent;

    @FXML
    private void switchingToTheVideoSelectionMenu(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
        VideoSelectionMenuController videoSelectionMenuControllerWhenSwitch = fxmlLoader.getController();
        VideoSelectionMenuController.displayVBox(videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuFlowPane());

        videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuLeftComboBox().setDisable(false);
        videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuRightComboBox().setDisable(false);
        videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuLeftComboBoxPromptLabel().setOpacity(1);
        videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuRightComboBoxPromptLabel().setOpacity(1);
        videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuLeftComboBox().getItems().addAll(VideoSelectionMenuController.leftComboBox);
        videoSelectionMenuControllerWhenSwitch.getVideoSelectionMenuRightComboBox().getItems().addAll(VideoSelectionMenuController.rightComboBox);
        VideoSelectionMenuController.currentController = videoSelectionMenuControllerWhenSwitch;
        currentStage.setScene(newScene);
    }

    @FXML
    private void openInfo() {
        videoDownloadSceneInfoVBox.setVisible(true);
        videoDownloadSceneInfoVBox.setDisable(false);
        videoDownloadSceneTopHBox.setDisable(true);
        videoDownloadSceneRedVBox.setDisable(true);
        videoDownloadSceneRedVBox.setVisible(false);
        videoDownloadSceneTopHBox.setOpacity(0.4);
        videoDownloadSceneRedVBox.setOpacity(0.4);
        videoDownloadSceneRedVBox.setOpacity(0.4);
    }

    @FXML
    private void closeInfo() {
        videoDownloadSceneInfoVBox.setVisible(false);
        videoDownloadSceneInfoVBox.setDisable(true);
        videoDownloadSceneTopHBox.setDisable(false);
        videoDownloadSceneRedVBox.setDisable(false);
        videoDownloadSceneRedVBox.setVisible(true);
        videoDownloadSceneTopHBox.setOpacity(1);
        videoDownloadSceneRedVBox.setOpacity(1);
        videoDownloadSceneRedVBox.setOpacity(1);
    }

    @FXML
    private void openVideoInBrowser() throws URISyntaxException, IOException {
        URI uri = new URI(linkForWatch);
        Desktop.getDesktop().browse(uri);
    }

    @FXML
    private void startDownloadVideo() {
        if(videoDownloadSceneDownloadLabel.getText().equals("Отменить загрузку")) {
            Platform.runLater(() -> {
                videoDownloadSceneCentralLabel.setText("Видео не загружено");
                videoDownloadSceneDownloadLabel.setText("Остановка загрузки");
            });
            DBInteraction.threadsForDownload.get(subtopic).interrupt();
            DBInteraction.isVideoDownloading.put(subtopic, null);

        } else if(videoDownloadSceneDownloadLabel.getText().equals("Скачать видео")) {
            Thread threadForDownloadVideo = new Thread(() -> {
                Platform.runLater(() -> {
                    videoDownloadSceneOfflineModeButton.setVisible(false);
                    videoDownloadSceneOfflineModeButton.setDisable(true);
                    videoDownloadSceneCentralLabel.setText("Видео загружается");
                    videoDownloadSceneGifImageView.setVisible(true);
                    videoDownloadSceneDownloadLabel.setText("Отменить загрузку");
                });
                DBInteraction.videoDownloadController = this;
                DBInteraction.downloadVideo(subject, topic, subtopic);
                if(!Thread.currentThread().isInterrupted() && DBInteraction.isConn) {
                    DBInteraction.isVideoDownloading.put(subtopic, false);
                    Platform.runLater(() -> {
                        videoDownloadSceneCentralLabel.setText("Видео загружено");
                        videoDownloadSceneGifImageView.setVisible(false);
                        videoDownloadSceneDownloadLabel.setText("Перейти к просмотру");
                    });
                } else {
                    Platform.runLater(() -> {
                        if(!DBInteraction.isConn) {
                            videoDownloadSceneOfflineModeButton.setDisable(false);
                            videoDownloadSceneOfflineModeButton.setVisible(true);
                        }
                        DBInteraction.isVideoDownloading.put(subtopic, null);
                        videoDownloadSceneCentralLabel.setText("Видео не загружено");
                        videoDownloadSceneGifImageView.setVisible(false);
                        videoDownloadSceneDownloadLabel.setText("Скачать видео");
                    });
                }
            });
            DBInteraction.threadsForDownload.put(subtopic, threadForDownloadVideo);
            DBInteraction.isVideoDownloading.put(subtopic, true);
            DBInteraction.downloadScene.put(subtopic, videoDownloadSceneDownloadLabel.getScene());
            threadForDownloadVideo.start();
        } else if(videoDownloadSceneDownloadLabel.getText().equals("Перейти к просмотру")) {
            currentStage = (Stage)((videoDownloadSceneDownloadHBox).getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-player-scene.fxml"));
            try {
                newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            videoPlayerControllerWhenSwitch = fxmlLoader.getController();
            videoPlayerControllerWhenSwitch.setPreviousScene("video-selection-menu-scene.fxml");

            Scene finalNewScene = newScene;
            Platform.runLater(() -> {
                videoPlayerControllerWhenSwitch.setTrackInTimeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneTimeSlider().lookup(".track"));
                videoPlayerControllerWhenSwitch.setTrackInVolumeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneVolumeSlider().lookup(".track"));
                videoPlayerControllerWhenSwitch.setThumbInTimeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneTimeSlider().lookup(".thumb"));
                videoPlayerControllerWhenSwitch.setThumbInVolumeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneVolumeSlider().lookup(".thumb"));
                videoPlayerControllerWhenSwitch.updateSizes(finalNewScene.getHeight());
                videoPlayerControllerWhenSwitch.isEducationVideo = true;
                videoPlayerControllerWhenSwitch.urlOfVideo = new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic)).toURI().toString();
                videoPlayerControllerWhenSwitch.doDictionaryOfPathToVideosInCurrentDirectory(new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic)).getParent());
                videoPlayerControllerWhenSwitch.restartPlayer();
            });
            currentStage.setScene(newScene);
        }
    }

    @FXML
    void initialize() {
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
