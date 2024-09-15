module org.player.videoplayer {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.player.videoplayer to javafx.fxml;
    exports org.player.videoplayer;
}