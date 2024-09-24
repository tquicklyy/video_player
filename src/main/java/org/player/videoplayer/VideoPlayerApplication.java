package org.player.videoplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayerApplication extends Application {

    public static final double MIN_WIDTH_STAGE = 706;
    public static final double MIN_HEIGHT_STAGE = 438;
    private Timer timerForMainMenuStageDragging = new Timer();
    private boolean isMainMenuStageDragging = false;

    @Override
    public void start(Stage mainMenuStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("main-menu-scene.fxml"));
        Scene mainMenuScene = new Scene(fxmlLoader.load());

        mainMenuStage.setTitle("Video player");
        mainMenuStage.getIcons().add(new Image("file:./src/main/resources/images/logo-video-player.png"));
        mainMenuStage.setScene(mainMenuScene);

        mainMenuStage.setMinHeight(MIN_HEIGHT_STAGE);
        mainMenuStage.setMinWidth(MIN_WIDTH_STAGE);

        ChangeListener<Number> mainMenuStagePositionChangeListener = (_, _, _) -> {
            if (!isMainMenuStageDragging) {
                isMainMenuStageDragging = true;
                mainMenuStage.setMinWidth(0);
                mainMenuStage.setMinHeight(0);
            }
            resetTimerForMainMenuStageDragging(mainMenuStage);
        };
        mainMenuStage.xProperty().addListener(mainMenuStagePositionChangeListener);
        mainMenuStage.yProperty().addListener(mainMenuStagePositionChangeListener);

        mainMenuStage.show();

    }

    private void resetTimerForMainMenuStageDragging(Stage stage) {
        timerForMainMenuStageDragging.cancel();
        timerForMainMenuStageDragging = new Timer();
        timerForMainMenuStageDragging.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    isMainMenuStageDragging = false;
                    stage.setMinWidth(MIN_WIDTH_STAGE);
                    stage.setMinHeight(MIN_HEIGHT_STAGE);
                });
            }
        }, 100);
    }

    public static void main(String[] args) {
        launch();
    }
}