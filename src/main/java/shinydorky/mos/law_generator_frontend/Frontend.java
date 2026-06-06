package shinydorky.mos.law_generator_frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import shinydorky.mos.law_generator_frontend.controller.MainSceneController;
import shinydorky.mos.law_generator_frontend.generator.FileGenerator;

public class Frontend extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        System.out.println("init: " + Thread.currentThread().getName());
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("start begin: " + Thread.currentThread().getName());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/MainScene.fxml"));
        Parent root = fxmlLoader.load();
        MainSceneController controller = fxmlLoader.getController();

        // scene, stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MOS Law Manager by ShinyDorky");
        primaryStage.show();
        primaryStage.setFullScreen(false);
//        primaryStage.setResizable(false);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(900);

        controller.initialize(primaryStage);
        FileGenerator.CreateTemplateDirIfMissing();

        System.out.println("start end: " + Thread.currentThread().getName());
    }

    @Override
    public void stop() {
        System.out.println("stop: " + Thread.currentThread().getName());
    }

}
