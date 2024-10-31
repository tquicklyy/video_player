package org.player.videoplayer;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXSlider;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private static boolean isVideoHasHours;
    private static boolean isTrackAndThumbInit = false;

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

    private static Font videoPlayerSceneCurrentTimeLabelFontBeforeWrap;
    private static Font videoPlayerSceneFullTimeLabelFontBeforeWrap;
    private static Font videoPlayerSceneVolumeLabelFontBeforeWrap;

    private static Node trackInTimeSlider;
    private static Node trackInVolumeSlider;
    private static Node thumbInTimeSlider;
    private static Node thumbInVolumeSlider;

    private static double topAnchorWhenFullScreen = 33.6;

    private Timer timerForFullScreenWhenUserMovedMouse = new Timer();
    private PauseTransition pauseForDispose;

    private static Alert alertWithVideo;

    @FXML
    private void playStopVideo() {
        if(mediaPlayerOfVideo != null && !isPauseForDisposeNow) {
            timerForFullScreenWhenUserMovedMouse.cancel();
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
            timerForFullScreenWhenUserMovedMouse.cancel();
            mediaPlayerOfVideo.pause();
            isSeekingTime = true;
            mediaPlayerOfVideo.seek(Duration.seconds(videoPlayerSceneTimeSlider.getValue()));
            isSeekingTime = false;
        }
    }

    @FXML
    private void continuePLayVideo() {
        if(isVideoPlayed && mediaOfVideo != null && !isPauseForDisposeNow) {
            pauseForDispose = new PauseTransition(Duration.millis(500));
            pauseForDispose.setOnFinished(_ -> {
                mediaPlayerOfVideo.play();
                resetTimerForMainMenuStageDragging();
                isPauseForDisposeNow = false;
            });
            isPauseForDisposeNow = true;
            pauseForDispose.play();
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
            mediaPlayerOfVideo.seek(Duration.seconds((int)mediaPlayerOfVideo.getCurrentTime().toSeconds() + 10));
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
            isVideoEnded = false;
            attemptsDownloadVideoIfError = 0;
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
            isVideoEnded = false;
            attemptsDownloadVideoIfError = 0;
            restartPlayer();
            resetTimerForMainMenuStageDragging();
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
            return fileName.endsWith(".mp4");
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
                "Video Files", "*.mp4");
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
            alertWithVideo = new Alert(Alert.AlertType.ERROR);
            alertWithVideo.setTitle("Ошибка открытия видео");
            alertWithVideo.setHeaderText("Не удалось открыть выбранный видеоролик после нескольких попыток.");
            alertWithVideo.setContentText("Пожалуйста, попробуйте ещё раз. Обратите внимание, что " +
                    "видеоплеер поддерживает видеоролики в формате MP4 с кодеком H.264.");
            alertWithVideo.showAndWait();
            return;
        }

        pauseForDispose = new PauseTransition(Duration.millis(1000));
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
            isVideoHasHours = totalDurationOfVideo.toHours() >= 1;
            videoPlayerSceneTimeSlider.setMax(totalDurationOfVideo.toSeconds());
            if(isVideoHasHours) {
                videoPlayerSceneFullTimeLabel.setText(String.format("%02d:%02d:%02d",(int) totalDurationOfVideo.toHours(), (int) totalDurationOfVideo.toMinutes() % 60, (int) totalDurationOfVideo.toSeconds() % 60));
            } else {
                videoPlayerSceneFullTimeLabel.setText(String.format("%02d:%02d", (int) totalDurationOfVideo.toMinutes(), (int) totalDurationOfVideo.toSeconds() % 60));
            }
            videoPlayerSceneTimeSlider.setValue(0);
            mediaPlayerOfVideo.setVolume(0.5);
            videoPlayerSceneVolumeSlider.setValue(50);
            videoPlayerSceneVolumeLabel.setText("50");

            mediaPlayerOfVideo.currentTimeProperty().addListener((_, _, newValue) -> {
                if(!isSeekingTime) {
                    if (isVideoEnded) {
                        isVideoEnded = false;
                        isVideoPlayed = true;
                        videoPlayerSceneStopPlayImageView.setImage(new Image(getClass().getResource("/images/stop-button.png").toString()));
                    }
                    videoPlayerSceneTimeSlider.setValue(newValue.toSeconds());
                    if(isVideoHasHours) {
                        videoPlayerSceneCurrentTimeLabel.setText(String.format("%02d:%02d:%02d",(int) newValue.toHours(), (int) newValue.toMinutes() % 60, (int) newValue.toSeconds() % 60));
                    } else {
                        videoPlayerSceneCurrentTimeLabel.setText(String.format("%02d:%02d", (int) newValue.toMinutes(), (int) newValue.toSeconds() % 60));
                    }
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

            videoPlayerSceneCurrentTimeLabelHeightBeforeWrap = videoPlayerSceneCurrentTimeLabel.getHeight();
            videoPlayerSceneTimeSliderHeightBeforeWrap = videoPlayerSceneTimeSlider.getHeight();
            videoPlayerSceneFullTimeLabelHeightBeforeWrap = videoPlayerSceneFullTimeLabel.getHeight();

            videoPlayerSceneVolumeImageViewHeightBeforeWrap = videoPlayerSceneVolumeImageView.getFitHeight();
            videoPlayerSceneVolumeSliderHeightBeforeWrap = videoPlayerSceneVolumeSlider.getHeight();
            videoPlayerSceneVolumeLabelHeightBeforeWrap = videoPlayerSceneVolumeLabel.getHeight();

            videoPlayerSceneBotVBoxHeightBeforeWrap = videoPlayerSceneBotVBox.getHeight();

            videoPlayerSceneVolumeImageViewWidthBeforeWrap = videoPlayerSceneVolumeImageView.getFitWidth();

            videoPlayerSceneCurrentTimeLabelFontBeforeWrap = videoPlayerSceneCurrentTimeLabel.getFont();
            videoPlayerSceneFullTimeLabelFontBeforeWrap = videoPlayerSceneFullTimeLabel.getFont();
            videoPlayerSceneVolumeLabelFontBeforeWrap = videoPlayerSceneVolumeLabel.getFont();

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

    private void addListenerForStageFullScreenProperty() {
        currentStage.fullScreenProperty().addListener((_, oldValue, _) -> {
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

        videoPlayerSceneBotVBox.setPrefHeight(videoPlayerSceneBotVBoxHeightBeforeWrap);

        videoPlayerSceneVolumeImageView.setFitWidth(videoPlayerSceneVolumeImageViewWidthBeforeWrap);

        videoPlayerSceneCurrentTimeLabel.setFont(videoPlayerSceneCurrentTimeLabelFontBeforeWrap);
        videoPlayerSceneFullTimeLabel.setFont(videoPlayerSceneFullTimeLabelFontBeforeWrap);
        videoPlayerSceneVolumeLabel.setFont(videoPlayerSceneVolumeLabelFontBeforeWrap);

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



    @FXML
    void initialize() {
        videoPlayerSceneVolumeSlider.setValue(0);
        videoPlayerSceneTimeSlider.setValue(0);

        videoPlayerSceneMediaView.fitWidthProperty().bind(videoPlayerSceneBorderPane.widthProperty());
        videoPlayerSceneMediaView.fitHeightProperty().bind(videoPlayerSceneBorderPane.heightProperty().subtract(videoPlayerSceneTopHBox.heightProperty()).subtract(videoPlayerSceneBotVBox.heightProperty()).subtract(10));

        videoPlayerSceneFullTimeLabel.prefWidthProperty().bind(videoPlayerSceneCurrentTimeLabel.prefWidthProperty());
        videoPlayerSceneFullTimeLabel.fontProperty().bind(videoPlayerSceneCurrentTimeLabel.fontProperty());

        videoPlayerSceneVolumeLabel.prefWidthProperty().bind(videoPlayerSceneCurrentTimeLabel.prefWidthProperty());
        videoPlayerSceneVolumeLabel.fontProperty().bind(videoPlayerSceneCurrentTimeLabel.fontProperty());

        videoPlayerSceneAnotherVideoButton.prefWidthProperty().bind(videoPlayerSceneBackButton.prefWidthProperty());
        videoPlayerSceneAnotherVideoButton.prefHeightProperty().bind(videoPlayerSceneBackButton.prefHeightProperty());
        videoPlayerSceneAnotherVideoButton.fontProperty().bind(videoPlayerSceneBackButton.fontProperty());

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
                videoPlayerSceneVolumeImageView.setImage(new Image(getClass().getResource("/images/without-volume-button.png").toString()));
                isVolumeOff = true;
            } else if(isVolumeOff) {
                videoPlayerSceneVolumeImageView.setImage(new Image(getClass().getResource("/images/volume-button.png").toString()));
                isVolumeOff = false;
            }
        });

        videoPlayerSceneBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if(!isTrackAndThumbInit) {
                trackInTimeSlider = videoPlayerSceneTimeSlider.lookup(".track");
                trackInVolumeSlider = videoPlayerSceneVolumeSlider.lookup(".track");
                thumbInTimeSlider = videoPlayerSceneTimeSlider.lookup(".thumb");
                thumbInVolumeSlider = videoPlayerSceneVolumeSlider.lookup(".thumb");
                isTrackAndThumbInit = true;
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
        });

    }
}
