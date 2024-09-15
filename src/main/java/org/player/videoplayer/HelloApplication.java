package org.player.videoplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage mainMenuStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        mainMenuStage.setTitle("Video player");
        mainMenuStage.getIcons().add(new Image("file:./src/main/resources/images/logo-video-player.png"));
        mainMenuStage.setScene(scene);
        mainMenuStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}