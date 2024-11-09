package org.player.videoplayer;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class DBInteraction {
    private static Connection connToDB;
    private static String queryForDB;
    private static PreparedStatement preparedStatementForDB;
    private static ResultSet resultSetForDB;

    private static Alert alertWhenErrorSynch;

    public static HashMap<String, Integer> idOfSubjects;
    public static HashMap<String, Integer> idOfTopics;
    public static HashMap<String, Integer> idOfSubtopics;
    public static ArrayList<String> nameOfSubtopics;
    public static HashMap<String, String> telergramMessageUrl;
    public static HashMap<String, String> videoUrl;
    public static HashMap<String, String> downloadUrl;
    public static HashMap<String, String> imageUrl;

    public static HashMap<String, Thread> threadsForDownload = new HashMap<>();
    public static HashMap<String, Boolean> isVideoDownloading = new HashMap<>();

    public static int getIdByNameOfSubject(String subject) {
        return idOfSubjects.get(subject);
    }

    public static int getIdByNameOfTopic(String topic) {
        return idOfTopics.get(topic);
    }

    public static boolean isOfflineMode;
    public static boolean isConn;

    private static PauseTransition pauseForStopDownload;

    public static void alertOn(String info) {
        Platform.runLater(() -> {
            alertWhenErrorSynch = new Alert(Alert.AlertType.ERROR);
            alertWhenErrorSynch.initOwner(MainMenuController.currentStage);
            alertWhenErrorSynch.setTitle("Ошибка синхронизации");
            alertWhenErrorSynch.setHeaderText(info);
            alertWhenErrorSynch.setContentText("Проверьте подключение к интернету и повторите попытку.");
            alertWhenErrorSynch.showAndWait();
        });
    }

    public static void connectToDB(String info) {
        try{
            connToDB = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kubstu-education", info, "read_only");
            isConn = true;
        } catch (Exception e) {
            alertOn("Не удалось синхронизировать данные.");
            isConn = false;
        }
    }

    public static void getSubjects(ComboBox<String> videoSelectionMenuLeftComboBox) {
        queryForDB = "SELECT * FROM \"Subject\"";
        try {
            preparedStatementForDB = connToDB.prepareStatement(queryForDB);
            resultSetForDB = preparedStatementForDB.executeQuery();
            idOfSubjects = new HashMap<>();
            while (resultSetForDB.next()) {
                videoSelectionMenuLeftComboBox.getItems().add(resultSetForDB.getString("subject"));
                idOfSubjects.put(resultSetForDB.getString("subject"), resultSetForDB.getInt("id"));
            }
            isConn = true;
        }
        catch(Exception e) {
            System.out.println(3);
            System.out.println(e.getMessage());
            alertOn("Не удалось получить темы видео.");
            isConn = false;
            if(!videoSelectionMenuLeftComboBox.getItems().isEmpty()) {
                videoSelectionMenuLeftComboBox.getItems().clear();
            }
        }
    }

    public static void getTopics(Integer id, ComboBox<String> videoSelectionMenuRightComboBox) {
        queryForDB = "SELECT \"Topic\".id, \"Topic\".topic FROM \"Subject\" INNER JOIN \"Topic\" ON \"Subject\".id = \"Topic\".subject_id WHERE \"Subject\".id = ?";
        try {
            preparedStatementForDB = connToDB.prepareStatement(queryForDB);
            preparedStatementForDB.setInt(1, id);
            resultSetForDB = preparedStatementForDB.executeQuery();
            idOfTopics = new HashMap<>();
            while (resultSetForDB.next()) {
                idOfTopics.put(resultSetForDB.getString("topic"), resultSetForDB.getInt("id"));
                videoSelectionMenuRightComboBox.getItems().add(resultSetForDB.getString("topic"));
            }
            isConn = true;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            alertOn("Не удалось получить подтемы видео.");
            isConn = false;
            if(!videoSelectionMenuRightComboBox.getItems().isEmpty()) {
                videoSelectionMenuRightComboBox.getItems().clear();
            }
        }
    }

    public static void getSubtopics(String subject, String topic, Integer id) {
        queryForDB = "SELECT \"Subtopic\".id, \"Subtopic\".subtopic, \"Subtopic\".telegram_message_url, \"Subtopic\".video_url, \"Subtopic\".download_url, \"Subtopic\".image_url FROM \"Topic\" INNER JOIN \"Subtopic\" ON \"Topic\".id = \"Subtopic\".topic_id WHERE \"Topic\".id = ? ORDER BY \"Subtopic\".id";
        try {
            preparedStatementForDB = connToDB.prepareStatement(queryForDB);
            preparedStatementForDB.setInt(1, id);
            resultSetForDB = preparedStatementForDB.executeQuery();
            nameOfSubtopics = new ArrayList<>();
            imageUrl = new HashMap<>();
            telergramMessageUrl = new HashMap<>();
            videoUrl = new HashMap<>();
            downloadUrl = new HashMap<>();
            idOfSubtopics = new HashMap<>();

            int index = 0;
            while (resultSetForDB.next()) {
                index++;
                nameOfSubtopics.add(index + " " + resultSetForDB.getString("subtopic"));
                telergramMessageUrl.put(resultSetForDB.getString("subtopic"), resultSetForDB.getString("telegram_message_url"));
                videoUrl.put(index + " " + resultSetForDB.getString("subtopic"), resultSetForDB.getString("video_url"));
                downloadUrl.put(index + " " + resultSetForDB.getString("subtopic"), resultSetForDB.getString("download_url"));
                imageUrl.put(index + " " + resultSetForDB.getString("subtopic"),resultSetForDB.getString("image_url"));
                idOfSubtopics.put(index + " " + resultSetForDB.getString("subtopic"), resultSetForDB.getInt("id"));
                File fileToSave = new File(String.format("../Materials/%s/%s/%s/%s.json", subject, topic, index + " " + resultSetForDB.getString("subtopic"), index + " " + resultSetForDB.getString("subtopic")));
                if (!fileToSave.getParentFile().exists()) {
                    fileToSave.getParentFile().mkdirs();
                }
                try(FileWriter writer = new FileWriter(fileToSave)) {
                    String info = String.format("Глава: %s\nТема: %s\nНомер видео: %s\nНазвание видео: %s\nСсылка на видео в telegram: %s\nСсылка на видео в ВК: %s\nСсылка на бота в telegram: @kubstu_education_bot\nСсылка на группу ВК: https://vk.com/club227646062", subject, topic, index, resultSetForDB.getString("subtopic"), DBInteraction.telergramMessageUrl.get(resultSetForDB.getString("subtopic")), DBInteraction.videoUrl.get(resultSetForDB.getString("subtopic")));
                    writer.write(info);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            isConn = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            alertOn("Не удалось получить список видео.");
            isConn = false;
        }
    }

    public static void downloadVideo(String subject, String topic, String subtopic) {
        URL urlOfVideo;
        File directoryToSave;
            directoryToSave = new File(String.format("../Materials/%s/%s/%s", subject, topic, subtopic));
            if(!directoryToSave.exists()) {
                directoryToSave.mkdirs();
            }
            try {
                urlOfVideo = new URI(downloadUrl.get(subtopic)).toURL();
                File fileToSave = new File(String.format("%s/%s.mp4", directoryToSave, subtopic));
                if(!fileToSave.exists()) {
                    download(urlOfVideo, fileToSave);
                }

                if(!Thread.currentThread().isInterrupted()) {
                    VideoSelectionMenuController.deleteImageViewHashMap.get(subtopic).setImage(new Image(DBInteraction.class.getResource("/images/delete-red.png").toString()));
                    Platform.runLater(() -> {
                        pauseForStopDownload = new PauseTransition(Duration.millis(1000));
                        pauseForStopDownload.setOnFinished(_ -> {
                            System.out.println("я да");
                            VideoDownloadController.videoDownloadSceneCentralLabelHashMap.get(subtopic).setText("Видео не загружено");
                            VideoDownloadController.videoDownloadSceneGifImageViewHashMap.get(subtopic).setVisible(false);
                            VideoDownloadController.videoDownloadSceneDownloadLabelHashMap.get(subtopic).setText("Скачать видео");
                        });
                    });
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

    }

    public static void downloadImages(String subject, String topic) {
        URL urlOfImage;
        File directoryToSave;
        for(String subtopic: nameOfSubtopics) {
            directoryToSave = new File(String.format("../Materials/%s/%s/%s", subject, topic, subtopic));
            if(!directoryToSave.exists()) {
                directoryToSave.mkdirs();
            }
            if (Thread.currentThread().isInterrupted()) return;
            try {
                urlOfImage = new URI(imageUrl.get(subtopic)).toURL();
                File fileToSave = new File(String.format("%s/%s.png", directoryToSave, subtopic));
                if(!fileToSave.exists()) {
                    if(isConn) {
                        download(urlOfImage, fileToSave);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void download(URL urlForContent, File fileToSave ) throws IOException {
        HttpsURLConnection connectionForDownload = (HttpsURLConnection) urlForContent.openConnection();
        connectionForDownload.setRequestMethod("GET");
        connectionForDownload.setRequestProperty("User-Agent","Mozilla/5.0");
        connectionForDownload.setConnectTimeout(7000);
        connectionForDownload.setReadTimeout(7000);
        try(BufferedInputStream inRead = new BufferedInputStream(connectionForDownload.getInputStream());
            FileOutputStream fileToWrite = new FileOutputStream(fileToSave)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while((bytesRead = inRead.read(buffer, 0, 8192)) != -1 && !Thread.currentThread().isInterrupted()) {
                fileToWrite.write(buffer, 0, bytesRead);
            }
            isConn = true;
        } catch (Exception e) {
            if(fileToSave.exists()) {
                fileToSave.delete();
            }
            if(Thread.currentThread().isInterrupted()) return;
            alertOn("Не удалось скачать необходимые файлы.");
            Thread.currentThread().interrupt();
            isConn = false;
            return;
        }
        if (Thread.currentThread().isInterrupted()) {
            fileToSave.delete();
        }

    }
}
