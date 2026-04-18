package shinydorky.mos.law_generator_frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class MainSceneController {
    private Stage mainStage;

    @FXML
    private TabPane tabPane;

    public void initialize(Stage mainStage){
        this.mainStage = mainStage;
        tabPane.setMinWidth(mainStage.getWidth()/5);
        tabPane.setMaxWidth(mainStage.getWidth()/5);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            tabPane.setMinWidth(mainStage.getWidth()/5);
            tabPane.setMaxWidth(mainStage.getWidth()/5);
//            System.out.println("Height: " + mainStage.getHeight() + " Width: " + mainStage.getWidth());
        };

        mainStage.widthProperty().addListener(stageSizeListener);

        AddTabPane("TEST");
        AddTabPane("TEST");
        AddTabPane("TEST");
        AddTabPane("TEST");
    }

    private void AddTabPane(String name){
        Tab newTab = new Tab(name);

        ScrollPane scrollPane = new ScrollPane();

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(0, 5, 0, 5));
        for (int i = 0; i < 20; i++){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/LawTypeTab.fxml"));
                Parent container = loader.load();
                vBox.getChildren().add(container);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        scrollPane.setContent(vBox);
        newTab.setContent(scrollPane);
        tabPane.getTabs().add(tabPane.getTabs().size(), newTab);
    }

}
