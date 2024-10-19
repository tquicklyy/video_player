package org.player.videoplayer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WatchVideoFromYouTubeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane WVFYBorderPane;

    @FXML
    private WebView WVFYWebView;

    private WebEngine engine;

    @FXML
    void initialize() {
        engine = WVFYWebView.getEngine();

        // HTML код для загрузки видео через iframe
        String videoUrl = "https://vk.com/video_ext.php?oid=-227646062&id=456239021&hash=6a41847ab8b1a986";
        String htmlContent = String.format(
                "<html>" +
                        "<head>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                        "<style>body{margin:0; padding:0; overflow:hidden;}</style>" +
                        "</head>" +
                        "<body>" +
                        "<iframe src=\"%s\" width=\"640\" height=\"360\" frameborder=\"0\" allowfullscreen allow=\"autoplay; encrypted-media; fullscreen; picture-in-picture\"></iframe>" +
                        "</body>" +
                        "</html>",
                videoUrl
        );

        // Загружаем HTML код
        engine.loadContent(htmlContent, "text/html");
    }
}
