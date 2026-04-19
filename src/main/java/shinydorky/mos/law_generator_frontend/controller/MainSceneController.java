package shinydorky.mos.law_generator_frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shinydorky.mos.law_generator_frontend.model.BasicLawFile;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawType;
import shinydorky.mos.law_generator_frontend.rest.RESTConnector;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class MainSceneController {
    private Stage mainStage;

    @FXML
    private TreeView<BasicLawFile> treeView;

    @FXML
    private AnchorPane displayPane1;

    @FXML
    private TextField typeNameText;
    @FXML
    private TextField typeSignatureText;

    public void initialize(Stage mainStage){
        this.mainStage = mainStage;
        resize();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            resize();
        };

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            typeNameText.setText(newValue.getValue().getName());
            typeSignatureText.setText(newValue.getValue().getSignature());
        });

        mainStage.widthProperty().addListener(stageSizeListener);
        SetupTreeView();

    }

    private void resize(){
        treeView.setMinWidth(mainStage.getWidth()/5);
        treeView.setMaxWidth(mainStage.getWidth()/5);
        displayPane1.setMinWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
        displayPane1.setMaxWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
//        System.out.println("Height: " + mainStage.getHeight() + " Width: " + mainStage.getWidth());
    }

    private void SetupTreeView(){
        TreeItem<BasicLawFile> root = new TreeItem<>();
        LawType lawType = LawType.builder()
                .id(1l)
                .signature("military")
                .name("Military").build();
        TreeItem<BasicLawFile> lawType1 = new TreeItem<>(lawType);
//        TreeItem<String> root = new TreeItem<>("ROOT");
        treeView.setShowRoot(false);
        treeView.getSelectionModel().clearSelection();
        treeView.setRoot(root);

        ArrayList<LawType> types = RESTConnector.getAllTypes();
        for(LawType type: types){
            TreeItem<BasicLawFile> lawTypeItem = new TreeItem<>(type);
            root.getChildren().add(lawTypeItem);
        }
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
        lawType1.getChildren().addAll(b1, b2);
        root.getChildren().addAll(lawType1);
    }
}
