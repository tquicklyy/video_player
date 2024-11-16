import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.player.videoplayer.DBInteraction;

import static org.junit.jupiter.api.Assertions.*;

public class DBInteractionTest {

    @BeforeAll
    public static void setUpBeforeAll() {
        DBInteraction.connectToDB();

        Platform.startup(() -> {});
    }

    @Test
    void whenConnectToDBThenIsConnTrue() {
        assertTrue(DBInteraction.isConn);
    }

    @Test
    void whenConnectToDBThenConnToDBNotNull() {
        assertNotNull(DBInteraction.connToDB);
    }

    @Test
    void whenGetSubjectsThenIdOfSubjectsSizeNotZeroAndHashMapNotNull() {
        ComboBox<String> comboBox = new ComboBox<>();

        DBInteraction.getSubjects(comboBox);

        assertNotNull(DBInteraction.idOfSubjects);

        assertNotEquals(0,DBInteraction.idOfSubjects.size());
    }

    @Test
    void whenGetTopicsThenIdOfTopicsSizeNotZeroAndHashMapNotNull() {
        DBInteraction.getTopics(1, new ComboBox<>());

        assertNotNull(DBInteraction.idOfTopics);

        assertNotEquals(0,DBInteraction.idOfTopics.size());
    }

    @Test
    void whenGetSubtopicsThenAllListsAndMapsNotNullAndSizesNotZero() {
        DBInteraction.getSubtopics("Программирование", "Операторы языка C#");

        assertNotNull(DBInteraction.idOfTopics);

        assertNotEquals(0,DBInteraction.nameOfSubtopics.size());
    }

    @Test
    void whenDownloadImageThenIsConnTrue() {
        DBInteraction.getSubtopics("Программирование", "Операторы языка C#");

        DBInteraction.downloadImages("Программирование", "Операторы языка C#");

        assertTrue(DBInteraction.isConn);
    }

    @Test
    void whenDownloadVideoThenIsConnTrue() {
        DBInteraction.getSubtopics("Программирование", "Операторы языка C#");

        DBInteraction.downloadVideo("Программирование", "Операторы языка C#", "1 Операторы языка C#");

        assertTrue(DBInteraction.isConn);
    }
}
