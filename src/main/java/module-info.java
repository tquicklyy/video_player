module org.player.videoplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires javafx.media;
    requires com.jfoenix;
    requires java.sql;


    opens org.player.videoplayer to javafx.fxml;
    exports org.player.videoplayer;
}