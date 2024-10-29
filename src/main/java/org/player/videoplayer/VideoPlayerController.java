package org.player.videoplayer;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXSlider;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


public class VideoPlayerController {


    @FXML
    private Label videoPlayerSceneAnotherVideoButton;

    @FXML
    private Label videoPlayerSceneBackButton;

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
    private AnchorPane videoPlayerSceneAnchorPane;

    @FXML
    private StackPane videoPlayerSceneStackPane;

    @FXML
    private ImageView videoPlayerSceneStopPlayImageView;

    @FXML
    private ImageView videoPlayerSceneTenSecondsBackImageView;

    @FXML
    private ImageView videoPlayerSceneTenSecondsForwardImageView;

    @FXML
    private JFXSlider videoPlayerSceneTimeSlider;

    @FXML
    private ImageView videoPlayerSceneVolumeImageView;

    @FXML
    private Label videoPlayerSceneVolumeLabel;

    @FXML
    private Slider videoPlayerSceneVolumeSlider;

    @FXML
    private ImageView videoPlayerSceneWrapUnwrapImageView;

    @FXML
    private BorderPane videoPlayerSceneBorderPane;

    @FXML
    private HBox videoPlayerSceneTopHBox;

    @FXML
    private VBox videoPlayerSceneBotVBox;

    @FXML
    private ImageView videoPlayerSceneCentralImageView;

    private static Stage currentStage;
    private static Scene currentScene;
    private static Scene newScene;

    private static String urlOfVideo;
    private String urlOfDirectoryOfVideo;

    private static int attemptsDownloadVideoIfError = 0;
    private static final int MAX_ATTEMPTS_DOWNLOAD_VIDEO_IF_ERROR = 100;

    private Media mediaOfVideo;
    private MediaPlayer mediaPlayerOfVideo;

    Duration totalDurationOfVideo;

    private static boolean isVideoPlayed = true;
    private static boolean isVideoEnded = false;
    private static boolean isVolumeOff = true;
    private static boolean isFullScreen = false;
    private static boolean isFullScreenListenerAdded = false;
    private static boolean isPauseForDisposeNow = false;
    private static boolean isSeekingTime = false;


    private double lastVolumeValueWhenUserOffVolume;

    private static HashMap<Integer, String> dictionaryOfPathToVideosInCurrentDirectory;

    private static int lastNumberOfDictionary = 0;

    private static int currentKeyOfVideoForDictionary;

    private static double videoPlayerSceneTopHBoxHeightBeforeWrap;
    private static double videoPlayerSceneBackButtonHeightBeforeWrap;
    private static double videoPlayerSceneAnotherVideoButtonHeightBeforeWrap;
    private static double videoPlayerSceneCurrentTimeLabelHeightBeforeWrap;
    private static double videoPlayerSceneTimeSliderHeightBeforeWrap;
    private static double videoPlayerSceneFullTimeLabelHeightBeforeWrap;
    private static double videoPlayerSceneVolumeImageViewHeightBeforeWrap;
    private static double videoPlayerSceneVolumeSliderHeightBeforeWrap;
    private static double videoPlayerSceneVolumeLabelHeightBeforeWrap;
    private static double videoPlayerSceneTenSecondsBackImageViewHeightBeforeWrap;
    private static double videoPlayerScenePreviousVideoImageViewHeightBeforeWrap;
    private static double videoPlayerSceneStopPlayImageViewHeightBeforeWrap;
    private static double videoPlayerSceneNextVideoImageViewHeightBeforeWrap;
    private static double videoPlayerSceneTenSecondsForwardImageViewHeightBeforeWrap;
    private static double videoPlayerSceneWrapUnwrapImageViewHeightBeforeWrap;
    private static double videoPlayerSceneBotVBoxHeightBeforeWrap;

    private static double videoPlayerSceneVolumeImageViewWidthBeforeWrap;
    private static double videoPlayerSceneTenSecondsBackImageViewWidthBeforeWrap;
    private static double videoPlayerScenePreviousVideoImageViewWidthBeforeWrap;
    private static double videoPlayerSceneStopPlayImageViewWidthBeforeWrap;
    private static double videoPlayerSceneNextVideoImageViewWidthBeforeWrap;
    private static double videoPlayerSceneTenSecondsForwardImageViewWidthBeforeWrap;
    private static double videoPlayerSceneWrapUnwrapImageViewWidthBeforeWrap;

    private static Font videoPlayerSceneCurrentTimeLabelFontBeforeWrap;
    private static Font videoPlayerSceneFullTimeLabelFontBeforeWrap;
    private static Font videoPlayerSceneVolumeLabelFontBeforeWrap;

    private static double topAnchorWhenFullScreen = 33.6;

    private Timer timerForFullScreenWhenUserMovedMouse = new Timer();
    private PauseTransition pauseForDispose;

    @FXML
    private void playStopVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            if (isVideoPlayed) {
                mediaPlayerOfVideo.pause();
                videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/play-button.png").toString()));
                isVideoPlayed = false;
            } else {
                if (isVideoEnded) {
                    isSeekingTime = true;
                    mediaPlayerOfVideo.seek(Duration.seconds(0));
                    isVideoEnded = false;
                }
                mediaPlayerOfVideo.play();
                videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/stop-button.png").toString()));
                isVideoPlayed = true;
                isSeekingTime = false;
            }
        }
    }

    @FXML
    private void changeTimeWithSlider() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            mediaPlayerOfVideo.pause();
            isSeekingTime = true;
            mediaPlayerOfVideo.seek(Duration.seconds((int)videoPlayerSceneTimeSlider.getValue()));
            isSeekingTime = false;
        }
    }

    @FXML
    private void continuePLayVideo() {
        if(isVideoPlayed && mediaOfVideo != null && !isPauseForDisposeNow) mediaPlayerOfVideo.play();
    }

    @FXML
    private void volumeOffOn() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
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
        }
    }

    @FXML
    private void changeVolumeWithSlider() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
        mediaPlayerOfVideo.setVolume(videoPlayerSceneVolumeSlider.getValue()/100);
        }
    }

    @FXML
    private void tenSecondsBack() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            isSeekingTime = true;
            mediaPlayerOfVideo.seek(Duration.seconds((int)mediaPlayerOfVideo.getCurrentTime().toSeconds() - 10));
            isSeekingTime = false;
        }
    }

    @FXML
    private void tenSecondsForward() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            isSeekingTime = true;
            mediaPlayerOfVideo.seek(Duration.seconds((int)mediaPlayerOfVideo.getCurrentTime().toSeconds() + 10));
            isSeekingTime = false;
        }
    }

    @FXML
    private void openPreviousVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            if (currentKeyOfVideoForDictionary == 0) currentKeyOfVideoForDictionary = lastNumberOfDictionary + 1;
            urlOfVideo = dictionaryOfPathToVideosInCurrentDirectory.get(--currentKeyOfVideoForDictionary);
            isVideoPlayed = true;
            isVideoEnded = false;
            attemptsDownloadVideoIfError = 0;
            restartPlayer();
        }
    }

    @FXML
    private void openNextVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            if (currentKeyOfVideoForDictionary >= lastNumberOfDictionary) currentKeyOfVideoForDictionary = -1;
            urlOfVideo = dictionaryOfPathToVideosInCurrentDirectory.get(++currentKeyOfVideoForDictionary);
            isVideoPlayed = true;
            isVideoEnded = false;
            attemptsDownloadVideoIfError = 0;
            restartPlayer();
        }
    }

    private static void doDictionaryOfPathToVideosInCurrentDirectory(String urlOfDirectoryOfVideo) {
        dictionaryOfPathToVideosInCurrentDirectory = new HashMap<>();
        File directory = new File(urlOfDirectoryOfVideo);
        if(directory.exists() && directory.isDirectory()) {
            int key = 0;
            lastNumberOfDictionary = 0;
            for (File file: directory.listFiles()) {
                if(isFileVideo(file)) {
                    if(file.toURI().toString().equals(urlOfVideo)) {
                        currentKeyOfVideoForDictionary = key;
                    }
                    dictionaryOfPathToVideosInCurrentDirectory.put(key,file.toURI().toString());
                    if (key > lastNumberOfDictionary) lastNumberOfDictionary = key;
                    key++;
                }
            }
        }
    }

    private static boolean isFileVideo(File file) {
        if(file.isFile()) {
            String fileName = file.getName().toLowerCase();
            return fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mkv");
        }
        return false;
    }

    @FXML
    private void openNewVideo() {
        if(!isFullScreenListenerAdded) {
            currentStage = (Stage) videoPlayerSceneBorderPane.getScene().getWindow();
            addListenerForStageFullScreenProperty();
            isFullScreenListenerAdded = true;
        }
        if(mediaPlayerOfVideo != null) {
            mediaPlayerOfVideo.pause();
            videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/play-button.png").toString()));
            isVideoPlayed = false;
        }

        FileChooser fileChooserOfVideo = new FileChooser();
        fileChooserOfVideo.setTitle("Выберите видео");
        FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter(
                "Video Files", "*.mp4", "*.avi", "*.mkv"
        );
        fileChooserOfVideo.getExtensionFilters().add(videoFilter);
        File selectedFile = fileChooserOfVideo.showOpenDialog(currentStage);

        if(selectedFile != null) {
            urlOfVideo = selectedFile.toURI().toString();
            urlOfDirectoryOfVideo = selectedFile.getParent();

            doDictionaryOfPathToVideosInCurrentDirectory(urlOfDirectoryOfVideo);

            attemptsDownloadVideoIfError = 0;
            restartPlayer();
        } else {
            if(mediaPlayerOfVideo != null)  {
                mediaPlayerOfVideo.play();
                videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/stop-button.png").toString()));
                isVideoPlayed = true;
            }
        }
    }

    private void restartPlayer() {
        if(mediaPlayerOfVideo != null) {
            mediaPlayerOfVideo.dispose();
            mediaOfVideo = null;
        }

        if(attemptsDownloadVideoIfError == MAX_ATTEMPTS_DOWNLOAD_VIDEO_IF_ERROR) {
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить видео после нескольких попыток. Пожалуйста попробуйте загрузить видео заново, " +
                    "если данный тип видео поддерживается программой").showAndWait();
            return;
        }

        pauseForDispose = new PauseTransition(Duration.millis(1500));
        pauseForDispose.setOnFinished(_ -> {
            setupVideo();
        });
        isPauseForDisposeNow = true;
        pauseForDispose.play();

    }

    private void setupVideo() {
        mediaOfVideo = new Media(urlOfVideo);
        mediaPlayerOfVideo = new MediaPlayer(mediaOfVideo);

        mediaPlayerOfVideo.setOnError(() -> {
            System.err.println("Ошибка медиаплеера: " + mediaPlayerOfVideo.getError().getMessage());
            attemptsDownloadVideoIfError++;
            restartPlayer();
        });

        mediaPlayerOfVideo.setOnReady(() -> {
            totalDurationOfVideo = mediaOfVideo.getDuration();
            videoPlayerSceneTimeSlider.setMax(totalDurationOfVideo.toSeconds());
            videoPlayerSceneFullTimeLabel.setText(String.format("%02d:%02d", (int)totalDurationOfVideo.toMinutes(), (int)totalDurationOfVideo.toSeconds() % 60));

            videoPlayerSceneTimeSlider.setValue(0);
            mediaPlayerOfVideo.setVolume(0.5);
            videoPlayerSceneVolumeSlider.setValue(50);
            videoPlayerSceneVolumeLabel.setText("50");

            mediaPlayerOfVideo.currentTimeProperty().addListener((_, _, newValue) -> {
                System.out.println(newValue.toSeconds());
                System.out.println(videoPlayerSceneTimeSlider.getValue());
                if(!isSeekingTime) {
                    if (isVideoEnded) {
                        isVideoEnded = false;
                        isVideoPlayed = true;
                        videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/stop-button.png").toString()));
                    }
                    videoPlayerSceneTimeSlider.setValue(newValue.toSeconds());
                    videoPlayerSceneCurrentTimeLabel.setText(String.format("%02d:%02d", (int) newValue.toMinutes(), (int) newValue.toSeconds() % 60));
                }
            });

            mediaPlayerOfVideo.setOnEndOfMedia(() -> {
                videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/play-button.png").toString()));
                isVideoEnded = true;
                isVideoPlayed = false;
            });

            isVolumeOff = false;
            mediaPlayerOfVideo.setAutoPlay(true);
            videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/stop-button.png").toString()));

            isVideoPlayed = true;
            videoPlayerSceneMediaView.setMediaPlayer(mediaPlayerOfVideo);
            isPauseForDisposeNow = false;
        });
    }

    @FXML
    private void wrapUnwrapVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            if (isFullScreen) {
                currentStage.setFullScreen(false);

                videoPlayerSceneWrapUnwrapImageView.setImage(new Image(getClass().getResource("/images/unwrap-button.png").toString()));

                videoPlayerSceneBorderPane.setOnMouseMoved(null);
                timerForFullScreenWhenUserMovedMouse.cancel();

                videoPlayerSceneMediaView.fitHeightProperty().unbind();
                videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneTopHBox.heightProperty()).subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

                videoPlayerSceneBackButton.setPrefHeight(videoPlayerSceneBackButtonHeightBeforeWrap);
                videoPlayerSceneTopHBox.setPrefHeight(videoPlayerSceneTopHBoxHeightBeforeWrap);
                videoPlayerSceneAnotherVideoButton.setPrefHeight(videoPlayerSceneAnotherVideoButtonHeightBeforeWrap);

                setSizesAfterUnwrapWithoutTopHBox();

                isFullScreen = false;
            } else {
                currentStage.setFullScreen(true);

                videoPlayerSceneWrapUnwrapImageView.setImage(new Image(getClass().getResource("/images/wrap-button.png").toString()));

                wrapInterfaceWhenFullScreen();
            }
        }
    }

    private void wrapInterfaceWhenFullScreen() {
            videoPlayerSceneBackButtonHeightBeforeWrap = videoPlayerSceneBackButton.getHeight();
            videoPlayerSceneTopHBoxHeightBeforeWrap = videoPlayerSceneTopHBox.getHeight();
            videoPlayerSceneAnotherVideoButtonHeightBeforeWrap = videoPlayerSceneAnotherVideoButton.getHeight();

            videoPlayerSceneCurrentTimeLabelHeightBeforeWrap = videoPlayerSceneCurrentTimeLabel.getHeight();
            videoPlayerSceneTimeSliderHeightBeforeWrap = videoPlayerSceneTimeSlider.getHeight();
            videoPlayerSceneFullTimeLabelHeightBeforeWrap = videoPlayerSceneFullTimeLabel.getHeight();

            videoPlayerSceneVolumeImageViewHeightBeforeWrap = videoPlayerSceneVolumeImageView.getFitHeight();
            videoPlayerSceneVolumeSliderHeightBeforeWrap = videoPlayerSceneVolumeSlider.getHeight();
            videoPlayerSceneVolumeLabelHeightBeforeWrap = videoPlayerSceneVolumeLabel.getHeight();

            videoPlayerSceneTenSecondsBackImageViewHeightBeforeWrap = videoPlayerSceneTenSecondsBackImageView.getFitHeight();
            videoPlayerScenePreviousVideoImageViewHeightBeforeWrap = videoPlayerScenePreviousVideoImageView.getFitHeight();
            videoPlayerSceneStopPlayImageViewHeightBeforeWrap = videoPlayerSceneStopPlayImageView.getFitHeight();

            videoPlayerSceneNextVideoImageViewHeightBeforeWrap = videoPlayerSceneNextVideoImageView.getFitHeight();
            videoPlayerSceneTenSecondsForwardImageViewHeightBeforeWrap = videoPlayerSceneTenSecondsForwardImageView.getFitHeight();

            videoPlayerSceneBotVBoxHeightBeforeWrap = videoPlayerSceneBotVBox.getHeight();

            videoPlayerSceneVolumeImageViewWidthBeforeWrap = videoPlayerSceneVolumeImageView.getFitWidth();

            videoPlayerSceneTenSecondsBackImageViewWidthBeforeWrap = videoPlayerSceneTenSecondsBackImageView.getFitWidth();
            videoPlayerScenePreviousVideoImageViewWidthBeforeWrap = videoPlayerScenePreviousVideoImageView.getFitWidth();
            videoPlayerSceneStopPlayImageViewWidthBeforeWrap = videoPlayerSceneStopPlayImageView.getFitWidth();

            videoPlayerSceneNextVideoImageViewWidthBeforeWrap = videoPlayerSceneNextVideoImageView.getFitWidth();
            videoPlayerSceneTenSecondsForwardImageViewWidthBeforeWrap = videoPlayerSceneTenSecondsForwardImageView.getFitWidth();
            videoPlayerSceneWrapUnwrapImageViewHeightBeforeWrap = videoPlayerSceneWrapUnwrapImageView.getFitHeight();
            videoPlayerSceneWrapUnwrapImageViewWidthBeforeWrap = videoPlayerSceneWrapUnwrapImageView.getFitWidth();

            videoPlayerSceneCurrentTimeLabelFontBeforeWrap = videoPlayerSceneCurrentTimeLabel.getFont();
            videoPlayerSceneFullTimeLabelFontBeforeWrap = videoPlayerSceneFullTimeLabel.getFont();
            videoPlayerSceneVolumeLabelFontBeforeWrap = videoPlayerSceneVolumeLabel.getFont();

            videoPlayerSceneBackButton.setPrefHeight(0);
            videoPlayerSceneTopHBox.setPrefHeight(0);
            videoPlayerSceneAnotherVideoButton.setPrefHeight(0);

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

    private void addListenerForStageFullScreenProperty() {
        currentStage.fullScreenProperty().addListener((_, oldValue, _) -> {
            if(oldValue) {
                videoPlayerSceneBorderPane.setStyle("-fx-background-color: #FFF");
                timerForFullScreenWhenUserMovedMouse.cancel();

                videoPlayerSceneMediaView.fitHeightProperty().unbind();
                videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneTopHBox.heightProperty()).subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

                videoPlayerSceneBackButton.setPrefHeight(videoPlayerSceneBackButtonHeightBeforeWrap);
                videoPlayerSceneTopHBox.setPrefHeight(videoPlayerSceneTopHBoxHeightBeforeWrap);
                videoPlayerSceneAnotherVideoButton.setPrefHeight(videoPlayerSceneAnotherVideoButtonHeightBeforeWrap);

                setSizesAfterUnwrapWithoutTopHBox();

                videoPlayerSceneBorderPane.setOnMouseMoved(null);
                isFullScreen = false;
                videoPlayerSceneWrapUnwrapImageView.setImage(new Image(getClass().getResource("/images/unwrap-button.png").toString()));
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

        videoPlayerSceneTenSecondsBackImageView.setFitHeight(videoPlayerSceneTenSecondsBackImageViewHeightBeforeWrap);
        videoPlayerScenePreviousVideoImageView.setFitHeight(videoPlayerScenePreviousVideoImageViewHeightBeforeWrap);
        videoPlayerSceneStopPlayImageView.setFitHeight(videoPlayerSceneStopPlayImageViewHeightBeforeWrap);

        videoPlayerSceneNextVideoImageView.setFitHeight(videoPlayerSceneNextVideoImageViewHeightBeforeWrap);
        videoPlayerSceneTenSecondsForwardImageView.setFitHeight(videoPlayerSceneTenSecondsForwardImageViewHeightBeforeWrap);
        videoPlayerSceneWrapUnwrapImageView.setFitHeight(videoPlayerSceneWrapUnwrapImageViewHeightBeforeWrap);
        videoPlayerSceneBotVBox.setPrefHeight(videoPlayerSceneBotVBoxHeightBeforeWrap);

        videoPlayerSceneVolumeImageView.setFitWidth(videoPlayerSceneVolumeImageViewWidthBeforeWrap);

        videoPlayerSceneTenSecondsBackImageView.setFitWidth(videoPlayerSceneTenSecondsBackImageViewWidthBeforeWrap);
        videoPlayerScenePreviousVideoImageView.setFitWidth(videoPlayerScenePreviousVideoImageViewWidthBeforeWrap);
        videoPlayerSceneStopPlayImageView.setFitWidth(videoPlayerSceneStopPlayImageViewWidthBeforeWrap);

        videoPlayerSceneNextVideoImageView.setFitWidth(videoPlayerSceneNextVideoImageViewWidthBeforeWrap);
        videoPlayerSceneTenSecondsForwardImageView.setFitWidth(videoPlayerSceneTenSecondsForwardImageViewWidthBeforeWrap);
        videoPlayerSceneWrapUnwrapImageView.setFitWidth(videoPlayerSceneWrapUnwrapImageViewWidthBeforeWrap);

        videoPlayerSceneCurrentTimeLabel.setFont(videoPlayerSceneCurrentTimeLabelFontBeforeWrap);
        videoPlayerSceneFullTimeLabel.setFont(videoPlayerSceneFullTimeLabelFontBeforeWrap);
        videoPlayerSceneVolumeLabel.setFont(videoPlayerSceneVolumeLabelFontBeforeWrap);

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

                        videoPlayerSceneBackButton.setPrefHeight(0);
                        videoPlayerSceneTopHBox.setPrefHeight(0);
                        videoPlayerSceneAnotherVideoButton.setPrefHeight(0);

                        videoPlayerSceneCurrentTimeLabel.setPrefHeight(0);
                        videoPlayerSceneTimeSlider.setPrefHeight(0);
                        videoPlayerSceneFullTimeLabel.setPrefHeight(0);

                        videoPlayerSceneVolumeImageView.setFitHeight(0);
                        videoPlayerSceneVolumeSlider.setPrefHeight(0);
                        videoPlayerSceneVolumeLabel.setPrefHeight(0);

                        videoPlayerSceneTenSecondsBackImageView.setFitHeight(0);
                        videoPlayerScenePreviousVideoImageView.setFitHeight(0);
                        videoPlayerSceneStopPlayImageView.setFitHeight(0);

                        videoPlayerSceneNextVideoImageView.setFitHeight(0);
                        videoPlayerSceneTenSecondsForwardImageView.setFitHeight(0);
                        videoPlayerSceneWrapUnwrapImageView.setFitHeight(0);
                        videoPlayerSceneBotVBox.setPrefHeight(0);

                        videoPlayerSceneVolumeImageView.setFitWidth(1);

                        videoPlayerSceneTenSecondsBackImageView.setFitWidth(1);
                        videoPlayerScenePreviousVideoImageView.setFitWidth(1);
                        videoPlayerSceneStopPlayImageView.setFitWidth(1);

                        videoPlayerSceneNextVideoImageView.setFitWidth(1);
                        videoPlayerSceneTenSecondsForwardImageView.setFitWidth(1);
                        videoPlayerSceneWrapUnwrapImageView.setFitWidth(1);

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



    @FXML
    void initialize() {
        videoPlayerSceneVolumeSlider.setValue(0);
        videoPlayerSceneTimeSlider.setValue(0);

        videoPlayerSceneMediaView.fitWidthProperty().bind(videoPlayerSceneBorderPane.widthProperty());
        videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneTopHBox.heightProperty()).subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

        videoPlayerSceneVolumeSlider.valueProperty().addListener((_,_,newValue) -> {
            videoPlayerSceneVolumeLabel.setText(String.format("%d",(int)videoPlayerSceneVolumeSlider.getValue()));
            if(newValue.intValue() == 0) {
                videoPlayerSceneVolumeImageView.setImage(new Image(getClass().getResource("/images/without-volume-button.png").toString()));
                isVolumeOff = true;
            } else if(isVolumeOff) {
                videoPlayerSceneVolumeImageView.setImage(new Image(getClass().getResource("/images/volume-button.png").toString()));
                isVolumeOff = false;
            }
        });

    }
}
