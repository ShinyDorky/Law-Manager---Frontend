package shinydorky.mos.law_generator_frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import shinydorky.mos.law_generator_frontend.model.BasicLawFile;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawType;

import java.awt.*;
import java.io.IOException;

@Component
public class MainSceneController {
    private Stage mainStage;

    @FXML
    private TabPane tabPane;

    @FXML
    private TreeView treeView;

    public void initialize(Stage mainStage){
        this.mainStage = mainStage;
        tabPane.setMinWidth(mainStage.getWidth()/5);
        tabPane.setMaxWidth(mainStage.getWidth()/5);
        treeView.setMinWidth(mainStage.getWidth()/5);
        treeView.setMaxWidth(mainStage.getWidth()/5);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            tabPane.setMinWidth(mainStage.getWidth()/5);
            tabPane.setMaxWidth(mainStage.getWidth()/5);
            treeView.setMinWidth(mainStage.getWidth()/5);
            treeView.setMaxWidth(mainStage.getWidth()/5);
//            System.out.println("Height: " + mainStage.getHeight() + " Width: " + mainStage.getWidth());
        };

        mainStage.widthProperty().addListener(stageSizeListener);

        AddTabPane("TEST");
        AddTabPane("TEST");
        AddTabPane("TEST");
        AddTabPane("TEST");

        SetupTreeView();

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

    private void SetupTreeView(){
        LawType lawType = LawType.builder()
                .id(1l)
                .signature("military")
                .name("Military").build();
        TreeItem<BasicLawFile> root = new TreeItem<>(lawType);
//        TreeItem<String> root = new TreeItem<>("ROOT");
        treeView.setShowRoot(true);
        treeView.setRoot(root);

        LawGroup lawGroup1 = LawGroup.builder()
                .id(1l)
                .name("Recruitment Laws")
                .desc("These laws govern the recruitment methods employed by our state.")
                .signature("recruitment")
                .build();
        LawGroup lawGroup2 = LawGroup.builder()
                .id(1l)
                .name("Army Equipment Laws")
                .desc("These laws govern how our state manages the equipment of our armies.")
                .signature("army_equipment")
                .build();
        TreeItem<BasicLawFile> b1 = new TreeItem<>(lawGroup1);
        TreeItem<BasicLawFile> b2 = new TreeItem<>(lawGroup2);

        LawOption lawOption1 = LawOption.builder()
                .id(1l)
                .name("Recruitment Bonus")
                .signature("bonus")
                .desc("Paying people to join our armies is a decent incentive.")
                .canKeep("")
                .canPass("")
                .placeInOrder(0)
                .build();
        LawOption lawOption2 = LawOption.builder()
                .id(2l)
                .name("Share of Plunder")
                .signature("bonus")
                .desc("Paying with our enemies' gold is a much better alternative.")
                .canKeep("")
                .canPass("")
                .placeInOrder(0)
                .build();

        LawOption lawOption3 = LawOption.builder()
                .id(3l)
                .name("Personal Expense")
                .signature("personal")
                .desc("How those who fight for us arm themselves is their own personal matter.")
                .canKeep("")
                .canPass("")
                .placeInOrder(0)
                .build();
        LawOption lawOption4 = LawOption.builder()
                .id(4l)
                .name("State-funded arms")
                .signature("state")
                .effects("maa_upkeep = 0.1")
                .desc("Enables army equipment policies.\nProviding basic armaments for our fighters will make them more effective.")
                .canKeep("")
                .canPass("")
                .placeInOrder(1)
                .build();
        TreeItem<BasicLawFile> l1 = new TreeItem<>(lawOption1);
        TreeItem<BasicLawFile> l2 = new TreeItem<>(lawOption2);
        TreeItem<BasicLawFile> l3 = new TreeItem<>(lawOption3);
        TreeItem<BasicLawFile> l4 = new TreeItem<>(lawOption4);

        b1.getChildren().addAll(l1, l2);
        b2.getChildren().addAll(l3, l4);
        root.getChildren().addAll(b1, b2);
    }
}
