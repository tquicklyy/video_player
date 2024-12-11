package org.player.videoplayer;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

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
import java.util.Objects;

public class DBInteraction {
    public static Connection connToDB;
    private static String queryForDB;
    private static PreparedStatement preparedStatementForDB;
    private static ResultSet resultSetForDB;

    private static Alert alertWhenErrorSynchronized;

    public static HashMap<String, Integer> idOfSubjects;
    public static HashMap<String, Integer> idOfTopics;
    public static ArrayList<String> nameOfSubtopics;
    public static HashMap<String, String> telegramMessageUrl;
    public static HashMap<String, String> videoUrl;
    public static HashMap<String, String> downloadUrl;
    public static HashMap<String, String> imageUrl;

    public static HashMap<String, Thread> threadsForDownload = new HashMap<>();
    public static HashMap<String, Boolean> isVideoDownloading = new HashMap<>();

    public static boolean isOfflineMode;
    public static boolean isConn;

    public static void alertOn(String info) {
        Platform.runLater(() -> {
            alertWhenErrorSynchronized = new Alert(Alert.AlertType.ERROR);
            alertWhenErrorSynchronized.initOwner(MainMenuController.currentStage);
            alertWhenErrorSynchronized.setTitle("Ошибка синхронизации");
            alertWhenErrorSynchronized.setHeaderText(info);
            alertWhenErrorSynchronized.setContentText("Проверьте подключение к интернету и повторите попытку.");
            alertWhenErrorSynchronized.showAndWait();
        });
    }

    public static void connectToDB() {
        try{
            connToDB = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kubstu-education", "read_only", "read_only");
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
            System.out.println("Ошибка получения тем видео: " + e);
            alertOn("Не удалось получить темы видео.");
            isConn = false;
            if(!videoSelectionMenuLeftComboBox.getItems().isEmpty()) {
                videoSelectionMenuLeftComboBox.getItems().clear();
            }
        }
    }

    public static void getTopics(Integer id, ComboBox<String> videoSelectionMenuRightComboBox) {
        queryForDB = "SELECT \"Topic\".id, \"Topic\".topic FROM \"Subject\" INNER JOIN \"Topic\" ON \"Subject\".id = \"Topic\".subject_id WHERE \"Subject\".id = ? ORDER BY \"Topic\".topic";
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
            System.out.println("Ошибка получения подтем видео: " + e);
            alertOn("Не удалось получить подтемы видео.");
            isConn = false;
            if(!videoSelectionMenuRightComboBox.getItems().isEmpty()) {
                videoSelectionMenuRightComboBox.getItems().clear();
            }
        }
    }

    public static void getSubtopics(String subject, String topic) {
        queryForDB = "SELECT \"Subtopic\".id, \"Subtopic\".subtopic, \"Subtopic\".telegram_message_url, \"Subtopic\".video_url, \"Subtopic\".download_url, \"Subtopic\".image_url FROM \"Topic\" INNER JOIN \"Subtopic\" ON \"Topic\".id = \"Subtopic\".topic_id WHERE \"Topic\".id = ? ORDER BY \"Subtopic\".id";
        try {
            preparedStatementForDB = connToDB.prepareStatement(queryForDB);
            preparedStatementForDB.setInt(1, DBInteraction.idOfTopics.get(topic));
            resultSetForDB = preparedStatementForDB.executeQuery();
            
            nameOfSubtopics = new ArrayList<>();
            imageUrl = new HashMap<>();
            telegramMessageUrl = new HashMap<>();
            videoUrl = new HashMap<>();
            downloadUrl = new HashMap<>();

            String currentSubtopicWithNumber;

            int numberOfVideo = 0;
            while (resultSetForDB.next()) {
                numberOfVideo++;
                currentSubtopicWithNumber = numberOfVideo + " " + resultSetForDB.getString("subtopic");

                nameOfSubtopics.add(currentSubtopicWithNumber);
                telegramMessageUrl.put(currentSubtopicWithNumber, resultSetForDB.getString("telegram_message_url"));
                videoUrl.put(currentSubtopicWithNumber, resultSetForDB.getString("video_url"));
                downloadUrl.put(currentSubtopicWithNumber, resultSetForDB.getString("download_url"));
                imageUrl.put(currentSubtopicWithNumber,resultSetForDB.getString("image_url"));

                File fileToSave = new File(String.format("../Materials/%s/%s/%s/%s.json", subject, topic, numberOfVideo + " " + resultSetForDB.getString("subtopic"), numberOfVideo + " " + resultSetForDB.getString("subtopic")));
                if (!fileToSave.getParentFile().exists()) {
                    if(fileToSave.getParentFile().mkdirs()) {
                        System.out.printf("Успешное создание папки для подтемы %s%n", topic);
                    }
                }

                try(FileWriter writer = new FileWriter(fileToSave)) {
                    String info = String.format(
                            """
                            Глава: %s
                            Тема: %s
                            Номер видео: %s
                            Название видео: %s
                            Ссылка на видео в telegram: %s
                            Ссылка на видео в ВК: %s
                            Юзернейм бота в telegram: @kubstu_education_bot
                            Ссылка на группу ВК: https://vk.com/club227646062""",
                            subject,
                            topic,
                            numberOfVideo,
                            resultSetForDB.getString("subtopic"),
                            DBInteraction.telegramMessageUrl.get(currentSubtopicWithNumber),
                            DBInteraction.videoUrl.get(currentSubtopicWithNumber));

                    writer.write(info);
                } catch (Exception e) {
                    System.out.println("Ошибка записи информации в файл типа .json: " + e);
                }
            }
            isConn = true;
        } catch (Exception e) {
            System.out.println("Ошибка получение списка видео: " + e);
            alertOn("Не удалось получить список видео.");
            isConn = false;
        }
    }

    public static void downloadVideo(String subject, String topic, String subtopic) {
        URL urlOfVideo;
        File directoryToSave;
        File fileToSave;
            directoryToSave = new File(String.format("../Materials/%s/%s/%s", subject, topic, subtopic));
            try {
                urlOfVideo = new URI(downloadUrl.get(subtopic)).toURL();
                fileToSave = new File(String.format("%s/%s.mp4", directoryToSave, subtopic));

                download(urlOfVideo, fileToSave);

                if(!Thread.currentThread().isInterrupted() && isConn) {
                    VideoSelectionMenuController.deleteImageViewHashMap.get(subtopic).setImage(new Image(Objects.requireNonNull(DBInteraction.class.getResource("/images/delete-red.png")).toString()));
                    VideoSelectionMenuController.deleteImageViewHashMap.get(subtopic).setCursor(Cursor.HAND);
                }
            } catch (Exception e) {
                System.out.println("Ошибка скачивания видео: " + e);
            }

    }

    public static void downloadImages(String subject, String topic) {
        URL urlOfImage;
        File directoryToSave;
        File fileToSave;
        File oldFile;

        for(String subtopic: nameOfSubtopics) {
            directoryToSave = new File(String.format("../Materials/%s/%s/%s", subject, topic, subtopic));

            if (Thread.currentThread().isInterrupted()) return; // To avoid downloading the rest

            try {
                urlOfImage = new URI(imageUrl.get(subtopic)).toURL();
                fileToSave = new File(String.format("%s/%snew.png", directoryToSave, subtopic));
                if(isConn) {
                    download(urlOfImage, fileToSave);
                    if (!Thread.currentThread().isInterrupted() && isConn) {
                        oldFile = new File(String.format("%s/%s.png", directoryToSave, subtopic));
                        if(oldFile.delete()) System.out.println("Старая фотография была удалена.");
                        if(fileToSave.renameTo(oldFile)) System.out.println("Новая фотография была добавлена.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка загрузки фотографий: " + e);
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
            if(!Thread.currentThread().isInterrupted()) {
                alertOn("Не удалось скачать необходимые файлы.");
                isConn = false;
            } else {
                isConn = true;
            }
        }
        if (Thread.currentThread().isInterrupted() || !isConn) {
            if(fileToSave.delete()) {
                System.out.println("Загружаемый файл был удалён");
            }
        }
    }
}
