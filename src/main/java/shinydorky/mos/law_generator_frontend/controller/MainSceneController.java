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
    private AnchorPane displayPaneType;
    @FXML
    private AnchorPane displayPaneGroup;

    @FXML
    private TextField typeNameText;
    @FXML
    private TextField typeSignatureText;
    @FXML
    private TextField groupNameText;
    @FXML
    private TextField groupSignatureText;
    @FXML
    private TextArea groupDescriptionText;

    public void initialize(Stage mainStage){
        this.mainStage = mainStage;
        resize();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            resize();
        };

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            DisplayChosenItem(newValue.getValue());
        });

        mainStage.widthProperty().addListener(stageSizeListener);
        SetupTreeView();

    }

    private void resize(){
        treeView.setMinWidth(mainStage.getWidth()/5);
        treeView.setMaxWidth(mainStage.getWidth()/5);
        displayPaneType.setMinWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
        displayPaneType.setMaxWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
        displayPaneGroup.setMinWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
        displayPaneGroup.setMaxWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
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
        if (selectedItem.getClass() == LawType.class){
            displayPaneType.setVisible(true);
            displayPaneGroup.setVisible(false);

            typeNameText.setText(selectedItem.getName());
            typeSignatureText.setText(selectedItem.getSignature());
        } else if (selectedItem.getClass() == LawGroup.class){
            displayPaneType.setVisible(false);
            displayPaneGroup.setVisible(true);

            groupNameText.setText(selectedItem.getName());
            groupSignatureText.setText(selectedItem.getSignature());
            groupDescriptionText.setText(((LawGroup)selectedItem).getDesc());
        }

    }
}
