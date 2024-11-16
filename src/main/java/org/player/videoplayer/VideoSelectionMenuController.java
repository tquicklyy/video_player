package org.player.videoplayer;

import com.jfoenix.controls.JFXTextArea;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class VideoSelectionMenuController {
    
    @FXML
    private BorderPane videoSelectionMenuBorderPane;

    public FlowPane getVideoSelectionMenuFlowPane() {
        return videoSelectionMenuFlowPane;
    }

    @FXML
    private FlowPane videoSelectionMenuFlowPane;

    public ComboBox<String> getVideoSelectionMenuLeftComboBox() {
        return videoSelectionMenuLeftComboBox;
    }

    @FXML
    private Label videoSelectionMenuLeftLabelUnderComboBox;

    public ComboBox<String> getVideoSelectionMenuRightComboBox() {
        return videoSelectionMenuRightComboBox;
    }

    @FXML
    private Label videoSelectionMenuLeftComboBoxPromptLabel;

    public Label getVideoSelectionMenuLeftComboBoxPromptLabel() {
        return videoSelectionMenuLeftComboBoxPromptLabel;
    }

    @FXML
    private Label videoSelectionMenuRightComboBoxPromptLabel;

    public Label getVideoSelectionMenuRightComboBoxPromptLabel() {
        return videoSelectionMenuRightComboBoxPromptLabel;
    }

    @FXML
    private ComboBox<String> videoSelectionMenuLeftComboBox;

    @FXML
    public Label videoSelectionMenuOfflineModeButton;

    @FXML
    private Label videoSelectionMenuBackButton;

    @FXML
    private HBox videoSelectionMenuTopHBox;

    @FXML
    private Label videoSelectionMenuConfirmLabel;

    @FXML
    private Label videoSelectionMenuCancelLabel;

    @FXML
    private VBox videoSelectionMenuDeleteVBox;

    @FXML
    private Label videoSelectionMenuDeleteTopLabel;

    @FXML
    private Label videoSelectionMenuDeleteCenterLabel;

    @FXML
    private ComboBox<String> videoSelectionMenuRightComboBox;

    @FXML
    private Label videoSelectionMenuRightLabelForComboBox;

    @FXML
    private ImageView videoSelectionMenuUpdate;

    @FXML
    private ScrollPane videoSelectionMenuScrollPane;
    
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

    private static Stage currentStage;
    private static Scene newScene;

    public static Thread threadForSynchronize;
    public static Thread threadForDownloadImage;
    public static Thread threadForCreateVBoxBySubtopic;
    public static Thread threadForCreateVBoxByDirectories;

    public static boolean isThreadForSynchronizeActive;
    public static boolean isThreadForDownloadImageActive;
    public static boolean isThreadForCreateVBoxBySubtopicActive;
    public static boolean isThreadForCreateVBoxByDirectoriesActive;
    public static boolean isLeftComboBoxUpdateWhileChangeScene;
    public static boolean isRightComboBoxUpdateWhileChangeScene;

    public static HashMap<String, VBox> mainVBoxHashMap;
    public static HashMap<String, VBox> secondVBoxHashMap;
    public static HashMap<String, ImageView> imageViewHashMap;
    public static HashMap<String, Label> nameOfVideoLabelHashMap;
    public static HashMap<String, HBox> hBoxHashMap;
    public static HashMap<String, Label> watchLabelHashMap;
    public static HashMap<String, ImageView> infoImageViewHashMap;
    public static HashMap<String, ImageView> deleteImageViewHashMap;
    public static HashMap<String, String> lastSubjectHashMap;
    public static HashMap<String, String> lastTopicHashMap;

    public static ObservableList<String> leftComboBox;
    public static ObservableList<String> rightComboBox;

    private PauseTransition pauseForDispose;

    public static VideoSelectionMenuController currentController;
    private VideoPlayerController videoPlayerControllerWhenSwitch;
    
    private File fileForDelete;
    private String subtopicForDelete;

    public static String subject;
    public static String topic;

    private static Alert alertForSomething;

    @FXML
    private void switchingToTheMainMenu(MouseEvent event) throws IOException {
        if(threadForSynchronize != null) threadForSynchronize.interrupt();
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("main-menu-scene.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
        currentStage.setScene(newScene);
    }

    private void openInfo(String textForArea, VideoSelectionMenuController currentController) {
        currentController.videoSelectionMenuInfoVBox.setVisible(true);
        currentController.videoSelectionMenuInfoVBox.setDisable(false);
        currentController.videoSelectionMenuInfoTextArea.setText(textForArea);
        currentController.videoSelectionMenuBorderPane.setDisable(true);
        currentController.videoSelectionMenuBorderPane.setOpacity(0.4);
    }

    @FXML
    private void closeInfo() {
        videoSelectionMenuInfoVBox.setVisible(false);
        videoSelectionMenuInfoVBox.setDisable(true);
        videoSelectionMenuBorderPane.setDisable(false);
        videoSelectionMenuBorderPane.setOpacity(1);
    }

    @FXML
    private void deleteVideo() {
        if(fileForDelete.delete()) {
            deleteImageViewHashMap.get(subtopicForDelete).setImage(new Image(getClass().getResource("/images/delete-black.png").toString()));
            alertForSomething = new Alert(Alert.AlertType.INFORMATION);
            alertForSomething.setHeaderText("Удаление видео");
            alertForSomething.setContentText("Видео успешно удалено!");
            if(VideoDownloadController.videoDownloadControllerHashMap.get(subtopicForDelete) != null) {
                VideoDownloadController.videoDownloadSceneCentralLabelHashMap.get(subtopicForDelete).setText("Видео не загружено");
                VideoDownloadController.videoDownloadSceneDownloadLabelHashMap.get(subtopicForDelete).setText("Скачать видео");
            }
            closeVideoSelectionMenuDeleteVBox();
        }
    }

    @FXML
    private void closeVideoSelectionMenuDeleteVBox() {
        videoSelectionMenuDeleteVBox.setVisible(false);
        videoSelectionMenuDeleteVBox.setDisable(true);
        videoSelectionMenuBorderPane.setDisable(false);
        fileForDelete = null;
    }

    @FXML
    public void offlineModeActivateSelectionScene() {
        DBInteraction.isOfflineMode = true;
        videoSelectionMenuLeftComboBox.setDisable(true);
        videoSelectionMenuRightComboBox.setDisable(true);
        videoSelectionMenuOfflineModeButton.setDisable(true);
        videoSelectionMenuLeftComboBoxPromptLabel.setVisible(true);
        videoSelectionMenuRightComboBoxPromptLabel.setVisible(true);
        videoSelectionMenuOfflineModeButton.setOpacity(1);
        videoSelectionMenuOfflineModeButton.setCursor(Cursor.DEFAULT);
        videoSelectionMenuOfflineModeButton.setText("Автономный режим включён");
        if(!videoSelectionMenuFlowPane.getChildren().isEmpty()) {
            videoSelectionMenuFlowPane.getChildren().clear();
        }
        videoSelectionMenuLeftComboBox.getItems().clear();
        videoSelectionMenuRightComboBox.getItems().clear();
        leftComboBoxFillInOfflineMode();
    }
    
    private void leftComboBoxFillInOfflineMode() {
        File materailsFile = new File(String.format("../Materials"));
        if(materailsFile.isDirectory()) {
            File[] filesInMaterials = materailsFile.listFiles();
            if(filesInMaterials.length == 0) {
                alertOn("Темы видео отсутствуют на вашем ПК.", "Отключите автономный режим, чтобы получить список тем.");
            } else {
                videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/loading-circle.gif").toString()));
                pauseForDispose = new PauseTransition(Duration.millis(750));
                pauseForDispose.setOnFinished(_ -> {
                    videoSelectionMenuUpdate.setDisable(true);
                    videoSelectionMenuLeftComboBox.setDisable(true);
                    videoSelectionMenuRightComboBox.setDisable(true);
                    videoSelectionMenuLeftComboBoxPromptLabel.setVisible(true);
                    videoSelectionMenuRightComboBoxPromptLabel.setVisible(true);

                    videoSelectionMenuFlowPane.getChildren().clear();
                    videoSelectionMenuRightComboBox.getItems().clear();
                    videoSelectionMenuLeftComboBox.getItems().clear();
                    for (File fileInMaterials :  filesInMaterials) {
                        videoSelectionMenuLeftComboBox.getItems().add(fileInMaterials.getName());
                    }
                    videoSelectionMenuLeftComboBox.setDisable(false);
                    leftComboBox = videoSelectionMenuLeftComboBox.getItems();
                    videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                    videoSelectionMenuUpdate.setDisable(false);
                });
                pauseForDispose.play();
            }
        }
    }

    private void rightComboBoxFillInOfflineMode() {
        videoSelectionMenuLeftComboBoxPromptLabel.setVisible(false);
        videoSelectionMenuLeftComboBox.setDisable(true);
        videoSelectionMenuRightComboBox.setDisable(true);
        File materailsThemesFile = new File(String.format("../Materials/%s", videoSelectionMenuLeftComboBox.getValue()));
        if(materailsThemesFile.isDirectory()) {
            File[] filesInMaterialsTheme = materailsThemesFile.listFiles();
            if(filesInMaterialsTheme.length == 0) {
                alertOn("Подтемы видео отсутствуют на вашем ПК.", "Отключите автономный режим, чтобы получить список подтем.");
            } else {
                videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/loading-circle.gif").toString()));
                pauseForDispose = new PauseTransition(Duration.millis(750));
                pauseForDispose.setOnFinished(_ -> {
                    videoSelectionMenuFlowPane.getChildren().clear();
                    videoSelectionMenuLeftComboBox.setDisable(true);
                    videoSelectionMenuRightComboBox.setDisable(true);
                    videoSelectionMenuRightComboBoxPromptLabel.setVisible(true);
                    videoSelectionMenuUpdate.setDisable(true);

                    videoSelectionMenuRightComboBox.getItems().clear();

                    for (File fileInMaterialsThemes :  filesInMaterialsTheme) {
                        videoSelectionMenuRightComboBox.getItems().add(fileInMaterialsThemes.getName());
                    }
                    videoSelectionMenuLeftComboBox.setDisable(false);
                    videoSelectionMenuRightComboBox.setDisable(false);
                    rightComboBox = videoSelectionMenuRightComboBox.getItems();
                    videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                    videoSelectionMenuUpdate.setDisable(false);
                });
                pauseForDispose.play();
            }
        }
    }

    public static void alertOn(String infoHeader, String infoContent) {
        Platform.runLater(() -> {
            alertForSomething = new Alert(Alert.AlertType.ERROR);
            alertForSomething.initOwner(currentStage);
            alertForSomething.initOwner(MainMenuController.currentStage);
            alertForSomething.setTitle("Ошибка получения данных.");
            alertForSomething.setHeaderText(infoHeader);
            alertForSomething.setContentText(infoContent);
            alertForSomething.showAndWait();
        });
    }
    
    @FXML
    public void startSynchronize() {
        DBInteraction.isOfflineMode = false;
        videoSelectionMenuOfflineModeButton.setText("Включить автономный режим");
        videoSelectionMenuOfflineModeButton.setCursor(Cursor.HAND);
        videoSelectionMenuOfflineModeButton.setVisible(false);
        videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/loading-circle.gif").toString()));
        videoSelectionMenuUpdate.setDisable(true);
        videoSelectionMenuLeftComboBox.setDisable(true);
        videoSelectionMenuRightComboBox.setDisable(true);
        videoSelectionMenuLeftComboBoxPromptLabel.setVisible(true);
        videoSelectionMenuRightComboBoxPromptLabel.setVisible(true);

        videoSelectionMenuFlowPane.getChildren().clear();
        videoSelectionMenuRightComboBox.getItems().clear();
        videoSelectionMenuLeftComboBox.getItems().clear();

        pauseForDispose = new PauseTransition(Duration.millis(750));
        pauseForDispose.setOnFinished(_ -> {
            threadForSynchronize = new Thread(() -> {
                DBInteraction.connectToDB();
                if(DBInteraction.isConn) {
                    DBInteraction.getSubjects(videoSelectionMenuLeftComboBox);
                    if(DBInteraction.isConn) {
                        Platform.runLater(() -> {
                            videoSelectionMenuLeftComboBox.setDisable(false);
                            leftComboBox = videoSelectionMenuLeftComboBox.getItems();
                        });
                    } else {
                        Platform.runLater(() -> {
                            videoSelectionMenuOfflineModeButton.setDisable(false);
                            videoSelectionMenuOfflineModeButton.setVisible(true);
                            videoSelectionMenuLeftComboBox.setDisable(true);
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        videoSelectionMenuOfflineModeButton.setDisable(false);
                        videoSelectionMenuOfflineModeButton.setVisible(true);
                        videoSelectionMenuLeftComboBox.setDisable(true);
                    });
                }
                Platform.runLater(() -> {
                    videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                    videoSelectionMenuUpdate.setDisable(false);
                });
                isThreadForSynchronizeActive = false;
            });
            isThreadForSynchronizeActive = true;
            threadForSynchronize.start();
        });
        pauseForDispose.play();
    }

    private void SynchronizeRightComboBox() {
        videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/loading-circle.gif").toString()));
        videoSelectionMenuOfflineModeButton.setDisable(true);
        videoSelectionMenuOfflineModeButton.setVisible(false);
        videoSelectionMenuFlowPane.getChildren().clear();
        videoSelectionMenuLeftComboBox.setDisable(true);
        videoSelectionMenuRightComboBox.setDisable(true);
        videoSelectionMenuRightComboBoxPromptLabel.setVisible(true);
        videoSelectionMenuLeftComboBoxPromptLabel.setVisible(false);
        videoSelectionMenuUpdate.setDisable(true);

        videoSelectionMenuRightComboBox.getItems().clear();

        pauseForDispose = new PauseTransition(Duration.millis(750));
        pauseForDispose.setOnFinished(_ -> {
            threadForSynchronize = new Thread(() -> {
                DBInteraction.getTopics(DBInteraction.idOfSubjects.get(videoSelectionMenuLeftComboBox.getValue()), videoSelectionMenuRightComboBox);
                if(DBInteraction.isConn) {
                    Platform.runLater(() -> {
                        videoSelectionMenuLeftComboBox.setDisable(false);
                        videoSelectionMenuRightComboBox.setDisable(false);
                        rightComboBox = videoSelectionMenuRightComboBox.getItems();
                    });
                } else {
                    Platform.runLater(() -> {
                        videoSelectionMenuOfflineModeButton.setDisable(false);
                        videoSelectionMenuOfflineModeButton.setVisible(true);
                        videoSelectionMenuLeftComboBox.setDisable(false);
                    });
                }
                Platform.runLater(() -> {
                    videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                    videoSelectionMenuUpdate.setDisable(false);
                });
                isThreadForSynchronizeActive = false;
            });
            isThreadForSynchronizeActive = true;
            threadForSynchronize.start();
        });
        pauseForDispose.play();
    }

    private void updateSizes(Number newValue) {
        if (newValue.intValue() < 978) {
            videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 13));

            videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 13));

            videoSelectionMenuLeftComboBox.setPrefWidth(246);
            videoSelectionMenuLeftComboBox.setPrefHeight(27);

            videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 13px;");

            videoSelectionMenuRightComboBox.setPrefWidth(246);
            videoSelectionMenuRightComboBox.setPrefHeight(27);
            videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 13px;");

            if(mainVBoxHashMap == null) return;
            for(ImageView videoSelectionMenuImageView: imageViewHashMap.values()) {
                videoSelectionMenuImageView.setFitHeight(150);
                videoSelectionMenuImageView.setFitWidth(280);
            }

            for(Label videoSelectionMenuNameOfVideoLabel: nameOfVideoLabelHashMap.values()) {
                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            }

            for(Label videoSelectionMenuLabelForWatch: watchLabelHashMap.values()) {
                videoSelectionMenuLabelForWatch.setPrefWidth(179);
                videoSelectionMenuLabelForWatch.setPrefHeight(30);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            }

            for(ImageView videoSelectionMenuImageViewForDelete: deleteImageViewHashMap.values()) {
                videoSelectionMenuImageViewForDelete.setFitHeight(31);
                videoSelectionMenuImageViewForDelete.setFitWidth(37);
            }

        } else if (newValue.intValue() < 1251) {
            videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));

            videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));

            videoSelectionMenuLeftComboBox.setPrefWidth(371);
            videoSelectionMenuLeftComboBox.setPrefHeight(32);
            videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 14.6px;");

            videoSelectionMenuRightComboBox.setPrefWidth(371);
            videoSelectionMenuRightComboBox.setPrefHeight(32);
            videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 14.6px;");

            if(mainVBoxHashMap == null) return;
            for(ImageView videoSelectionMenuImageView: imageViewHashMap.values()) {
                videoSelectionMenuImageView.setFitHeight(206);
                videoSelectionMenuImageView.setFitWidth(358);
            }

            for(Label videoSelectionMenuNameOfVideoLabel: nameOfVideoLabelHashMap.values()) {
                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));
            }

            for(Label videoSelectionMenuLabelForWatch: watchLabelHashMap.values()) {
                videoSelectionMenuLabelForWatch.setPrefWidth(268);
                videoSelectionMenuLabelForWatch.setPrefHeight(30);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 14.6));
            }

            for(ImageView videoSelectionMenuImageViewForDelete: deleteImageViewHashMap.values()) {
                videoSelectionMenuImageViewForDelete.setFitHeight(32);
                videoSelectionMenuImageViewForDelete.setFitWidth(38);
            }
        } else if (newValue.intValue() < 1624) {
            videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));

            videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));

            videoSelectionMenuLeftComboBox.setPrefWidth(496);
            videoSelectionMenuLeftComboBox.setPrefHeight(34.4);
            videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 16.2px;");

            videoSelectionMenuRightComboBox.setPrefWidth(496);
            videoSelectionMenuRightComboBox.setPrefHeight(34.4);
            videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 16.2px;");

            if(mainVBoxHashMap == null) return;
            for(ImageView videoSelectionMenuImageView: imageViewHashMap.values()) {
                videoSelectionMenuImageView.setFitHeight(278);
                videoSelectionMenuImageView.setFitWidth(482);
            }

            for(Label videoSelectionMenuNameOfVideoLabel: nameOfVideoLabelHashMap.values()) {
                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));
            }

            for(Label videoSelectionMenuLabelForWatch: watchLabelHashMap.values()) {
                videoSelectionMenuLabelForWatch.setPrefWidth(390);
                videoSelectionMenuLabelForWatch.setPrefHeight(31);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 16.2));
            }

            for(ImageView videoSelectionMenuImageViewForDelete: deleteImageViewHashMap.values()) {
                videoSelectionMenuImageViewForDelete.setFitHeight(33);
                videoSelectionMenuImageViewForDelete.setFitWidth(39);
            }
        } else {
            videoSelectionMenuLeftLabelUnderComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

            videoSelectionMenuRightLabelForComboBox.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));

            videoSelectionMenuLeftComboBox.setPrefWidth(621);
            videoSelectionMenuLeftComboBox.setPrefHeight(36.8);
            videoSelectionMenuLeftComboBox.setStyle("-fx-font-size: 17.8px;");

            videoSelectionMenuRightComboBox.setPrefWidth(621);
            videoSelectionMenuRightComboBox.setPrefHeight(36.8);
            videoSelectionMenuRightComboBox.setStyle("-fx-font-size: 17.8px;");

            if(mainVBoxHashMap == null) return;
            for(ImageView videoSelectionMenuImageView: imageViewHashMap.values()) {
                videoSelectionMenuImageView.setFitHeight(418);
                videoSelectionMenuImageView.setFitWidth(707);
            }

            for(Label videoSelectionMenuNameOfVideoLabel: nameOfVideoLabelHashMap.values()) {
                videoSelectionMenuNameOfVideoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));
            }

            for(Label videoSelectionMenuLabelForWatch: watchLabelHashMap.values()) {
                videoSelectionMenuLabelForWatch.setPrefWidth(613);
                videoSelectionMenuLabelForWatch.setPrefHeight(32);
                videoSelectionMenuLabelForWatch.setFont(Font.font("Arial", FontWeight.BOLD, 17.8));
            }

            for(ImageView videoSelectionMenuImageViewForDelete: deleteImageViewHashMap.values()) {
                videoSelectionMenuImageViewForDelete.setFitHeight(34);
                videoSelectionMenuImageViewForDelete.setFitWidth(40);
            }
        }
    }

    public void createVBoxInOfflineMode(String subject, String topic) {
        videoSelectionMenuRightComboBoxPromptLabel.setVisible(false);
        videoSelectionMenuRightComboBox.setDisable(true);
        videoSelectionMenuLeftComboBox.setDisable(true);
        VBox mainVBox;
        VBox secondVBox;
        ImageView imageViewOfVideo;
        Label nameOfVideo;
        HBox hBoxWithLabels;
        Label watchLabel;
        ImageView infoImageView;
        ImageView deleteImageView;

        mainVBoxHashMap = new HashMap<>();
        imageViewHashMap = new HashMap<>();
        secondVBoxHashMap = new HashMap<>();
        nameOfVideoLabelHashMap = new HashMap<>();
        hBoxHashMap = new HashMap<>();
        watchLabelHashMap = new HashMap<>();
        infoImageViewHashMap = new HashMap<>();
        deleteImageViewHashMap = new HashMap<>();
        lastSubjectHashMap = new HashMap<>();
        lastTopicHashMap = new HashMap<>();
        int i = 0;

        File directoryWithSubtopic = new File(String.format("../Materials/%s/%s", subject, topic));
        File[] subtopicFiles = directoryWithSubtopic.listFiles();
        if(subtopicFiles.length == 0) {
            alertOn("Видео отсутствуют на вашем ПК.", "Отключите автономный режим, чтобы скачать видео.");
        } else {
            String subtopic;
            for(File subtopicFile: subtopicFiles) {
                subtopic = subtopicFile.getName();
                mainVBoxHashMap.put(subtopic, new VBox());
                imageViewHashMap.put(subtopic, new ImageView());
                secondVBoxHashMap.put(subtopic, new VBox());
                nameOfVideoLabelHashMap.put(subtopic, new Label());
                hBoxHashMap.put(subtopic, new HBox());
                watchLabelHashMap.put(subtopic, new Label());
                infoImageViewHashMap.put(subtopic, new ImageView());
                deleteImageViewHashMap.put(subtopic, new ImageView());

                mainVBox = mainVBoxHashMap.get(subtopic);
                secondVBox = secondVBoxHashMap.get(subtopic);
                imageViewOfVideo = imageViewHashMap.get(subtopic);

                nameOfVideo = nameOfVideoLabelHashMap.get(subtopic);
                hBoxWithLabels = hBoxHashMap.get(subtopic);
                watchLabel = watchLabelHashMap.get(subtopic);
                infoImageView = infoImageViewHashMap.get(subtopic);
                deleteImageView = deleteImageViewHashMap.get(subtopic);

                mainVBox.getChildren().add(imageViewOfVideo);
                mainVBox.getChildren().add(secondVBox);

                secondVBox.getChildren().add(nameOfVideo);
                secondVBox.getChildren().add(hBoxWithLabels);

                hBoxWithLabels.getChildren().add(watchLabel);
                hBoxWithLabels.getChildren().add(infoImageView);
                hBoxWithLabels.getChildren().add(deleteImageView);

                mainVBox.setAlignment(Pos.CENTER);
                mainVBox.getStylesheets().add(getClass().getResource("/cssStyle/video-selection-menu-red-vbox.css").toString());
                mainVBox.getStyleClass().add("root");

                VBox.setMargin(imageViewOfVideo, new Insets(7));
                imageViewOfVideo.setPreserveRatio(true);

                secondVBox.setAlignment(Pos.CENTER);
                secondVBox.setSpacing(10);
                VBox.setMargin(secondVBox, new Insets(10,0,10,0));

                nameOfVideo.setStyle("-fx-background-color: #FF4040;");
                nameOfVideo.setAlignment(Pos.CENTER);
                nameOfVideo.setText(subtopic);
                nameOfVideo.setTextFill(Color.WHITE);

                VBox.setMargin(hBoxWithLabels , new Insets(0,10,0,10));
                hBoxWithLabels.setSpacing(10);
                hBoxWithLabels.setAlignment(Pos.CENTER);

                watchLabel.getStylesheets().add(getClass().getResource("/cssStyle/video-selection-menu-label-for-watch.css").toString());
                watchLabel.getStyleClass().add("custom-label");
                watchLabel.setAlignment(Pos.CENTER);
                watchLabel.setText("Посмотреть");
                watchLabel.minHeightProperty().bind(watchLabel.prefHeightProperty());
                watchLabel.maxHeightProperty().bind(watchLabel.prefHeightProperty());
                watchLabel.minWidthProperty().bind(watchLabel.prefWidthProperty());
                watchLabel.maxWidthProperty().bind(watchLabel.prefWidthProperty());
                watchLabel.setCursor(Cursor.HAND);

                String finalSubtopic = subtopic;
                watchLabel.setOnMousePressed((MouseEvent event) -> {
                    currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    Scene newScene = null;
                    FXMLLoader fxmlLoader;

                    lastSubjectHashMap.put(finalSubtopic, videoSelectionMenuLeftComboBox.getValue());
                    lastTopicHashMap.put(finalSubtopic, videoSelectionMenuRightComboBox.getValue());

                    File videoFile = new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, finalSubtopic,finalSubtopic));

                    if(!videoFile.exists()) {
                        alertOn("Видео не скачано на ПК.", "Отключите автономный режим, чтобы скачать видео.");

                    } else {
                        fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-player-scene.fxml"));
                        try {
                            newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        videoPlayerControllerWhenSwitch = fxmlLoader.getController();
                        videoPlayerControllerWhenSwitch.subtopic = finalSubtopic;
                        videoPlayerControllerWhenSwitch.setPreviousScene("video-selection-menu-scene.fxml");
                        videoPlayerControllerWhenSwitch.isEducationVideo = true;
                        Scene finalNewScene = newScene;
                        Platform.runLater(() -> {
                            videoPlayerControllerWhenSwitch.setTrackInTimeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneTimeSlider().lookup(".track"));
                            videoPlayerControllerWhenSwitch.setTrackInVolumeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneVolumeSlider().lookup(".track"));
                            videoPlayerControllerWhenSwitch.setThumbInTimeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneTimeSlider().lookup(".thumb"));
                            videoPlayerControllerWhenSwitch.setThumbInVolumeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneVolumeSlider().lookup(".thumb"));
                            videoPlayerControllerWhenSwitch.isEducationVideo = true;
                            videoPlayerControllerWhenSwitch.updateSizes(finalNewScene.getHeight());
                            videoPlayerControllerWhenSwitch.urlOfVideo = videoFile.toURI().toString();
                            videoPlayerControllerWhenSwitch.doDictionaryOfPathToVideosInCurrentDirectory(new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, finalSubtopic, finalSubtopic)).getParent());
                            videoPlayerControllerWhenSwitch.restartPlayer();

                        });
                        currentStage.setScene(newScene);
                    }

                });

                infoImageView.setImage(new Image(getClass().getResource("/images/info-red-i.png").toString()));
                infoImageView.setPreserveRatio(true);
                infoImageView.setCursor(Cursor.HAND);

                infoImageView.setOnMousePressed((MouseEvent _) -> {
                    String info = "";
                    String readLine;
                    try(BufferedReader reader = new BufferedReader(new FileReader(new File(String.format("../Materials/%s/%s/%s/%s.json",subject,topic,finalSubtopic,finalSubtopic))))) {
                        while ((readLine = reader.readLine()) != null) {
                            info += readLine + "\n";
                        }
                    } catch (Exception e) {
                        String index = "";
                        for(char symbolInNameOfFile: new File(String.format("../Materials/%s/%s/%s",subject,topic,finalSubtopic)).getName().toCharArray()) {
                            if(Character.isDigit(symbolInNameOfFile)) {
                                index += symbolInNameOfFile;
                            } else {
                                break;
                            }
                        }
                        info = String.format("Глава: %s\nТема: %s\nНомер видео: %s\nНазвание видео: %s\nСсылка на видео в telegram: %s\nСсылка на видео в ВК: %s\nСсылка на бота в telegram: @kubstu_education_bot\nСсылка на группу ВК: https://vk.com/club227646062", subject, topic, index, "Неизвестно", "Неизвестно", "Неизвестно");
                        System.out.println(e.getMessage());
                    }
                    openInfo(info, currentController);
                });

                File currentFile = new File(String.format("../Materials/%s/%s/%s/%s.mp4",subject, topic, subtopic, subtopic));
                if (currentFile.exists()) {
                    deleteImageView.setImage(new Image(getClass().getResource("/images/delete-red.png").toString()));
                } else {
                    deleteImageView.setImage(new Image(getClass().getResource("/images/delete-black.png").toString()));
                }
                deleteImageView.setPreserveRatio(true);
                deleteImageView.setCursor(Cursor.HAND);
                deleteImageView.setOnMousePressed((MouseEvent _) -> {
                    if(currentFile.exists()) {
                        currentController.subtopicForDelete = finalSubtopic;
                        currentController.fileForDelete = currentFile;
                        currentController.videoSelectionMenuDeleteVBox.setDisable(false);
                        currentController.videoSelectionMenuDeleteVBox.setVisible(true);
                        currentController.videoSelectionMenuBorderPane.setDisable(true);
                    }
                });

                infoImageView.fitHeightProperty().bind(deleteImageView.fitHeightProperty());
                infoImageView.fitWidthProperty().bind(deleteImageView.fitWidthProperty());
            }
        }

    }

    public void createVBoxBySubtopicInSelectionScene(String subject, String topic) {
        if(!DBInteraction.isConn) return;
        VBox mainVBox;
        VBox secondVBox;
        ImageView imageViewOfVideo;
        Label nameOfVideo;
        HBox hBoxWithLabels;
        Label watchLabel;
        ImageView infoImageView;
        ImageView deleteImageView;

        mainVBoxHashMap = new HashMap<>();
        imageViewHashMap = new HashMap<>();
        secondVBoxHashMap = new HashMap<>();
        nameOfVideoLabelHashMap = new HashMap<>();
        hBoxHashMap = new HashMap<>();
        watchLabelHashMap = new HashMap<>();
        infoImageViewHashMap = new HashMap<>();
        deleteImageViewHashMap = new HashMap<>();
        lastSubjectHashMap = new HashMap<>();
        lastTopicHashMap = new HashMap<>();

        for(String subtopic: DBInteraction.nameOfSubtopics) {
            mainVBoxHashMap.put(subtopic, new VBox());
            imageViewHashMap.put(subtopic, new ImageView());
            secondVBoxHashMap.put(subtopic, new VBox());
            nameOfVideoLabelHashMap.put(subtopic, new Label());
            hBoxHashMap.put(subtopic, new HBox());
            watchLabelHashMap.put(subtopic, new Label());
            infoImageViewHashMap.put(subtopic, new ImageView());
            deleteImageViewHashMap.put(subtopic, new ImageView());

            mainVBox = mainVBoxHashMap.get(subtopic);
            secondVBox = secondVBoxHashMap.get(subtopic);
            imageViewOfVideo = imageViewHashMap.get(subtopic);
            nameOfVideo = nameOfVideoLabelHashMap.get(subtopic);
            hBoxWithLabels = hBoxHashMap.get(subtopic);
            watchLabel = watchLabelHashMap.get(subtopic);
            infoImageView = infoImageViewHashMap.get(subtopic);
            deleteImageView = deleteImageViewHashMap.get(subtopic);

            mainVBox.getChildren().add(imageViewOfVideo);
            mainVBox.getChildren().add(secondVBox);

            secondVBox.getChildren().add(nameOfVideo);
            secondVBox.getChildren().add(hBoxWithLabels);

            hBoxWithLabels.getChildren().add(watchLabel);
            hBoxWithLabels.getChildren().add(infoImageView);
            hBoxWithLabels.getChildren().add(deleteImageView);

            mainVBox.setAlignment(Pos.CENTER);
            mainVBox.getStylesheets().add(getClass().getResource("/cssStyle/video-selection-menu-red-vbox.css").toString());
            mainVBox.getStyleClass().add("root");

            VBox.setMargin(imageViewOfVideo, new Insets(7));
            imageViewOfVideo.setPreserveRatio(true);

            secondVBox.setAlignment(Pos.CENTER);
            secondVBox.setSpacing(10);
            VBox.setMargin(secondVBox, new Insets(10,0,10,0));

            nameOfVideo.setStyle("-fx-background-color: #FF4040;");
            nameOfVideo.setAlignment(Pos.CENTER);
            nameOfVideo.setText(subtopic);
            nameOfVideo.setTextFill(Color.WHITE);

            VBox.setMargin(hBoxWithLabels , new Insets(0,10,0,10));
            hBoxWithLabels.setSpacing(10);
            hBoxWithLabels.setAlignment(Pos.CENTER);

            watchLabel.getStylesheets().add(getClass().getResource("/cssStyle/video-selection-menu-label-for-watch.css").toString());
            watchLabel.getStyleClass().add("custom-label");
            watchLabel.setAlignment(Pos.CENTER);
            watchLabel.setText("Посмотреть");
            watchLabel.minHeightProperty().bind(watchLabel.prefHeightProperty());
            watchLabel.maxHeightProperty().bind(watchLabel.prefHeightProperty());
            watchLabel.minWidthProperty().bind(watchLabel.prefWidthProperty());
            watchLabel.maxWidthProperty().bind(watchLabel.prefWidthProperty());
            watchLabel.setCursor(Cursor.HAND);

            watchLabel.setOnMousePressed((MouseEvent event) -> {
                Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene newScene = null;
                FXMLLoader fxmlLoader;

                lastSubjectHashMap.put(subtopic, videoSelectionMenuLeftComboBox.getValue());
                lastTopicHashMap.put(subtopic, videoSelectionMenuRightComboBox.getValue());

                if(DBInteraction.isVideoDownloading.get(subtopic) != null && DBInteraction.isVideoDownloading.get(subtopic)) {
                    fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-download-scene.fxml"));
                    try {
                        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    VideoDownloadController videoDownloadControllerWhenSwitch = fxmlLoader.getController();
                    VideoDownloadController.videoDownloadControllerHashMap.put(subtopic, videoDownloadControllerWhenSwitch);
                    videoDownloadControllerWhenSwitch.videoDownloadSceneCentralVBox.getChildren().clear();
                    videoDownloadControllerWhenSwitch.videoDownloadSceneCentralVBox.getChildren().add(VideoDownloadController.videoDownloadSceneRedVBoxHashMap.get(subtopic));

                    String info = "";
                    String readLine;
                    try(BufferedReader reader = new BufferedReader(new FileReader(new File(String.format("../Materials/%s/%s/%s/%s.json",subject,topic,subtopic,subtopic))))) {
                        while ((readLine = reader.readLine()) != null) {
                            info += readLine + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    videoDownloadControllerWhenSwitch.videoDownloadSceneInfoTextArea.setText(info);

                    videoDownloadControllerWhenSwitch.linkForWatch = DBInteraction.videoUrl.get(subtopic);
                    videoDownloadControllerWhenSwitch.subject = subject;
                    videoDownloadControllerWhenSwitch.topic = topic;
                    currentStage.setScene(newScene);
                    Scene finalNewScene1 = newScene;
                    Platform.runLater(() -> {
                        videoDownloadControllerWhenSwitch.updateSizes(finalNewScene1.getHeight());
                    });

                } else if(DBInteraction.isVideoDownloading.get(subtopic) == null && !(new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic)).exists()) || DBInteraction.isVideoDownloading.get(subtopic) != null && !(new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic)).exists())) {
                    fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-download-scene.fxml"));
                    try {
                        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    VideoDownloadController videoDownloadControllerWhenSwitch = fxmlLoader.getController();
                    if(VideoDownloadController.videoDownloadSceneRedVBoxHashMap.get(subtopic) == null) {
                        VideoDownloadController.createVBoxForDownload(subject, topic, subtopic);
                    }
                    VideoDownloadController.videoDownloadControllerHashMap.put(subtopic, videoDownloadControllerWhenSwitch);
                    videoDownloadControllerWhenSwitch.videoDownloadSceneCentralVBox.getChildren().clear();
                    videoDownloadControllerWhenSwitch.videoDownloadSceneCentralVBox.getChildren().add(VideoDownloadController.videoDownloadSceneRedVBoxHashMap.get(subtopic));

                    String info = "";
                    String readLine;
                    try(BufferedReader reader = new BufferedReader(new FileReader(new File(String.format("../Materials/%s/%s/%s/%s.json",subject,topic,subtopic,subtopic))))) {
                        while ((readLine = reader.readLine()) != null) {
                            info += readLine + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    videoDownloadControllerWhenSwitch.videoDownloadSceneInfoTextArea.setText(info);

                    videoDownloadControllerWhenSwitch.linkForWatch = DBInteraction.videoUrl.get(subtopic);
                    videoDownloadControllerWhenSwitch.subject = subject;
                    videoDownloadControllerWhenSwitch.topic = topic;
                    currentStage.setScene(newScene);
                    Scene finalNewScene1 = newScene;
                    Platform.runLater(() -> {
                        videoDownloadControllerWhenSwitch.updateSizes(finalNewScene1.getHeight());
                    });
                } else {
                    fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-player-scene.fxml"));
                    try {

                        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    videoPlayerControllerWhenSwitch = fxmlLoader.getController();
                    videoPlayerControllerWhenSwitch.subtopic = subtopic;
                    videoPlayerControllerWhenSwitch.setPreviousScene("video-selection-menu-scene.fxml");
                    Scene finalNewScene = newScene;
                    Platform.runLater(() -> {
                        videoPlayerControllerWhenSwitch.setTrackInTimeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneTimeSlider().lookup(".track"));
                        videoPlayerControllerWhenSwitch.setTrackInVolumeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneVolumeSlider().lookup(".track"));
                        videoPlayerControllerWhenSwitch.setThumbInTimeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneTimeSlider().lookup(".thumb"));
                        videoPlayerControllerWhenSwitch.setThumbInVolumeSlider(videoPlayerControllerWhenSwitch.getVideoPlayerSceneVolumeSlider().lookup(".thumb"));
                        videoPlayerControllerWhenSwitch.isEducationVideo = true;
                        videoPlayerControllerWhenSwitch.videoPlayerSceneAnotherVideoButton.setDisable(true);
                        videoPlayerControllerWhenSwitch.videoPlayerSceneAnotherVideoButton.setVisible(false);
                        videoPlayerControllerWhenSwitch.videoPlayerSceneBackButton.setText("Список видео");
                        videoPlayerControllerWhenSwitch.updateSizes(finalNewScene.getHeight());
                        videoPlayerControllerWhenSwitch.urlOfVideo = new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic)).toURI().toString();
                        videoPlayerControllerWhenSwitch.doDictionaryOfPathToVideosInCurrentDirectory(new File(String.format("../Materials/%s/%s/%s/%s.mp4", subject, topic, subtopic,subtopic)).getParent());
                        videoPlayerControllerWhenSwitch.restartPlayer();
                    });
                    currentStage.setScene(newScene);
                }

            });

            infoImageView.setImage(new Image(getClass().getResource("/images/info-red-i.png").toString()));
            infoImageView.setPreserveRatio(true);
            infoImageView.setCursor(Cursor.HAND);

            infoImageView.setOnMousePressed((MouseEvent _) -> {
                String info = "";
                String readLine;
                try(BufferedReader reader = new BufferedReader(new FileReader(new File(String.format("../Materials/%s/%s/%s/%s.json",subject,topic,subtopic,subtopic))))) {
                    while ((readLine = reader.readLine()) != null) {
                        info += readLine + "\n";
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                openInfo(info, currentController);
            });

            File currentFile = new File(String.format("../Materials/%s/%s/%s/%s.mp4",subject, topic, subtopic, subtopic));
            if (currentFile.exists()) {
                deleteImageView.setImage(new Image(getClass().getResource("/images/delete-red.png").toString()));
            } else {
                deleteImageView.setImage(new Image(getClass().getResource("/images/delete-black.png").toString()));
            }
            deleteImageView.setPreserveRatio(true);
            deleteImageView.setCursor(Cursor.HAND);
            deleteImageView.setOnMousePressed((MouseEvent _) -> {
                if(currentFile.exists()) {
                    currentController.subtopicForDelete = subtopic;
                    currentController.fileForDelete = currentFile;
                    currentController.videoSelectionMenuDeleteVBox.setDisable(false);
                    currentController.videoSelectionMenuDeleteVBox.setVisible(true);
                    currentController.videoSelectionMenuBorderPane.setDisable(true);
                }
            });

            infoImageView.fitHeightProperty().bind(deleteImageView.fitHeightProperty());
            infoImageView.fitWidthProperty().bind(deleteImageView.fitWidthProperty());
        }
    }

    public static void displayVBox(FlowPane videoSelectionMenuFlowPane) {
        VBox vBox;
        if(!DBInteraction.isOfflineMode) {
            for (String subtopic : DBInteraction.nameOfSubtopics) {
                vBox = mainVBoxHashMap.get(subtopic);
                videoSelectionMenuFlowPane.getChildren().add(vBox);
            }
        } else {
            File directoryWithSubtopic = new File(String.format("../Materials/%s/%s", subject, topic));
            File[] subtopicFiles = directoryWithSubtopic.listFiles();
            for(File subtopicFile: subtopicFiles) {
                vBox = mainVBoxHashMap.get(subtopicFile.getName());
                videoSelectionMenuFlowPane.getChildren().add(vBox);
            }
        }
    }

    public static void setImages(String subject, String topic) {
        ImageView imageViewOfVideo;
        if(!DBInteraction.isOfflineMode) {
            for (String subtopic : DBInteraction.nameOfSubtopics) {
                try {
                    imageViewOfVideo = imageViewHashMap.get(subtopic);
                    imageViewOfVideo.setImage(new Image(new File(String.format("../Materials/%s/%s/%s/%s.png", subject, topic, subtopic, subtopic)).toURI().toString()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            File directoryWithSubtopic = new File(String.format("../Materials/%s/%s", subject, topic));
            File[] subtopicFiles = directoryWithSubtopic.listFiles();
            File imageForSet;
            for(File subtopicFile: subtopicFiles) {
                try {
                    imageViewOfVideo = imageViewHashMap.get(subtopicFile.getName());
                    imageForSet = new File((String.format("../Materials/%s/%s/%s/%s.png", subject, topic, subtopicFile.getName(), subtopicFile.getName())));
                    if(imageForSet.exists()) {
                        System.out.println(1);
                        imageViewOfVideo.setImage(new Image(imageForSet.toURI().toString()));
                    } else {
                        System.out.println(2);
                        imageViewOfVideo.setImage(new Image(VideoSelectionMenuController.class.getResource("/images/logo-for-undownload-video.png").toURI().toString()));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @FXML
    void initialize() {
        videoSelectionMenuLeftComboBoxPromptLabel.setOpacity(1);
        videoSelectionMenuRightComboBoxPromptLabel.setOpacity(1);

        videoSelectionMenuBorderPane.prefWidthProperty().bind(videoSelectionMenuStackPane.widthProperty());
        videoSelectionMenuBorderPane.prefHeightProperty().bind(videoSelectionMenuStackPane.heightProperty());

        videoSelectionMenuUpdate.fitWidthProperty().bind(videoSelectionMenuBackButton.heightProperty());
        videoSelectionMenuUpdate.fitHeightProperty().bind(videoSelectionMenuBackButton.heightProperty());

        videoSelectionMenuFlowPane.prefWidthProperty().bind(videoSelectionMenuScrollPane.widthProperty());
        videoSelectionMenuFlowPane.prefHeightProperty().bind(videoSelectionMenuScrollPane.heightProperty());

        videoSelectionMenuLeftComboBoxPromptLabel.prefWidthProperty().bind(videoSelectionMenuLeftComboBox.widthProperty());
        videoSelectionMenuLeftComboBoxPromptLabel.prefHeightProperty().bind(videoSelectionMenuLeftComboBox.heightProperty());

        videoSelectionMenuRightComboBoxPromptLabel.prefWidthProperty().bind(videoSelectionMenuRightComboBox.widthProperty());
        videoSelectionMenuRightComboBoxPromptLabel.prefHeightProperty().bind(videoSelectionMenuRightComboBox.heightProperty());

        videoSelectionMenuLeftComboBoxPromptLabel.fontProperty().bind(videoSelectionMenuLeftLabelUnderComboBox.fontProperty());
        videoSelectionMenuRightComboBoxPromptLabel.fontProperty().bind(videoSelectionMenuRightLabelForComboBox.fontProperty());

        videoSelectionMenuCancelLabel.prefWidthProperty().bind(videoSelectionMenuConfirmLabel.prefWidthProperty());
        videoSelectionMenuCancelLabel.prefHeightProperty().bind(videoSelectionMenuConfirmLabel.prefHeightProperty());
        videoSelectionMenuCancelLabel.fontProperty().bind(videoSelectionMenuConfirmLabel.fontProperty());

        videoSelectionMenuDeleteCenterLabel.fontProperty().bind(videoSelectionMenuDeleteTopLabel.fontProperty());
        videoSelectionMenuDeleteCenterLabel.prefWidthProperty().bind(videoSelectionMenuDeleteVBox.prefWidthProperty());

        videoSelectionMenuDeleteTopLabel.prefWidthProperty().bind(videoSelectionMenuDeleteVBox.prefWidthProperty());

        videoSelectionMenuOfflineModeButton.prefHeightProperty().bind(videoSelectionMenuBackButton.prefHeightProperty());
        videoSelectionMenuOfflineModeButton.fontProperty().bind(videoSelectionMenuBackButton.fontProperty());

        videoSelectionMenuLeftComboBox.valueProperty().addListener((_,_,newValue) -> {
            if(newValue != null) {
                if(isLeftComboBoxUpdateWhileChangeScene) {
                    isLeftComboBoxUpdateWhileChangeScene = false;
                    return;
                }
                if(!DBInteraction.isOfflineMode) {
                    SynchronizeRightComboBox();
                } else {
                    rightComboBoxFillInOfflineMode();
                }
            }
        });

        videoSelectionMenuRightComboBox.valueProperty().addListener((_,_,newValue) -> {
            if(newValue != null) {
                if(isRightComboBoxUpdateWhileChangeScene) {
                    isRightComboBoxUpdateWhileChangeScene = false;
                    return;
                }
                if(!DBInteraction.isOfflineMode) {
                    videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/loading-circle.gif").toString()));
                    videoSelectionMenuOfflineModeButton.setDisable(true);
                    videoSelectionMenuOfflineModeButton.setVisible(false);
                    videoSelectionMenuLeftComboBox.setDisable(true);
                    videoSelectionMenuRightComboBox.setDisable(true);

                    videoSelectionMenuUpdate.setDisable(true);
                    videoSelectionMenuRightComboBoxPromptLabel.setVisible(false);
                    videoSelectionMenuFlowPane.getChildren().clear();

                    pauseForDispose = new PauseTransition(Duration.millis(750));
                    pauseForDispose.setOnFinished(_ -> {
                        threadForSynchronize = new Thread(() -> {
                            DBInteraction.getSubtopics(videoSelectionMenuLeftComboBox.getValue(), videoSelectionMenuRightComboBox.getValue());
                            isThreadForSynchronizeActive = false;
                        });
                        isThreadForSynchronizeActive = true;
                        threadForSynchronize.start();

                        threadForCreateVBoxBySubtopic = new Thread(() -> {
                            try {
                                threadForSynchronize.join();
                                threadForDownloadImage.join();
                                if (DBInteraction.isConn) {
                                    createVBoxBySubtopicInSelectionScene(videoSelectionMenuLeftComboBox.getValue(), videoSelectionMenuRightComboBox.getValue());
                                    setImages(videoSelectionMenuLeftComboBox.getValue(), videoSelectionMenuRightComboBox.getValue());
                                    Platform.runLater(() -> {
                                        videoSelectionMenuUpdate.setDisable(false);
                                        videoSelectionMenuLeftComboBox.setDisable(false);
                                        videoSelectionMenuRightComboBox.setDisable(false);
                                        videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                                        displayVBox(videoSelectionMenuFlowPane);
                                        updateSizes(videoSelectionMenuBorderPane.getWidth());
                                    });
                                } else {
                                    Platform.runLater(() -> {
                                        videoSelectionMenuOfflineModeButton.setDisable(false);
                                        videoSelectionMenuOfflineModeButton.setVisible(true);
                                        videoSelectionMenuUpdate.setDisable(false);
                                        videoSelectionMenuLeftComboBox.setDisable(false);
                                        videoSelectionMenuRightComboBox.setDisable(false);
                                        videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                                    });
                                }
                            } catch (InterruptedException e) {
                                System.out.println(e.getMessage());
                            }
                            isThreadForCreateVBoxBySubtopicActive = false;
                        });
                        isThreadForCreateVBoxBySubtopicActive = true;
                        threadForCreateVBoxBySubtopic.start();

                        threadForDownloadImage = new Thread(() -> {
                            try {
                                threadForSynchronize.join();
                                if (DBInteraction.isConn) {
                                    DBInteraction.downloadImages(videoSelectionMenuLeftComboBox.getValue(), videoSelectionMenuRightComboBox.getValue());
                                }
                                isThreadForDownloadImageActive = false;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                        });
                        isThreadForDownloadImageActive = true;
                        threadForDownloadImage.start();

                    });
                    pauseForDispose.play();
                } else {
                    pauseForDispose = new PauseTransition(Duration.millis(750));
                    videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/loading-circle.gif").toString()));
                    videoSelectionMenuUpdate.setDisable(true);
                    videoSelectionMenuLeftComboBox.setDisable(true);
                    videoSelectionMenuRightComboBox.setDisable(true);
                    videoSelectionMenuRightComboBoxPromptLabel.setVisible(false);
                    pauseForDispose.setOnFinished(_ -> {
                        videoSelectionMenuFlowPane.getChildren().clear();
                        subject = videoSelectionMenuLeftComboBox.getValue();
                        topic = videoSelectionMenuRightComboBox.getValue();
                        createVBoxInOfflineMode(videoSelectionMenuLeftComboBox.getValue(), videoSelectionMenuRightComboBox.getValue());
                        setImages(videoSelectionMenuLeftComboBox.getValue(), videoSelectionMenuRightComboBox.getValue());
                        displayVBox(videoSelectionMenuFlowPane);
                        updateSizes(videoSelectionMenuBorderPane.getWidth());
                        videoSelectionMenuLeftComboBox.setDisable(false);
                        videoSelectionMenuRightComboBox.setDisable(false);
                        videoSelectionMenuUpdate.setDisable(false);
                        videoSelectionMenuUpdate.setImage(new Image(getClass().getResource("/images/update.png").toString()));
                    });
                    pauseForDispose.play();

                }
            }
        });

        videoSelectionMenuBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 600) {
                videoSelectionMenuTopHBox.setPrefHeight(39);

                videoSelectionMenuBackButton.setPrefWidth(132);
                videoSelectionMenuBackButton.setPrefHeight(27);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 14));

                videoSelectionMenuOfflineModeButton.setPrefWidth(255);

                videoSelectionMenuInfoVBox.setPrefWidth(518);
                videoSelectionMenuInfoVBox.setPrefHeight(250);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoSelectionMenuCloseInfoButton.setPrefWidth(132);
                videoSelectionMenuCloseInfoButton.setPrefHeight(27);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 14));

                videoSelectionMenuDeleteVBox.setPrefWidth(518);
                videoSelectionMenuDeleteVBox.setPrefHeight(157);

                videoSelectionMenuDeleteTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                videoSelectionMenuConfirmLabel.setPrefWidth(132);
                videoSelectionMenuConfirmLabel.setPrefHeight(27);
                videoSelectionMenuConfirmLabel.setFont(Font.font("Arial Black", 14));

            } else if (newValue.intValue() < 800) {
                videoSelectionMenuTopHBox.setPrefHeight(40);

                videoSelectionMenuBackButton.setPrefWidth(171);
                videoSelectionMenuBackButton.setPrefHeight(28);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 15));

                videoSelectionMenuOfflineModeButton.setPrefWidth(275);

                videoSelectionMenuInfoVBox.setPrefWidth(572);
                videoSelectionMenuInfoVBox.setPrefHeight(276);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoSelectionMenuCloseInfoButton.setPrefWidth(146);
                videoSelectionMenuCloseInfoButton.setPrefHeight(28);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 15));

                videoSelectionMenuDeleteVBox.setPrefWidth(572);
                videoSelectionMenuDeleteVBox.setPrefHeight(173);

                videoSelectionMenuDeleteTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

                videoSelectionMenuConfirmLabel.setPrefWidth(146);
                videoSelectionMenuConfirmLabel.setPrefHeight(28);
                videoSelectionMenuConfirmLabel.setFont(Font.font("Arial Black", 15));
            } else if (newValue.intValue() < 950) {
                videoSelectionMenuTopHBox.setPrefHeight(41);

                videoSelectionMenuBackButton.setPrefWidth(210);
                videoSelectionMenuBackButton.setPrefHeight(29);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 16));

                videoSelectionMenuOfflineModeButton.setPrefWidth(295);

                videoSelectionMenuInfoVBox.setPrefWidth(626);
                videoSelectionMenuInfoVBox.setPrefHeight(302);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoSelectionMenuCloseInfoButton.setPrefWidth(160);
                videoSelectionMenuCloseInfoButton.setPrefHeight(29);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 16));

                videoSelectionMenuDeleteVBox.setPrefWidth(626);
                videoSelectionMenuDeleteVBox.setPrefHeight(189);

                videoSelectionMenuDeleteTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                videoSelectionMenuConfirmLabel.setPrefWidth(160);
                videoSelectionMenuConfirmLabel.setPrefHeight(29);
                videoSelectionMenuConfirmLabel.setFont(Font.font("Arial Black", 16));
            } else {
                videoSelectionMenuTopHBox.setPrefHeight(42);

                videoSelectionMenuBackButton.setPrefWidth(250);
                videoSelectionMenuBackButton.setPrefHeight(30);
                videoSelectionMenuBackButton.setFont(Font.font("Arial Black", 17));

                videoSelectionMenuOfflineModeButton.setPrefWidth(315);

                videoSelectionMenuInfoVBox.setPrefWidth(679);
                videoSelectionMenuInfoVBox.setPrefHeight(327);

                videoSelectionMenuInfoTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoSelectionMenuInfoTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoSelectionMenuCloseInfoButton.setPrefWidth(173);
                videoSelectionMenuCloseInfoButton.setPrefHeight(30);
                videoSelectionMenuCloseInfoButton.setFont(Font.font("Arial Black", 17));

                videoSelectionMenuDeleteVBox.setPrefWidth(679);
                videoSelectionMenuDeleteVBox.setPrefHeight(205);

                videoSelectionMenuDeleteTopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

                videoSelectionMenuConfirmLabel.setPrefWidth(173);
                videoSelectionMenuConfirmLabel.setPrefHeight(30);
                videoSelectionMenuConfirmLabel.setFont(Font.font("Arial Black", 17));
            }
        });

        videoSelectionMenuBorderPane.widthProperty().addListener(( _, _, newValue) -> updateSizes(newValue));
    }

}
