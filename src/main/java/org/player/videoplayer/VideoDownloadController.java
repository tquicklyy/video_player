package org.player.videoplayer;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
import java.util.HashMap;
import java.util.Objects;

public class VideoDownloadController {

    @FXML
    private Label videoDownloadSceneBackButton;

    @FXML
    private Label videoDownloadSceneOfflineModeButton;

    @FXML
    private ImageView videoDownloadSceneInfoButton;

    @FXML
    private HBox videoDownloadSceneTopHBox;

    @FXML
    private BorderPane videoDownloadSceneBorderPane;

    @FXML
    private VBox videoDownloadSceneInfoVBox;

    @FXML
    private Label videoDownloadSceneInfoTopLabel;

    @FXML
    public JFXTextArea videoDownloadSceneInfoTextArea;

    @FXML
    private Label videoDownloadSceneCloseInfoButton;

    @FXML
    public VBox videoDownloadSceneCentralVBox;
    
    private static Scene newScene;

    public String linkForWatch;
    public String subject;
    public String topic;

    public static HashMap<String, VideoDownloadController> videoDownloadControllerHashMap = new HashMap<>();
    public static HashMap<String, VBox> videoDownloadSceneRedVBoxHashMap = new HashMap<>();
    public static HashMap<String, Label> videoDownloadSceneNameOfVideoLabelHashMap = new HashMap<>();
    public static HashMap<String, Label> videoDownloadSceneCentralLabelHashMap = new HashMap<>();
    public static HashMap<String, HBox> videoDownloadSceneDownloadHBoxHashMap = new HashMap<>();
    public static HashMap<String, ImageView> videoDownloadSceneDownloadImageViewHashMap = new HashMap<>();
    public static HashMap<String, Label> videoDownloadSceneDownloadLabelHashMap = new HashMap<>();
    public static HashMap<String, ImageView> videoDownloadSceneGifImageViewHashMap = new HashMap<>();

    @FXML
    private void switchingToTheVideoSelectionMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), MainMenuController.currentStage.getScene().getWidth(), MainMenuController.currentStage.getScene().getHeight());

        VideoSelectionMenuController videoSelectionMenuControllerWhenSwitch = fxmlLoader.getController();
        VideoSelectionMenuController.displayVBox(videoSelectionMenuControllerWhenSwitch.videoSelectionMenuFlowPane);

        videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBox.setDisable(false);
        videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBox.setDisable(false);

        VideoSelectionMenuController.isLeftComboBoxUpdateWhileChangeScene = true;
        VideoSelectionMenuController.isRightComboBoxUpdateWhileChangeScene = true;

        videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBox.setValue(subject);
        videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBox.setValue(topic);

        videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBox.getItems().addAll(VideoSelectionMenuController.leftComboBox);
        videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBox.getItems().addAll(VideoSelectionMenuController.rightComboBox);

        VideoSelectionMenuController.currentController = videoSelectionMenuControllerWhenSwitch;
        MainMenuController.currentStage.setScene(newScene);

        Platform.runLater(() -> {
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBoxPromptLabel.setVisible(false);
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBoxPromptLabel.setVisible(false);
        });
    }

    @FXML
    private void offlineModeActivateDownloadScene(MouseEvent event) throws IOException {
        DBInteraction.isOfflineMode = true;
        if(DBInteraction.nameOfSubtopics != null) {
            for(Thread threadForVideoDownload: DBInteraction.threadsForDownload.values()) {
                threadForVideoDownload.interrupt();
                try {
                    threadForVideoDownload.join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        switchingToTheVideoSelectionMenuWhenOfflineModeActivate(event);
    }

    private void switchingToTheVideoSelectionMenuWhenOfflineModeActivate(MouseEvent event) throws IOException {
        VideoPlayerController.isEducationVideo = false;
        MainMenuController.currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), MainMenuController.currentStage.getScene().getWidth(), MainMenuController.currentStage.getScene().getHeight());
        VideoSelectionMenuController videoSelectionMenuControllerWhenSwitch = fxmlLoader.getController();

        VideoSelectionMenuController.currentController = videoSelectionMenuControllerWhenSwitch;
        MainMenuController.currentStage.setScene(newScene);

        Platform.runLater(() -> {
            videoSelectionMenuControllerWhenSwitch.offlineModeActivateSelectionScene();

            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuOfflineModeButton.setText("Автономный режим включён");
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuOfflineModeButton.setVisible(true);
        });
    }

    @FXML
    private void openInfo() {
        videoDownloadSceneInfoVBox.setVisible(true);
        videoDownloadSceneInfoVBox.setDisable(false);
        videoDownloadSceneTopHBox.setDisable(true);
        videoDownloadSceneCentralVBox.getChildren().getFirst().setDisable(true);
        videoDownloadSceneCentralVBox.getChildren().getFirst().setVisible(false);

        videoDownloadSceneTopHBox.setOpacity(0.4);
        videoDownloadSceneInfoButton.setOpacity(0.4);
        videoDownloadSceneCentralVBox.setOpacity(0.4);
        videoDownloadSceneCentralVBox.setOpacity(0.4);
    }

    @FXML
    private void closeInfo() {
        videoDownloadSceneInfoVBox.setVisible(false);
        videoDownloadSceneInfoVBox.setDisable(true);
        videoDownloadSceneTopHBox.setDisable(false);
        videoDownloadSceneCentralVBox.getChildren().getFirst().setDisable(false);
        videoDownloadSceneCentralVBox.getChildren().getFirst().setVisible(true);

        videoDownloadSceneTopHBox.setOpacity(1);
        videoDownloadSceneInfoButton.setOpacity(1);
        videoDownloadSceneCentralVBox.setOpacity(1);
        videoDownloadSceneCentralVBox.setOpacity(1);
    }

    private static void openVideoInBrowser(String linkForWatch) {
        try {
            URI uri = new URI(linkForWatch);
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            System.out.println("Ошибка открытия видео в браузере: " + e);
        }
    }

    private static void startDownloadVideo(String subject, String topic, String subtopic) {
        if(videoDownloadSceneDownloadLabelHashMap.get(subtopic).getText().equals("Отменить загрузку")) {
            videoDownloadSceneCentralLabelHashMap.get(subtopic).setText("Видео не загружено");
            videoDownloadSceneDownloadLabelHashMap.get(subtopic).setText("Остановка загрузки");

            DBInteraction.threadsForDownload.get(subtopic).interrupt();
            DBInteraction.isVideoDownloading.put(subtopic, null);

        } else if(videoDownloadSceneDownloadLabelHashMap.get(subtopic).getText().equals("Скачать видео")) {
            Thread threadForDownloadVideo = new Thread(() -> {
                Platform.runLater(() -> {
                    videoDownloadControllerHashMap.get(subtopic).videoDownloadSceneOfflineModeButton.setVisible(false);
                    videoDownloadControllerHashMap.get(subtopic).videoDownloadSceneOfflineModeButton.setDisable(true);

                    videoDownloadSceneCentralLabelHashMap.get(subtopic).setText("Видео загружается");
                    videoDownloadSceneDownloadLabelHashMap.get(subtopic).setText("Отменить загрузку");
                    videoDownloadSceneGifImageViewHashMap.get(subtopic).setVisible(true);
                });

                DBInteraction.downloadVideo(subject, topic, subtopic);

                if(!Thread.currentThread().isInterrupted() && DBInteraction.isConn) {
                    DBInteraction.isVideoDownloading.put(subtopic, false);

                    Platform.runLater(() -> {
                        videoDownloadSceneCentralLabelHashMap.get(subtopic).setText("Видео загружено");
                        videoDownloadSceneDownloadLabelHashMap.get(subtopic).setText("Перейти к просмотру");
                        videoDownloadSceneGifImageViewHashMap.get(subtopic).setVisible(false);
                    });
                } else {
                    Platform.runLater(() -> {
                        if(!DBInteraction.isConn) {
                            videoDownloadControllerHashMap.get(subtopic).videoDownloadSceneOfflineModeButton.setDisable(false);
                            videoDownloadControllerHashMap.get(subtopic).videoDownloadSceneOfflineModeButton.setVisible(true);
                        }
                        DBInteraction.isVideoDownloading.put(subtopic, null);
                        videoDownloadSceneCentralLabelHashMap.get(subtopic).setText("Видео не загружено");
                        videoDownloadSceneGifImageViewHashMap.get(subtopic).setVisible(false);
                        videoDownloadSceneDownloadLabelHashMap.get(subtopic).setText("Скачать видео");
                    });
                }
            });
            DBInteraction.threadsForDownload.put(subtopic, threadForDownloadVideo);
            DBInteraction.isVideoDownloading.put(subtopic, true);
            threadForDownloadVideo.start();
        } else if(videoDownloadSceneDownloadLabelHashMap.get(subtopic).getText().equals("Перейти к просмотру")) {
            VideoPlayerController.isEducationVideo = true;
            MainMenuController.currentStage = (Stage)((videoDownloadControllerHashMap.get(subtopic).videoDownloadSceneOfflineModeButton).getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-player-scene.fxml"));
            try {
                newScene = new Scene(fxmlLoader.load(), MainMenuController.currentStage.getScene().getWidth(), MainMenuController.currentStage.getScene().getHeight());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            VideoPlayerController videoPlayerControllerWhenSwitch = fxmlLoader.getController();
            videoPlayerControllerWhenSwitch.subtopic = subtopic;
            videoPlayerControllerWhenSwitch.previousScene = "video-selection-menu-scene.fxml";
            Scene finalNewScene = newScene;

            Platform.runLater(() -> {

                VideoPlayerController.trackInTimeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneTimeSlider.lookup(".track");
                VideoPlayerController.trackInVolumeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneVolumeSlider.lookup(".track");
                VideoPlayerController.thumbInTimeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneTimeSlider.lookup(".thumb");
                VideoPlayerController.thumbInVolumeSlider = videoPlayerControllerWhenSwitch.videoPlayerSceneVolumeSlider.lookup(".thumb");

                videoPlayerControllerWhenSwitch.videoPlayerSceneAnotherVideoButton.setDisable(true);
                videoPlayerControllerWhenSwitch.videoPlayerSceneAnotherVideoButton.setVisible(false);
                videoPlayerControllerWhenSwitch.updateSizes(finalNewScene.getHeight());
                File fileWithVideo = new File(String.format("./Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic));
                videoPlayerControllerWhenSwitch.urlOfVideo = fileWithVideo.toURI().toString();
                videoPlayerControllerWhenSwitch.doDictionaryOfPathToVideosInCurrentDirectory((fileWithVideo).getParent());
                videoPlayerControllerWhenSwitch.restartPlayer();
                videoPlayerControllerWhenSwitch.addListenerForStageFullScreenProperty();
                videoPlayerControllerWhenSwitch.isFullScreenListenerAdded = true;
                videoPlayerControllerWhenSwitch.videoPlayerSceneBackButton.setText("Список видео");
            });
            MainMenuController.currentStage.setScene(newScene);
        }
    }

    public static void createVBoxForDownload(String subject, String topic, String subtopic) {
        VBox videoDownloadSceneRedVBox = new VBox();
        Label videoDownloadSceneNameOfVideoLabel = new Label();
        Label videoDownloadSceneCentralLabel = new Label();
        HBox videoDownloadSceneDownloadHBox = new HBox();
        ImageView videoDownloadSceneDownloadImageView = new ImageView();
        Label videoDownloadSceneDownloadLabel = new Label();
        HBox videoDownloadSceneOnlineWatchHBox = new HBox();
        ImageView videoDownloadSceneOnlineWatchImageView = new ImageView();
        Label videoDownloadSceneOnlineWatchLabel = new Label();
        ImageView videoDownloadSceneGifImageView = new ImageView();

        videoDownloadSceneRedVBoxHashMap.put(subtopic, videoDownloadSceneRedVBox);
        videoDownloadSceneNameOfVideoLabelHashMap.put(subtopic, videoDownloadSceneNameOfVideoLabel);
        videoDownloadSceneCentralLabelHashMap.put(subtopic, videoDownloadSceneCentralLabel);
        videoDownloadSceneDownloadHBoxHashMap.put(subtopic, videoDownloadSceneDownloadHBox);
        videoDownloadSceneDownloadImageViewHashMap.put(subtopic, videoDownloadSceneDownloadImageView);
        videoDownloadSceneDownloadLabelHashMap.put(subtopic, videoDownloadSceneDownloadLabel);
        videoDownloadSceneGifImageViewHashMap.put(subtopic, videoDownloadSceneGifImageView);

        videoDownloadSceneRedVBox.getChildren().addAll(videoDownloadSceneNameOfVideoLabel, videoDownloadSceneCentralLabel, videoDownloadSceneDownloadHBox, videoDownloadSceneOnlineWatchHBox, videoDownloadSceneGifImageView);

        videoDownloadSceneRedVBox.setAlignment(Pos.CENTER);
        videoDownloadSceneRedVBox.setStyle("-fx-background-color: #9A9999; -fx-background-radius: 10px;");
        videoDownloadSceneRedVBox.setSpacing(15);
        videoDownloadSceneRedVBox.minWidthProperty().bind(videoDownloadSceneRedVBox.prefWidthProperty());
        videoDownloadSceneRedVBox.maxWidthProperty().bind(videoDownloadSceneRedVBox.prefWidthProperty());
        videoDownloadSceneRedVBox.minHeightProperty().bind(videoDownloadSceneRedVBox.prefHeightProperty());
        videoDownloadSceneRedVBox.minHeightProperty().bind(videoDownloadSceneRedVBox.prefHeightProperty());

        videoDownloadSceneCentralLabel.fontProperty().bind(videoDownloadSceneNameOfVideoLabel.fontProperty());
        videoDownloadSceneCentralLabel.setAlignment(Pos.CENTER);
        videoDownloadSceneCentralLabel.setText("Видео не загружено");

        videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        videoDownloadSceneNameOfVideoLabel.setAlignment(Pos.CENTER);
        videoDownloadSceneNameOfVideoLabel.setText(subtopic);

        videoDownloadSceneDownloadHBox.getChildren().addAll(videoDownloadSceneDownloadImageView, videoDownloadSceneDownloadLabel);
        videoDownloadSceneDownloadHBox.setAlignment(Pos.CENTER_LEFT);
        videoDownloadSceneDownloadHBox.setStyle("-fx-background-color: white; -fx-background-radius: 7px;");
        videoDownloadSceneDownloadHBox.maxWidthProperty().bind(videoDownloadSceneDownloadHBox.prefWidthProperty());
        videoDownloadSceneDownloadHBox.maxHeightProperty().bind(videoDownloadSceneDownloadHBox.prefHeightProperty());
        videoDownloadSceneDownloadHBox.setPadding(new Insets(0,5,0,5));
        videoDownloadSceneDownloadHBox.setCursor(Cursor.HAND);
        videoDownloadSceneDownloadHBox.setOnMousePressed(_ -> startDownloadVideo(subject, topic, subtopic));

        videoDownloadSceneOnlineWatchHBox.getChildren().addAll(videoDownloadSceneOnlineWatchImageView, videoDownloadSceneOnlineWatchLabel);
        videoDownloadSceneOnlineWatchHBox.setAlignment(Pos.CENTER_LEFT);
        videoDownloadSceneOnlineWatchHBox.styleProperty().bind(videoDownloadSceneDownloadHBox.styleProperty());
        videoDownloadSceneOnlineWatchHBox.maxWidthProperty().bind(videoDownloadSceneOnlineWatchHBox.prefWidthProperty());
        videoDownloadSceneOnlineWatchHBox.maxHeightProperty().bind(videoDownloadSceneOnlineWatchHBox.prefHeightProperty());
        videoDownloadSceneOnlineWatchHBox.prefWidthProperty().bind(videoDownloadSceneDownloadHBox.prefWidthProperty());
        videoDownloadSceneOnlineWatchHBox.prefHeightProperty().bind(videoDownloadSceneDownloadHBox.prefHeightProperty());
        videoDownloadSceneOnlineWatchHBox.setPadding(new Insets(0,5,0,5));
        videoDownloadSceneOnlineWatchHBox.setCursor(Cursor.HAND);
        videoDownloadSceneOnlineWatchHBox.setOnMousePressed(_ -> openVideoInBrowser(DBInteraction.videoUrl.get(subtopic)));

        videoDownloadSceneDownloadImageView.setPreserveRatio(true);
        videoDownloadSceneDownloadImageView.setImage(new Image(Objects.requireNonNull(VideoDownloadController.class.getResource("/images/download.png")).toString()));

        videoDownloadSceneOnlineWatchImageView.setPreserveRatio(true);
        videoDownloadSceneOnlineWatchImageView.fitWidthProperty().bind(videoDownloadSceneDownloadImageView.fitWidthProperty());
        videoDownloadSceneOnlineWatchImageView.fitHeightProperty().bind(videoDownloadSceneDownloadImageView.fitHeightProperty());
        videoDownloadSceneOnlineWatchImageView.setImage(new Image(Objects.requireNonNull(VideoDownloadController.class.getResource("/images/link.png")).toString()));

        videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", 14));
        videoDownloadSceneDownloadLabel.setAlignment(Pos.CENTER);
        videoDownloadSceneDownloadLabel.setText("Скачать видео");

        videoDownloadSceneOnlineWatchLabel.fontProperty().bind(videoDownloadSceneDownloadLabel.fontProperty());
        videoDownloadSceneOnlineWatchLabel.prefWidthProperty().bind(videoDownloadSceneDownloadLabel.prefWidthProperty());
        videoDownloadSceneOnlineWatchLabel.setAlignment(Pos.CENTER);
        videoDownloadSceneOnlineWatchLabel.setText("Посмотреть онлайн");

        videoDownloadSceneGifImageView.setPreserveRatio(true);
        videoDownloadSceneGifImageView.setImage(new Image(Objects.requireNonNull(VideoDownloadController.class.getResource("/images/loading-bar.gif")).toString()));
        videoDownloadSceneGifImageView.setVisible(false);
        videoDownloadSceneGifImageView.setDisable(true);
    }

    public void updateSizes(Number newValue) {
        if (newValue.intValue() < 600) {
            videoDownloadSceneTopHBox.setPrefHeight(39);

            videoDownloadSceneBackButton.setPrefWidth(132);
            videoDownloadSceneBackButton.setPrefHeight(27);
            videoDownloadSceneBackButton.setFont(Font.font("Arial Black", 14));

            videoDownloadSceneOfflineModeButton.setPrefWidth(255);

            for(VBox videoDownloadSceneRedVBox: VideoDownloadController.videoDownloadSceneRedVBoxHashMap.values()) {
                videoDownloadSceneRedVBox.setPrefWidth(450);
                videoDownloadSceneRedVBox.setPrefHeight(250);
            }

            for(Label videoDownloadSceneNameOfVideoLabel: VideoDownloadController.videoDownloadSceneNameOfVideoLabelHashMap.values()) {
                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            }

            for(Label videoDownloadSceneDownloadLabel: VideoDownloadController.videoDownloadSceneDownloadLabelHashMap.values()) {
                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                videoDownloadSceneDownloadLabel.setPrefWidth(210);
            }

            for(ImageView videoDownloadSceneDownloadImageView: VideoDownloadController.videoDownloadSceneDownloadImageViewHashMap.values()) {
                videoDownloadSceneDownloadImageView.setFitWidth(26);
                videoDownloadSceneDownloadImageView.setFitHeight(22);
            }

            for(HBox videoDownloadSceneDownloadHBox: VideoDownloadController.videoDownloadSceneDownloadHBoxHashMap.values()) {
                videoDownloadSceneDownloadHBox.setPrefWidth(233);
                videoDownloadSceneDownloadHBox.setPrefHeight(35);
            }

            for(ImageView videoDownloadSceneGifImageView: VideoDownloadController.videoDownloadSceneGifImageViewHashMap.values()) {
                videoDownloadSceneGifImageView.setFitWidth(154.4);
                videoDownloadSceneGifImageView.setFitHeight(17);
            }

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

            videoDownloadSceneOfflineModeButton.setPrefWidth(275);

            for(VBox videoDownloadSceneRedVBox: VideoDownloadController.videoDownloadSceneRedVBoxHashMap.values()) {
                videoDownloadSceneRedVBox.setPrefWidth(523);
                videoDownloadSceneRedVBox.setPrefHeight(290);
            }

            for(Label videoDownloadSceneNameOfVideoLabel: VideoDownloadController.videoDownloadSceneNameOfVideoLabelHashMap.values()) {
                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            }

            for(Label videoDownloadSceneDownloadLabel: VideoDownloadController.videoDownloadSceneDownloadLabelHashMap.values()) {
                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                videoDownloadSceneDownloadLabel.setPrefWidth(241);
            }

            for(ImageView videoDownloadSceneDownloadImageView: VideoDownloadController.videoDownloadSceneDownloadImageViewHashMap.values()) {
                videoDownloadSceneDownloadImageView.setFitWidth(28);
                videoDownloadSceneDownloadImageView.setFitHeight(24);
            }

            for(HBox videoDownloadSceneDownloadHBox: VideoDownloadController.videoDownloadSceneDownloadHBoxHashMap.values()) {
                videoDownloadSceneDownloadHBox.setPrefWidth(270);
                videoDownloadSceneDownloadHBox.setPrefHeight(37);
            }

            for(ImageView videoDownloadSceneGifImageView: VideoDownloadController.videoDownloadSceneGifImageViewHashMap.values()) {
                videoDownloadSceneGifImageView.setFitWidth(179);
                videoDownloadSceneGifImageView.setFitHeight(27);
            }

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

            videoDownloadSceneOfflineModeButton.setPrefWidth(295);

            for(VBox videoDownloadSceneRedVBox: VideoDownloadController.videoDownloadSceneRedVBoxHashMap.values()) {
                videoDownloadSceneRedVBox.setPrefWidth(596);
                videoDownloadSceneRedVBox.setPrefHeight(331);
            }

            for(Label videoDownloadSceneNameOfVideoLabel: VideoDownloadController.videoDownloadSceneNameOfVideoLabelHashMap.values()) {
                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            }

            for(Label videoDownloadSceneDownloadLabel: VideoDownloadController.videoDownloadSceneDownloadLabelHashMap.values()) {
                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                videoDownloadSceneDownloadLabel.setPrefWidth(269);
            }

            for(ImageView videoDownloadSceneDownloadImageView: VideoDownloadController.videoDownloadSceneDownloadImageViewHashMap.values()) {
                videoDownloadSceneDownloadImageView.setFitWidth(30);
                videoDownloadSceneDownloadImageView.setFitHeight(26);
            }

            for(HBox videoDownloadSceneDownloadHBox: VideoDownloadController.videoDownloadSceneDownloadHBoxHashMap.values()) {
                videoDownloadSceneDownloadHBox.setPrefWidth(308);
                videoDownloadSceneDownloadHBox.setPrefHeight(39);
            }

            for(ImageView videoDownloadSceneGifImageView: VideoDownloadController.videoDownloadSceneGifImageViewHashMap.values()) {
                videoDownloadSceneGifImageView.setFitWidth(204);
                videoDownloadSceneGifImageView.setFitHeight(29);
            }

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

            videoDownloadSceneOfflineModeButton.setPrefWidth(315);

            for(VBox videoDownloadSceneRedVBox: VideoDownloadController.videoDownloadSceneRedVBoxHashMap.values()) {
                videoDownloadSceneRedVBox.setPrefWidth(669);
                videoDownloadSceneRedVBox.setPrefHeight(371);
            }

            for(Label videoDownloadSceneNameOfVideoLabel: VideoDownloadController.videoDownloadSceneNameOfVideoLabelHashMap.values()) {
                videoDownloadSceneNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
            }

            for(Label videoDownloadSceneDownloadLabel: VideoDownloadController.videoDownloadSceneDownloadLabelHashMap.values()) {
                videoDownloadSceneDownloadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                videoDownloadSceneDownloadLabel.setPrefWidth(308);
            }

            for(ImageView videoDownloadSceneDownloadImageView: VideoDownloadController.videoDownloadSceneDownloadImageViewHashMap.values()) {
                videoDownloadSceneDownloadImageView.setFitWidth(32);
                videoDownloadSceneDownloadImageView.setFitHeight(28);
            }

            for(HBox videoDownloadSceneDownloadHBox: VideoDownloadController.videoDownloadSceneDownloadHBoxHashMap.values()) {
                videoDownloadSceneDownloadHBox.setPrefWidth(346);
                videoDownloadSceneDownloadHBox.setPrefHeight(41);
            }

            for(ImageView videoDownloadSceneGifImageView: VideoDownloadController.videoDownloadSceneGifImageViewHashMap.values()) {
                videoDownloadSceneGifImageView.setFitWidth(229);
                videoDownloadSceneGifImageView.setFitHeight(47);
            }

            videoDownloadSceneInfoVBox.setPrefWidth(679);
            videoDownloadSceneInfoVBox.setPrefHeight(327);

            videoDownloadSceneInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

            videoDownloadSceneInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 17));

            videoDownloadSceneCloseInfoButton.setPrefWidth(173);
            videoDownloadSceneCloseInfoButton.setPrefHeight(30);
            videoDownloadSceneCloseInfoButton.setFont(Font.font("Arial Black", 17));
        }
    }

    @FXML
    void initialize() {
        videoDownloadSceneOfflineModeButton.prefHeightProperty().bind(videoDownloadSceneBackButton.prefHeightProperty());
        videoDownloadSceneOfflineModeButton.fontProperty().bind(videoDownloadSceneBackButton.fontProperty());

        videoDownloadSceneInfoButton.fitWidthProperty().bind(videoDownloadSceneBackButton.heightProperty());
        videoDownloadSceneInfoButton.fitHeightProperty().bind(videoDownloadSceneBackButton.heightProperty());

        videoDownloadSceneBorderPane.heightProperty().addListener(( _, _, newValue) -> updateSizes(newValue));
    }

}
