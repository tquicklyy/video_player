package org.player.videoplayer;

import com.jfoenix.controls.JFXSlider;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayerController {

    @FXML
    public Label videoPlayerSceneAnotherVideoButton;

    @FXML
    public Label videoPlayerSceneBackButton;

    @FXML
    private Label videoPlayerSceneCurrentTimeLabel;

    @FXML
    private Label videoPlayerSceneFullTimeLabel;

    @FXML
    private MediaView videoPlayerSceneMediaView;

    @FXML
    private ImageView videoPlayerSceneNextVideoImageView;

    @FXML
    private ImageView videoPlayerScenePreviousVideoImageView;

    @FXML
    private StackPane videoPlayerSceneStackPane;

    @FXML
    private ImageView videoPlayerSceneStopPlayImageView;

    @FXML
    private ImageView videoPlayerSceneTenSecondsBackImageView;

    @FXML
    private ImageView videoPlayerSceneTenSecondsForwardImageView;

    @FXML
    public JFXSlider videoPlayerSceneTimeSlider;

    @FXML
    private ImageView videoPlayerSceneVolumeImageView;

    @FXML
    private Label videoPlayerSceneVolumeLabel;

    @FXML
    public JFXSlider videoPlayerSceneVolumeSlider;

    @FXML
    private ImageView videoPlayerSceneWrapUnwrapImageView;

    @FXML
    private BorderPane videoPlayerSceneBorderPane;

    @FXML
    private ImageView videoPlayerSceneSynchronizeImageView;

    @FXML
    private HBox videoPlayerSceneTopHBox;

    @FXML
    private VBox videoPlayerSceneBotVBox;

    public String previousScene;

    public String urlOfVideo;

    private static int attemptsDownloadVideoIfError = 0;
    private static final int MAX_ATTEMPTS_DOWNLOAD_VIDEO_IF_ERROR = 100;

    private Media mediaOfVideo;
    private MediaPlayer mediaPlayerOfVideo;

    Duration totalDurationOfVideo;

    private boolean isVideoPlayed = true;
    private boolean isVolumeOff = true;
    private boolean isFullScreen = false;
    public boolean isFullScreenListenerAdded = false;
    private boolean isPauseForDisposeNow = false;
    private boolean isSeekingTime = false;
    private boolean isVideoHasHours;
    private boolean isBackButton;

    private double lastVolumeValueWhenUserOffVolume;

    private static HashMap<Integer, String> dictionaryOfPathToVideosInCurrentDirectory;

    private static int lastNumberOfDictionary = 0;

    private static int currentKeyOfVideoForDictionary;

    private static double videoPlayerSceneTopHBoxHeightBeforeWrap;
    private static double videoPlayerSceneBackButtonHeightBeforeWrap;
    private static double videoPlayerSceneCurrentTimeLabelHeightBeforeWrap;
    private static double videoPlayerSceneTimeSliderHeightBeforeWrap;
    private static double videoPlayerSceneFullTimeLabelHeightBeforeWrap;
    private static double videoPlayerSceneVolumeImageViewHeightBeforeWrap;
    private static double videoPlayerSceneVolumeSliderHeightBeforeWrap;
    private static double videoPlayerSceneVolumeLabelHeightBeforeWrap;
    private static double videoPlayerSceneBotVBoxHeightBeforeWrap;
    private static double videoPlayerSceneVolumeImageViewWidthBeforeWrap;

    private Font videoPlayerSceneCurrentTimeLabelFontBeforeWrap;

    public static Node trackInTimeSlider;
    public static Node trackInVolumeSlider;
    public static Node thumbInTimeSlider;
    public static Node thumbInVolumeSlider;

    private static double topAnchorWhenFullScreen = 33.6;

    private Timer timerForFullScreenWhenUserMovedMouse = new Timer();
    private PauseTransition pauseForDispose;

    public static boolean isEducationVideo;

    public String subtopic;

    @FXML
    private void switchingToTheBackScene() throws IOException {
        isBackButton = true;
        isEducationVideo = false;

        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource(previousScene));
        Scene newScene = new Scene(fxmlLoader.load(), MainMenuController.currentStage.getScene().getWidth(), MainMenuController.currentStage.getScene().getHeight());
        if(previousScene.equals("video-selection-menu-scene.fxml")) {
            videoPlayerSceneMediaView.setMediaPlayer(null);
            mediaOfVideo = null;
            attemptsDownloadVideoIfError = 0;
            if(mediaPlayerOfVideo != null) {
                mediaPlayerOfVideo.stop();
                mediaPlayerOfVideo.dispose();
                mediaPlayerOfVideo = null;
            }
            System.gc();

            VideoSelectionMenuController videoSelectionMenuControllerWhenSwitch = fxmlLoader.getController();
            VideoSelectionMenuController.displayVBox(videoSelectionMenuControllerWhenSwitch.videoSelectionMenuFlowPane);

            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBox.setDisable(false);
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBox.setDisable(false);

            VideoSelectionMenuController.isLeftComboBoxUpdateWhileChangeScene = true;
            VideoSelectionMenuController.isRightComboBoxUpdateWhileChangeScene = true;

            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBox.setValue(VideoSelectionMenuController.lastSubjectHashMap.get(subtopic));
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBox.setValue(VideoSelectionMenuController.lastTopicHashMap.get(subtopic));

            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBox.getItems().addAll(VideoSelectionMenuController.leftComboBox);
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBox.getItems().addAll(VideoSelectionMenuController.rightComboBox);

            VideoSelectionMenuController.currentController = videoSelectionMenuControllerWhenSwitch;

            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuLeftComboBoxPromptLabel.setVisible(false);
            videoSelectionMenuControllerWhenSwitch.videoSelectionMenuRightComboBoxPromptLabel.setVisible(false);

            if(DBInteraction.isOfflineMode) {
                videoSelectionMenuControllerWhenSwitch.videoSelectionMenuOfflineModeButton.setVisible(true);
                videoSelectionMenuControllerWhenSwitch.videoSelectionMenuOfflineModeButton.setDisable(true);
                videoSelectionMenuControllerWhenSwitch.videoSelectionMenuOfflineModeButton.setOpacity(1);
                videoSelectionMenuControllerWhenSwitch.videoSelectionMenuOfflineModeButton.setText("Автономный режим включён");
            }
        }
        MainMenuController.currentStage.setScene(newScene);
    }

    @FXML
    private void playStopVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
            if (isVideoPlayed) {
                mediaPlayerOfVideo.pause();
                videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/play-button.png")).toString()));
                isVideoPlayed = false;
            } else {
                mediaPlayerOfVideo.play();
                videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/stop-button.png")).toString()));
                isVideoPlayed = true;
                isSeekingTime = false;
            }
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void changeTimeWithSlider() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
            mediaPlayerOfVideo.pause();
            isSeekingTime = true;
            double newTimeSeconds = videoPlayerSceneTimeSlider.getValue();

            Platform.runLater(() -> {
                mediaPlayerOfVideo.seek(Duration.seconds(newTimeSeconds));
                mediaPlayerOfVideo.pause();
                mediaPlayerOfVideo.play();

                if(newTimeSeconds >= totalDurationOfVideo.toSeconds()) {
                    videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/play-button.png")).toString()));
                    isVideoPlayed = false;
                    Platform.runLater(() -> {
                        mediaPlayerOfVideo.pause();
                        videoPlayerSceneTimeSlider.setValue(0);
                    });
                    mediaPlayerOfVideo.seek(Duration.seconds(0));
                }
                isSeekingTime = false;
            });

        }
    }

    @FXML
    private void continuePLayVideo() {
        if(isVideoPlayed && mediaOfVideo != null && !isPauseForDisposeNow) {
            mediaPlayerOfVideo.play();
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void volumeOffOn() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
            if (isVolumeOff) {
                mediaPlayerOfVideo.setVolume(lastVolumeValueWhenUserOffVolume);
                videoPlayerSceneVolumeSlider.setValue(lastVolumeValueWhenUserOffVolume * 100);
                isVolumeOff = false;
            } else {
                lastVolumeValueWhenUserOffVolume = mediaPlayerOfVideo.getVolume();
                mediaPlayerOfVideo.setVolume(0);
                videoPlayerSceneVolumeSlider.setValue(0);
                isVolumeOff = true;
            }
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void changeVolumeWithSlider() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            if(videoPlayerSceneVolumeSlider.getValue() == 0) lastVolumeValueWhenUserOffVolume = 0;
            timerForFullScreenWhenUserMovedMouse.cancel();
            mediaPlayerOfVideo.setVolume(videoPlayerSceneVolumeSlider.getValue()/100);
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void tenSecondsBack() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
            isSeekingTime = true;
            mediaPlayerOfVideo.seek(Duration.seconds((int)mediaPlayerOfVideo.getCurrentTime().toSeconds() - 10));
            isSeekingTime = false;
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void tenSecondsForward() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
            isSeekingTime = true;

            double newTimeSeconds = videoPlayerSceneTimeSlider.getValue() + 10;

            mediaPlayerOfVideo.seek(Duration.seconds(newTimeSeconds));

            if(newTimeSeconds >= totalDurationOfVideo.toSeconds()) {
                videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/play-button.png")).toString()));
                isVideoPlayed = false;
                Platform.runLater(() -> {
                    mediaPlayerOfVideo.pause();
                    videoPlayerSceneTimeSlider.setValue(0);
                });
                mediaPlayerOfVideo.seek(Duration.seconds(0));
            }

            isSeekingTime = false;
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void openPreviousVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
            if (currentKeyOfVideoForDictionary == 0) currentKeyOfVideoForDictionary = lastNumberOfDictionary + 1;
            urlOfVideo = dictionaryOfPathToVideosInCurrentDirectory.get(--currentKeyOfVideoForDictionary);

            isVideoPlayed = true;

            attemptsDownloadVideoIfError = 0;

            videoPlayerSceneAnotherVideoButton.setDisable(true);
            videoPlayerSceneSynchronizeImageView.setVisible(true);

            restartPlayer();
            resetTimerForMainMenuStageDragging();
        }
    }

    @FXML
    private void openNextVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();

            if (currentKeyOfVideoForDictionary >= lastNumberOfDictionary) currentKeyOfVideoForDictionary = -1;
            urlOfVideo = dictionaryOfPathToVideosInCurrentDirectory.get(++currentKeyOfVideoForDictionary);

            isVideoPlayed = true;
            attemptsDownloadVideoIfError = 0;

            videoPlayerSceneAnotherVideoButton.setDisable(true);
            videoPlayerSceneSynchronizeImageView.setVisible(true);

            restartPlayer();
            resetTimerForMainMenuStageDragging();
        }
    }

    public void doDictionaryOfPathToVideosInCurrentDirectory(String urlOfDirectoryOfVideo) {
        dictionaryOfPathToVideosInCurrentDirectory = new HashMap<>();
        File directory = new File(urlOfDirectoryOfVideo);
        if(isEducationVideo) {
            File generalDirectory = directory.getParentFile();
            if(generalDirectory.exists() && generalDirectory.isDirectory()) {
                int key = 0;
                lastNumberOfDictionary = 0;
                for (File file : Objects.requireNonNull(generalDirectory.listFiles())) {
                    for(File fileInside: Objects.requireNonNull(file.listFiles())) {
                        if (isFileVideo(fileInside)) {
                            if (fileInside.toURI().toString().equals(urlOfVideo)) {
                                currentKeyOfVideoForDictionary = key;
                            }
                            dictionaryOfPathToVideosInCurrentDirectory.put(key, fileInside.toURI().toString());
                            if (key > lastNumberOfDictionary) lastNumberOfDictionary = key;
                            key++;
                        }
                    }
                }
            }
        } else {
            if (directory.exists() && directory.isDirectory()) {
                int key = 0;
                lastNumberOfDictionary = 0;
                for (File file : Objects.requireNonNull(directory.listFiles())) {
                    if (isFileVideo(file)) {
                        if (file.toURI().toString().equals(urlOfVideo)) {
                            currentKeyOfVideoForDictionary = key;
                        }
                        dictionaryOfPathToVideosInCurrentDirectory.put(key, file.toURI().toString());
                        if (key > lastNumberOfDictionary) lastNumberOfDictionary = key;
                        key++;
                    }
                }
            }
        }
    }

    private static boolean isFileVideo(File file) {
        if(file.isFile()) {
            String fileName = file.getName().toLowerCase();
            return fileName.endsWith(".mp4");
        }
        return false;
    }

    @FXML
    public void openNewVideo() {
        if(!isFullScreenListenerAdded) {
            addListenerForStageFullScreenProperty();
            isFullScreenListenerAdded = true;
        }
        if(mediaPlayerOfVideo != null) {
            mediaPlayerOfVideo.pause();
            videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/play-button.png")).toString()));
            isVideoPlayed = false;
        }

        FileChooser fileChooserOfVideo = new FileChooser();
        fileChooserOfVideo.setTitle("Выберите видео");
        FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter(
                "Video Files", "*.mp4");
        fileChooserOfVideo.getExtensionFilters().add(videoFilter);
        File selectedFile = fileChooserOfVideo.showOpenDialog(MainMenuController.currentStage);

        if(selectedFile != null) {
            videoPlayerSceneAnotherVideoButton.setDisable(true);
            videoPlayerSceneSynchronizeImageView.setVisible(true);

            urlOfVideo = selectedFile.toURI().toString();
            String urlOfDirectoryOfVideo = selectedFile.getParent();

            doDictionaryOfPathToVideosInCurrentDirectory(urlOfDirectoryOfVideo);

            attemptsDownloadVideoIfError = 0;
            restartPlayer();
        } else {
            if(mediaPlayerOfVideo != null)  {
                mediaPlayerOfVideo.play();
                videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/stop-button.png")).toString()));
                isVideoPlayed = true;
            }
        }
    }

    private void alertOn() {
        Alert alertWithVideo = new Alert(Alert.AlertType.ERROR);
        alertWithVideo.setTitle("Ошибка открытия видео");
        alertWithVideo.setHeaderText("Не удалось открыть выбранный видеоролик после нескольких попыток.");
        alertWithVideo.setContentText("""
                    Обратите внимание, что видеоплеер поддерживает видеоролики в формате MP4 с кодеком H.264.
                    Если выбранное видео является обучающим, возможно оно находится на этапе загрузки.
                    Вы будуте автоматически возвращены в прошлое окно.""");
        alertWithVideo.getDialogPane().setMinWidth(400);
        alertWithVideo.getDialogPane().setMinHeight(250);
        alertWithVideo.initOwner(MainMenuController.currentStage);
        alertWithVideo.showAndWait();
        try {
            Platform.runLater(() -> {
                videoPlayerSceneMediaView.setMediaPlayer(null);
                mediaOfVideo = null;
                attemptsDownloadVideoIfError = 0;
                if(mediaPlayerOfVideo != null) {
                    mediaPlayerOfVideo.stop();
                    mediaPlayerOfVideo.dispose();
                    mediaPlayerOfVideo = null;
                }
                System.gc();
            });
            switchingToTheBackScene();
        } catch (Exception e) {
            System.out.println("Ошибка : " + e.getMessage());
        }
    }

    public void restartPlayer() {
        videoPlayerSceneMediaView.setMediaPlayer(null);
        mediaOfVideo = null;
        if(mediaPlayerOfVideo != null) {
            mediaPlayerOfVideo.stop();
            mediaPlayerOfVideo.dispose();
            mediaPlayerOfVideo = null;
        }
        System.gc();
        if(attemptsDownloadVideoIfError == MAX_ATTEMPTS_DOWNLOAD_VIDEO_IF_ERROR) {
            alertOn();
        }
        isPauseForDisposeNow = true;
        if(isBackButton) {
            isBackButton = false;
            return;
        }
        setupVideo();
    }

    private void setupVideo() {
        try {
            videoPlayerSceneCurrentTimeLabel.setText("00:00");
            mediaOfVideo = new Media(urlOfVideo);
            mediaPlayerOfVideo = new MediaPlayer(mediaOfVideo);

            totalDurationOfVideo = mediaOfVideo.getDuration();
            isVideoHasHours = totalDurationOfVideo.toHours() >= 1;

            if(isFullScreen) {
                resetTimerForMainMenuStageDragging();
                wrapInterfaceWhenFullScreen();
            }
            else updateSizes(videoPlayerSceneBorderPane.getHeight());

            videoPlayerSceneTimeSlider.setValue(0);
            mediaPlayerOfVideo.setVolume(0.5);
            videoPlayerSceneVolumeSlider.setValue(50);
            videoPlayerSceneVolumeLabel.setText("50");
            isVolumeOff = false;

            mediaPlayerOfVideo.setOnError(() -> {
                System.err.println("Ошибка медиаплеера: " + mediaPlayerOfVideo.getError().getMessage());
                attemptsDownloadVideoIfError++;
                videoPlayerSceneMediaView.setMediaPlayer(null);
                mediaOfVideo = null;
                if(mediaPlayerOfVideo != null) {
                    mediaPlayerOfVideo.stop();
                    mediaPlayerOfVideo.dispose();
                    mediaPlayerOfVideo = null;
                }
                System.gc();
                restartPlayer();
            });

            mediaPlayerOfVideo.setOnReady(() -> {
                totalDurationOfVideo = mediaOfVideo.getDuration();
                isVideoHasHours = totalDurationOfVideo.toHours() >= 1;
                videoPlayerSceneTimeSlider.setMax(totalDurationOfVideo.toSeconds());

                if(isVideoHasHours) {
                    updateSizes(videoPlayerSceneBorderPane.getHeight());
                    videoPlayerSceneFullTimeLabel.setText(String.format("%02d:%02d:%02d",(int) totalDurationOfVideo.toHours(), (int) totalDurationOfVideo.toMinutes() % 60, (int) totalDurationOfVideo.toSeconds() % 60));
                    videoPlayerSceneCurrentTimeLabel.setText("00:00:00");
                } else {
                    videoPlayerSceneFullTimeLabel.setText(String.format("%02d:%02d", (int) totalDurationOfVideo.toMinutes(), (int) totalDurationOfVideo.toSeconds() % 60));
                }

                mediaPlayerOfVideo.currentTimeProperty().addListener((_, _, newValue) -> {
                    if(!isSeekingTime) {
                        videoPlayerSceneTimeSlider.setValue(newValue.toSeconds());
                        if(isVideoHasHours) {
                            videoPlayerSceneCurrentTimeLabel.setText(String.format("%02d:%02d:%02d",(int) newValue.toHours(), (int) newValue.toMinutes() % 60, (int) newValue.toSeconds() % 60));
                        } else {
                            videoPlayerSceneCurrentTimeLabel.setText(String.format("%02d:%02d", (int) newValue.toMinutes(), (int) newValue.toSeconds() % 60));
                        }
                    }
                });

                mediaPlayerOfVideo.setOnEndOfMedia(() -> {
                    videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/play-button.png")).toString()));
                    isVideoPlayed = false;
                    mediaPlayerOfVideo.seek(Duration.seconds(0));
                    Platform.runLater(() -> {
                        mediaPlayerOfVideo.pause();
                        videoPlayerSceneTimeSlider.setValue(0);
                    });
                });

                videoPlayerSceneStopPlayImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/stop-button.png")).toString()));

                isVideoPlayed = true;
                timerForFullScreenWhenUserMovedMouse.cancel();
                pauseForDispose = new PauseTransition(Duration.millis(3000));
                pauseForDispose.setOnFinished(_ -> {
                    videoPlayerSceneMediaView.setMediaPlayer(mediaPlayerOfVideo);
                    isPauseForDisposeNow = false;
                    videoPlayerSceneAnotherVideoButton.setDisable(false);
                    videoPlayerSceneSynchronizeImageView.setVisible(false);

                    if(isBackButton) {
                        videoPlayerSceneMediaView.setMediaPlayer(null);
                        mediaOfVideo = null;
                        attemptsDownloadVideoIfError = 0;
                        if(mediaPlayerOfVideo != null) {
                            mediaPlayerOfVideo.stop();
                            mediaPlayerOfVideo.dispose();
                            mediaPlayerOfVideo = null;
                        }
                        System.gc();
                        isBackButton = false;
                    }

                    if(isFullScreen) {
                        resetTimerForMainMenuStageDragging();
                        wrapInterfaceWhenFullScreen();
                    }
                    else updateSizes(videoPlayerSceneBorderPane.getHeight());

                    videoPlayerSceneTimeSlider.setValue(0);
                    if(mediaPlayerOfVideo != null) mediaPlayerOfVideo.setVolume(0.5);
                    videoPlayerSceneVolumeSlider.setValue(50);
                    videoPlayerSceneVolumeLabel.setText("50");
                    isVolumeOff = false;

                    if(mediaPlayerOfVideo != null) mediaPlayerOfVideo.play();
                });

                pauseForDispose.play();
            });
        } catch (Exception e) {
            alertOn();
        }

    }

    @FXML
    private void wrapUnwrapVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            if (isFullScreen) {
                MainMenuController.currentStage.setFullScreen(false);
            } else {
                MainMenuController.currentStage.setFullScreen(true);

                videoPlayerSceneWrapUnwrapImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/wrap-button.png")).toString()));

                wrapInterfaceWhenFullScreen();
            }
        }
    }

    private void wrapInterfaceWhenFullScreen() {
            videoPlayerSceneBackButtonHeightBeforeWrap = videoPlayerSceneBackButton.getHeight();
            videoPlayerSceneTopHBoxHeightBeforeWrap = videoPlayerSceneTopHBox.getHeight();

            videoPlayerSceneCurrentTimeLabelHeightBeforeWrap = videoPlayerSceneCurrentTimeLabel.getHeight();
            videoPlayerSceneTimeSliderHeightBeforeWrap = videoPlayerSceneTimeSlider.getHeight();
            videoPlayerSceneFullTimeLabelHeightBeforeWrap = videoPlayerSceneFullTimeLabel.getHeight();

            videoPlayerSceneVolumeImageViewHeightBeforeWrap = videoPlayerSceneVolumeImageView.getFitHeight();
            videoPlayerSceneVolumeSliderHeightBeforeWrap = videoPlayerSceneVolumeSlider.getHeight();
            videoPlayerSceneVolumeLabelHeightBeforeWrap = videoPlayerSceneVolumeLabel.getHeight();

            videoPlayerSceneBotVBoxHeightBeforeWrap = videoPlayerSceneBotVBox.getHeight();

            videoPlayerSceneVolumeImageViewWidthBeforeWrap = videoPlayerSceneVolumeImageView.getFitWidth();

            videoPlayerSceneCurrentTimeLabelFontBeforeWrap = videoPlayerSceneCurrentTimeLabel.getFont();

            videoPlayerSceneTopHBox.setVisible(false);
            videoPlayerSceneTopHBox.setDisable(true);
            videoPlayerSceneBackButton.setPrefHeight(0);
            videoPlayerSceneTopHBox.setPrefHeight(0);

            resetTimerForMainMenuStageDragging();

            videoPlayerSceneBorderPane.setOnMouseMoved(_ -> {
                videoPlayerSceneBorderPane.setStyle("-fx-background-color: #FFF");
                videoPlayerSceneMediaView.fitHeightProperty().unbind();
                videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

                setSizesAfterUnwrapWithoutTopHBox();

                resetTimerForMainMenuStageDragging();
            });
            isFullScreen = true;
    }

    public void addListenerForStageFullScreenProperty() {
        MainMenuController.currentStage.fullScreenProperty().addListener((_, oldValue, _) -> {
            if(oldValue) {
                videoPlayerSceneBorderPane.setStyle("-fx-background-color: #FFF");
                timerForFullScreenWhenUserMovedMouse.cancel();

                videoPlayerSceneMediaView.fitHeightProperty().unbind();
                videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneTopHBox.heightProperty()).subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

                videoPlayerSceneBackButton.setPrefHeight(videoPlayerSceneBackButtonHeightBeforeWrap);
                videoPlayerSceneTopHBox.setPrefHeight(videoPlayerSceneTopHBoxHeightBeforeWrap);
                videoPlayerSceneTopHBox.setVisible(true);
                videoPlayerSceneTopHBox.setDisable(false);

                setSizesAfterUnwrapWithoutTopHBox();

                videoPlayerSceneBorderPane.setOnMouseMoved(null);
                isFullScreen = false;
                videoPlayerSceneWrapUnwrapImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/unwrap-button.png")).toString()));
            }
        });
    }

    public void setSizesAfterUnwrapWithoutTopHBox() {
        videoPlayerSceneCurrentTimeLabel.setPrefHeight(videoPlayerSceneCurrentTimeLabelHeightBeforeWrap);
        videoPlayerSceneTimeSlider.setPrefHeight(videoPlayerSceneTimeSliderHeightBeforeWrap);
        videoPlayerSceneFullTimeLabel.setPrefHeight(videoPlayerSceneFullTimeLabelHeightBeforeWrap);

        videoPlayerSceneVolumeImageView.setFitHeight(videoPlayerSceneVolumeImageViewHeightBeforeWrap);
        videoPlayerSceneVolumeSlider.setPrefHeight(videoPlayerSceneVolumeSliderHeightBeforeWrap);
        videoPlayerSceneVolumeLabel.setPrefHeight(videoPlayerSceneVolumeLabelHeightBeforeWrap);

        videoPlayerSceneBotVBox.setPrefHeight(videoPlayerSceneBotVBoxHeightBeforeWrap);

        videoPlayerSceneVolumeImageView.setFitWidth(videoPlayerSceneVolumeImageViewWidthBeforeWrap);

        videoPlayerSceneCurrentTimeLabel.setFont(videoPlayerSceneCurrentTimeLabelFontBeforeWrap);

        videoPlayerSceneBotVBox.setVisible(true);
        videoPlayerSceneBotVBox.setDisable(false);

        videoPlayerSceneVolumeSlider.setVisible(true);
        videoPlayerSceneTimeSlider.setVisible(true);
        videoPlayerSceneWrapUnwrapImageView.setVisible(true);
        videoPlayerSceneVolumeSlider.setDisable(false);
        videoPlayerSceneTimeSlider.setDisable(false);
        videoPlayerSceneWrapUnwrapImageView.setDisable(false);

        AnchorPane.setTopAnchor(videoPlayerSceneStackPane, 0d);
    }

    private void resetTimerForMainMenuStageDragging() {
        timerForFullScreenWhenUserMovedMouse.cancel();
        timerForFullScreenWhenUserMovedMouse = new Timer();
        timerForFullScreenWhenUserMovedMouse.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isFullScreen) {
                    Platform.runLater(() -> {
                        videoPlayerSceneMediaView.fitHeightProperty().unbind();
                        videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty());

                        videoPlayerSceneBotVBox.setVisible(false);
                        videoPlayerSceneBotVBox.setDisable(true);

                        videoPlayerSceneBackButton.setPrefHeight(0);
                        videoPlayerSceneTopHBox.setPrefHeight(0);

                        videoPlayerSceneCurrentTimeLabel.setPrefHeight(0);
                        videoPlayerSceneTimeSlider.setPrefHeight(0);
                        videoPlayerSceneFullTimeLabel.setPrefHeight(0);

                        videoPlayerSceneVolumeImageView.setFitHeight(0);
                        videoPlayerSceneVolumeSlider.setPrefHeight(0);
                        videoPlayerSceneVolumeLabel.setPrefHeight(0);

                        videoPlayerSceneBotVBox.setPrefHeight(0);

                        videoPlayerSceneVolumeImageView.setFitWidth(1);

                        videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 1));
                        videoPlayerSceneFullTimeLabel.setFont(Font.font("Arial", 1));
                        videoPlayerSceneVolumeLabel.setFont(Font.font("Arial", 1));

                        videoPlayerSceneVolumeSlider.setVisible(false);
                        videoPlayerSceneTimeSlider.setVisible(false);
                        videoPlayerSceneWrapUnwrapImageView.setVisible(false);
                        videoPlayerSceneVolumeSlider.setDisable(true);
                        videoPlayerSceneTimeSlider.setDisable(true);
                        videoPlayerSceneWrapUnwrapImageView.setDisable(true);

                        videoPlayerSceneBorderPane.setStyle("-fx-background-color: #000");

                        AnchorPane.setTopAnchor(videoPlayerSceneStackPane, topAnchorWhenFullScreen);
                    });
                }
            }
        }, 3000);
    }

    public void updateSizes(Number newValue) {
        if(trackInTimeSlider == null || trackInVolumeSlider == null || thumbInTimeSlider == null || thumbInVolumeSlider == null) {
            return;
        }
        if (newValue.intValue() < 600) {
            topAnchorWhenFullScreen = 32;

            videoPlayerSceneTopHBox.setPrefHeight(39);

            videoPlayerSceneBackButton.setPrefWidth(132);
            videoPlayerSceneBackButton.setPrefHeight(27);
            videoPlayerSceneBackButton.setFont(Font.font("Arial Black", 14));

            if(isVideoHasHours) {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(62);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 14));
            } else {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(45);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 14));
            }

            videoPlayerSceneVolumeImageView.setFitWidth(37);
            videoPlayerSceneVolumeImageView.setFitHeight(37);

            videoPlayerSceneVolumeSlider.setPrefWidth(92);

            trackInTimeSlider.setStyle("-fx-pref-height: 5px;");
            trackInVolumeSlider.setStyle("-fx-pref-height: 5px;");

            thumbInTimeSlider.setStyle("-fx-pref-width: 14px; -fx-pref-height: 14px;");
            thumbInVolumeSlider.setStyle("-fx-pref-width: 14px; -fx-pref-height: 14px;");
        } else if (newValue.intValue() < 800) {
            topAnchorWhenFullScreen = 33;

            videoPlayerSceneTopHBox.setPrefHeight(40);

            videoPlayerSceneBackButton.setPrefWidth(171);
            videoPlayerSceneBackButton.setPrefHeight(28);
            videoPlayerSceneBackButton.setFont(Font.font("Arial Black", 15));

            if(isVideoHasHours) {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(65);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 15));
            } else {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(45);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 15));
            }

            videoPlayerSceneVolumeImageView.setFitWidth(40);
            videoPlayerSceneVolumeImageView.setFitHeight(40);

            videoPlayerSceneVolumeSlider.setPrefWidth(105);

            trackInTimeSlider.setStyle("-fx-pref-height: 6px;");
            trackInVolumeSlider.setStyle("-fx-pref-height: 6px;");

            thumbInTimeSlider.setStyle("-fx-pref-width: 15px; -fx-pref-height: 15px;");
            thumbInVolumeSlider.setStyle("-fx-pref-width: 15px; -fx-pref-height: 15px;");
        } else if (newValue.intValue() < 1000) {
            topAnchorWhenFullScreen = 34;

            videoPlayerSceneTopHBox.setPrefHeight(41);

            videoPlayerSceneBackButton.setPrefWidth(210);
            videoPlayerSceneBackButton.setPrefHeight(29);
            videoPlayerSceneBackButton.setFont(Font.font("Arial Black", 16));

            if(isVideoHasHours) {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(68);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 16));
            } else {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(45);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 16));
            }

            videoPlayerSceneVolumeImageView.setFitWidth(43);
            videoPlayerSceneVolumeImageView.setFitHeight(43);

            videoPlayerSceneVolumeSlider.setPrefWidth(118);

            trackInTimeSlider.setStyle("-fx-pref-height: 7px;");
            trackInVolumeSlider.setStyle("-fx-pref-height: 7px;");

            thumbInTimeSlider.setStyle("-fx-pref-width: 16px; -fx-pref-height: 16px;");
            thumbInVolumeSlider.setStyle("-fx-pref-width: 16px; -fx-pref-height: 16px;");
        } else {
            topAnchorWhenFullScreen = 35;

            videoPlayerSceneTopHBox.setPrefHeight(42);

            videoPlayerSceneBackButton.setPrefWidth(250);
            videoPlayerSceneBackButton.setPrefHeight(30);
            videoPlayerSceneBackButton.setFont(Font.font("Arial Black", 17));

            if(isVideoHasHours) {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(71);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 17));
            } else {
                videoPlayerSceneCurrentTimeLabel.setPrefWidth(45);
                videoPlayerSceneCurrentTimeLabel.setFont(Font.font("Arial", 17));
            }

            videoPlayerSceneVolumeImageView.setFitWidth(46);
            videoPlayerSceneVolumeImageView.setFitHeight(46);

            videoPlayerSceneVolumeSlider.setPrefWidth(130);

            trackInTimeSlider.setStyle("-fx-pref-height: 8px;");
            trackInVolumeSlider.setStyle("-fx-pref-height: 8px;");

            thumbInTimeSlider.setStyle("-fx-pref-width: 17px; -fx-pref-height: 17px;");
            thumbInVolumeSlider.setStyle("-fx-pref-width: 17px; -fx-pref-height: 17px;");
        }
    }

    @FXML
    void initialize() {
        videoPlayerSceneVolumeSlider.setValue(0);
        videoPlayerSceneTimeSlider.setValue(0);

        videoPlayerSceneSynchronizeImageView.fitHeightProperty().bind(videoPlayerSceneBackButton.heightProperty());
        videoPlayerSceneSynchronizeImageView.fitWidthProperty().bind(videoPlayerSceneSynchronizeImageView.fitHeightProperty());

        videoPlayerSceneMediaView.fitWidthProperty().bind(videoPlayerSceneBorderPane.widthProperty());
        videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneTopHBox.heightProperty()).subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

        videoPlayerSceneFullTimeLabel.prefWidthProperty().bind(videoPlayerSceneCurrentTimeLabel.prefWidthProperty());
        videoPlayerSceneFullTimeLabel.fontProperty().bind(videoPlayerSceneCurrentTimeLabel.fontProperty());

        videoPlayerSceneVolumeLabel.prefWidthProperty().bind(videoPlayerSceneCurrentTimeLabel.prefWidthProperty());
        videoPlayerSceneVolumeLabel.fontProperty().bind(videoPlayerSceneCurrentTimeLabel.fontProperty());

        if(!isEducationVideo) {
            videoPlayerSceneAnotherVideoButton.prefWidthProperty().bind(videoPlayerSceneBackButton.prefWidthProperty());
            videoPlayerSceneAnotherVideoButton.prefHeightProperty().bind(videoPlayerSceneBackButton.prefHeightProperty());
            videoPlayerSceneAnotherVideoButton.fontProperty().bind(videoPlayerSceneBackButton.fontProperty());
        } else {
            videoPlayerSceneAnotherVideoButton.setPrefWidth(0);
            videoPlayerSceneAnotherVideoButton.setPrefHeight(0);
            videoPlayerSceneAnotherVideoButton.setFont(new Font(1));
        }
        videoPlayerScenePreviousVideoImageView.fitWidthProperty().bind(videoPlayerSceneVolumeImageView.fitWidthProperty());
        videoPlayerScenePreviousVideoImageView.fitHeightProperty().bind(videoPlayerSceneVolumeImageView.fitHeightProperty());

        videoPlayerSceneTenSecondsBackImageView.fitWidthProperty().bind(videoPlayerSceneVolumeImageView.fitWidthProperty());
        videoPlayerSceneTenSecondsBackImageView.fitHeightProperty().bind(videoPlayerSceneVolumeImageView.fitHeightProperty());

        videoPlayerSceneStopPlayImageView.fitWidthProperty().bind(videoPlayerSceneVolumeImageView.fitWidthProperty());
        videoPlayerSceneStopPlayImageView.fitHeightProperty().bind(videoPlayerSceneVolumeImageView.fitHeightProperty());

        videoPlayerSceneTenSecondsForwardImageView.fitWidthProperty().bind(videoPlayerSceneVolumeImageView.fitWidthProperty());
        videoPlayerSceneTenSecondsForwardImageView.fitHeightProperty().bind(videoPlayerSceneVolumeImageView.fitHeightProperty());

        videoPlayerSceneNextVideoImageView.fitWidthProperty().bind(videoPlayerSceneVolumeImageView.fitWidthProperty());
        videoPlayerSceneNextVideoImageView.fitHeightProperty().bind(videoPlayerSceneVolumeImageView.fitHeightProperty());

        videoPlayerSceneWrapUnwrapImageView.fitWidthProperty().bind(videoPlayerSceneVolumeImageView.fitWidthProperty());
        videoPlayerSceneWrapUnwrapImageView.fitHeightProperty().bind(videoPlayerSceneVolumeImageView.fitHeightProperty());

        videoPlayerSceneVolumeSlider.valueProperty().addListener((_,_,newValue) -> {
            videoPlayerSceneVolumeLabel.setText(String.format("%d",(int)videoPlayerSceneVolumeSlider.getValue()));
            if(newValue.intValue() == 0) {
                videoPlayerSceneVolumeImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/without-volume-button.png")).toString()));
                isVolumeOff = true;
            } else if(isVolumeOff) {
                videoPlayerSceneVolumeImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/volume-button.png")).toString()));
                isVolumeOff = false;
            }
        });

        videoPlayerSceneBorderPane.heightProperty().addListener(( _, _, newValue) -> updateSizes(newValue));

    }
}
