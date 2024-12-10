package org.player.videoplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

        mainMenuStage.setTitle("KubSTU Education");
        mainMenuStage.getIcons().add(new Image("file:./src/main/resources/images/logo-video-player-white-background.png"));
        mainMenuStage.setScene(mainMenuScene);

        mainMenuStage.setMinHeight(MIN_HEIGHT_STAGE);
        mainMenuStage.setMinWidth(MIN_WIDTH_STAGE);

        mainMenuStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение действия");
            alert.setHeaderText("Вы действительно хотите закрыть программу?");
            alert.setContentText("Данное действие может привести к потере данных.");

            ButtonType yesButton = new ButtonType("Подвердить", ButtonType.OK.getButtonData());
            ButtonType noButton = new ButtonType("Отмена", ButtonType.CANCEL.getButtonData());

            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.initOwner(mainMenuStage);

            alert.showAndWait().ifPresent(response -> {
                if(response == noButton || response.getButtonData().isCancelButton()) {
                    event.consume();
                    return;
                }

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

                try {
                    if(VideoSelectionMenuController.isThreadForDownloadImageActive) {
                        VideoSelectionMenuController.threadForDownloadImage.interrupt();
                        VideoSelectionMenuController.threadForDownloadImage.join();
                    }
                    if(VideoSelectionMenuController.isThreadForSynchronizeActive) {
                        VideoSelectionMenuController.threadForSynchronize.interrupt();
                        VideoSelectionMenuController.threadForSynchronize.join();
                    }
                    if(VideoSelectionMenuController.isThreadForCreateVBoxBySubtopicActive) {
                        VideoSelectionMenuController.threadForCreateVBoxBySubtopic.interrupt();
                        VideoSelectionMenuController.threadForCreateVBoxBySubtopic.join();
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка закрытия приложения: " + e);
                }
                System.exit(0);
            });

        });

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