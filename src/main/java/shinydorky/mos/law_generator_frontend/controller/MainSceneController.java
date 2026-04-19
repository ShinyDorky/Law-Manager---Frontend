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
import org.springframework.web.client.ResourceAccessException;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawType;
import shinydorky.mos.law_generator_frontend.rest.RESTConnector;

import java.io.IOError;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class MainSceneController {
    private Stage mainStage;

    @FXML
    private TreeView<LawType> treeView;

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
            if (newValue.getValue().getClass() == LawGroup.class){
                System.out.println("KLASA");
            }
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
        TreeItem<LawType> root = new TreeItem<>();
        LawType lawType = LawType.builder()
                .id(1l)
                .signature("military")
                .name("Military").build();
        TreeItem<LawType> lawType1 = new TreeItem<>(lawType);
//        TreeItem<String> root = new TreeItem<>("ROOT");
        treeView.setShowRoot(false);
        treeView.getSelectionModel().clearSelection();
        treeView.setRoot(root);
        try {

            ArrayList<LawType> types = RESTConnector.getAllTypes();
            for(LawType type: types){
                TreeItem<LawType> lawTypeItem = new TreeItem<>(type);
                root.getChildren().add(lawTypeItem);

                ArrayList<LawGroup> groups = RESTConnector.getGroupsInType(type.getId());
                for(LawGroup group: groups){
                    TreeItem<LawType> lawGroupItem = new TreeItem<>(group);
                    lawTypeItem.getChildren().add(lawGroupItem);

                    ArrayList<LawOption> options = RESTConnector.getOptionsInGroup(group.getId());
                    for(LawOption option: options){
                        TreeItem<LawType> lawOptionItem = new TreeItem<>(option);
                        lawGroupItem.getChildren().add(lawOptionItem);
                    }
                }
            }
        } catch (ResourceAccessException e){
            System.out.println("ERROR CONNECTING");
        }
    }

    private void DisplayChosenItem(LawType selectedItem){
        typeNameText.setText(selectedItem.getName());
        typeSignatureText.setText(selectedItem.getSignature());
        if (selectedItem.getClass() == LawGroup.class){
            System.out.println("KLASA");
        }

    }
}
