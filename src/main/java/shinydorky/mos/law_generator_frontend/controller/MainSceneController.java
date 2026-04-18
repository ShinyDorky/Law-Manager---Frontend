package shinydorky.mos.law_generator_frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class MainSceneController {
    private Stage mainStage;

    @FXML
    private ScrollPane scrollPane;

    public void initialize(Stage mainStage){
        this.mainStage = mainStage;
        scrollPane.setMinWidth(mainStage.getWidth()/5);
        scrollPane.setMaxWidth(mainStage.getWidth()/5);
        
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            scrollPane.setMinWidth(mainStage.getWidth()/5);
            scrollPane.setMaxWidth(mainStage.getWidth()/5);
//            System.out.println("Height: " + mainStage.getHeight() + " Width: " + mainStage.getWidth());
        };

        mainStage.widthProperty().addListener(stageSizeListener);
    }

}
