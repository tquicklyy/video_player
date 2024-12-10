package org.player.videoplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class InfoAboutPlayer {

    @FXML
    private Label infoAboutPLayerBackButton;

    @FXML
    private ImageView infoAboutPLayerImageView;

    @FXML
    private Label infoAboutPLayerSecondLabel;

    @FXML
    private HBox infoAboutPLayerTopHBox;

    @FXML
    private BorderPane infoAboutPLayerBorderPane;

    @FXML
    private VBox infoAboutPLayerVBox;

    @FXML
    private Label infoAboutPLayerFirstLabel;

    @FXML
    private ScrollPane infoAboutPLayerScrollPane;

    @FXML
    void switchingToTheMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("main-menu-scene.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), MainMenuController.currentStage.getScene().getWidth(), MainMenuController.currentStage.getScene().getHeight());
        MainMenuController.currentStage.setScene(newScene);
    }

    public void updateSizes(Number newValue) {
        if (newValue.intValue() < 978) {
            infoAboutPLayerImageView.setFitHeight(185);
            infoAboutPLayerImageView.setFitWidth(179);

            infoAboutPLayerFirstLabel.setFont(Font.font("Arial Black", 12));
        } else if (newValue.intValue() < 1251) {
            infoAboutPLayerImageView.setFitHeight(229);
            infoAboutPLayerImageView.setFitWidth(252);

            infoAboutPLayerFirstLabel.setFont(Font.font("Arial Black", 14));
        } else if (newValue.intValue() < 1624) {
            infoAboutPLayerImageView.setFitHeight(273);
            infoAboutPLayerImageView.setFitWidth(325);

            infoAboutPLayerFirstLabel.setFont(Font.font("Arial Black", 16));
        } else {
            infoAboutPLayerImageView.setFitHeight(319);
            infoAboutPLayerImageView.setFitWidth(398);

            infoAboutPLayerFirstLabel.setFont(Font.font("Arial Black", 18));
        }
    }


    @FXML
    void initialize() {
        infoAboutPLayerBorderPane.heightProperty().addListener(( _, _, newValue) -> {
            if (newValue.intValue() < 600) {
                infoAboutPLayerTopHBox.setPrefHeight(39);

                infoAboutPLayerBackButton.setPrefWidth(132);
                infoAboutPLayerBackButton.setPrefHeight(27);
                infoAboutPLayerBackButton.setFont(Font.font("Arial Black", 14));
            } else if (newValue.intValue() < 800) {
                infoAboutPLayerTopHBox.setPrefHeight(40);

                infoAboutPLayerBackButton.setPrefWidth(171);
                infoAboutPLayerBackButton.setPrefHeight(28);
                infoAboutPLayerBackButton.setFont(Font.font("Arial Black", 15));
            } else if (newValue.intValue() < 950) {
                infoAboutPLayerTopHBox.setPrefHeight(41);

                infoAboutPLayerBackButton.setPrefWidth(210);
                infoAboutPLayerBackButton.setPrefHeight(29);
                infoAboutPLayerBackButton.setFont(Font.font("Arial Black", 16));
            } else {
                infoAboutPLayerTopHBox.setPrefHeight(42);

                infoAboutPLayerBackButton.setPrefWidth(250);
                infoAboutPLayerBackButton.setPrefHeight(30);
                infoAboutPLayerBackButton.setFont(Font.font("Arial Black", 17));
            }
        });

        infoAboutPLayerBorderPane.widthProperty().addListener((_, _, newValue) -> updateSizes(newValue));

        infoAboutPLayerScrollPane.prefHeightProperty().bind(infoAboutPLayerBorderPane.heightProperty().subtract(infoAboutPLayerTopHBox.heightProperty()));
        infoAboutPLayerScrollPane.prefWidthProperty().bind(infoAboutPLayerBorderPane.widthProperty().subtract(infoAboutPLayerImageView.fitWidthProperty()));

        infoAboutPLayerVBox.prefWidthProperty().bind(infoAboutPLayerScrollPane.prefWidthProperty());

        infoAboutPLayerSecondLabel.fontProperty().bind(infoAboutPLayerFirstLabel.fontProperty());

        infoAboutPLayerSecondLabel.setText("""
                Версия программы: v1.6.

                Разработали: Конников Глеб Владимирович, Хагажеев Марат Муритович, Ковтун Владимир Александрович.

                Дата выпуска: 13 ноября 2024г.

                Программное средство 'Видеоплеер' разработано для просмотра пользователем\
                 своих видеороликов, а также обучающих видеуроков по программированию\
                 на объектно-ориентированном языке C#.

                В разработанном ПО доступны следующие функции:

                – просмотр собственных видео;
                – просмотр информации о ПО;
                – просмотр тем видео по программированию;
                – просмотр подтем видео по программированию;
                – просмотр списка видео по программированию;
                – скачивание видео по программированию;
                – удаление видео по программированию;
                – просмотр информации о видео;
                – просмотр видео по программироавнию онлайн;
                – просмотр видео по программированию локально.""");

    }

}
