package org.player.videoplayer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class VideoSelectionMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane videoSelectionMenuBorderPane;

    @FXML
    private FlowPane videoSelectionMenuFlowPane;

    @FXML
    private AnchorPane videoSelectionMenuLeftAnchorPane;

    @FXML
    private ComboBox<?> videoSelectionMenuLeftComboBox;

    @FXML
    private Label videoSelectionMenuLeftLabelForComboBox;

    @FXML
    private AnchorPane videoSelectionMenuRightAnchorPane;

    @FXML
    private ComboBox<?> videoSelectionMenuRightComboBox;

    @FXML
    private Label videoSelectionMenuRightLabelForComboBox;

    @FXML
    private ScrollPane videoSelectionMenuScrollPane;

    @FXML
    void initialize() {

    }

}
