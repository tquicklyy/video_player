package org.player.videoplayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
    private VBox videoDownloadSceneGrayVBox;

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
    private BorderPane videoPlayerSceneBorderPane;

    private static Stage currentStage;
    private static Scene currentScene;
    private static Scene newScene;

    @FXML
    private void switchingToTheVideoSelectionMenu(MouseEvent event) throws IOException {
        currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("video-selection-menu.fxml"));
        newScene = new Scene(fxmlLoader.load(), currentStage.getScene().getWidth(), currentStage.getScene().getHeight());
        currentStage.setScene(newScene);
    }

    @FXML
    void initialize() {

    }

}
