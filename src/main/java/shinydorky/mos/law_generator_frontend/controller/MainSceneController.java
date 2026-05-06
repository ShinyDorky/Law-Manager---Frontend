package shinydorky.mos.law_generator_frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawType;
import shinydorky.mos.law_generator_frontend.rest.RESTConnector;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class MainSceneController {
    private Stage mainStage;

    @FXML
    private TreeView<LawType> treeView;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane scrollPaneContent;
    @FXML
    private VBox itemDisplayBox;
    @FXML
    private HBox itemDisplayContent;

    @FXML
    private TextField nameText;
    @FXML
    private TextField signatureText;
    @FXML
    private TextArea descriptionText;
    @FXML
    private VBox descriptionArea;

    @FXML
    private Button addLawTypeButton;

    @FXML
    private VBox optionAttributes1;
    @FXML
    private VBox optionAttributes2;
    @FXML
    private TextField statePowerOpinion;
    @FXML
    private TextField militaryOpinion;
    @FXML
    private TextField religiousUnityOpinion;
    @FXML
    private TextField culturalToleranceOpinion;
    @FXML
    private TextField populismOpinion;

    @FXML
    private TextArea effectsText;


    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button addButton;

    public void initialize(Stage mainStage){
        this.mainStage = mainStage;
        resize();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            resize();
        };
        ChangeListener<Boolean> stageFullscreenListener = (observable, oldValue, newValue) -> {
            if (mainStage.maximizedProperty().get()){
                System.out.println("AAAAA");
            }
            resize();
        };

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){

                descriptionArea.setVisible(false);
                optionAttributes1.setVisible(false);
                optionAttributes2.setVisible(false);
                ClearFields();
            } else {
                DisplayChosenItem(newValue.getValue());
                deleteButton.setVisible(true);
                saveButton.setVisible(true);
                addButton.setVisible(false);
            }
        });

        scrollPane.widthProperty().addListener(stageSizeListener);
        scrollPane.heightProperty().addListener(stageSizeListener);
//        mainStage.maximizedProperty().addListener(stageFullscreenListener);
//        mainStage.fullScreenProperty().addListener(stageFullscreenListener);
        mainStage.widthProperty().addListener(stageSizeListener);
        mainStage.heightProperty().addListener(stageSizeListener);
        SetupTreeView();

    }

    private void resize(){
        itemDisplayBox.setMinWidth(scrollPane.getWidth() - 5);
        itemDisplayBox.setMaxWidth(scrollPane.getWidth() - 5);
        itemDisplayBox.setMinHeight(scrollPane.getHeight() - 10);
        itemDisplayBox.setMaxHeight(scrollPane.getHeight() - 10);
        itemDisplayContent.setMinHeight(scrollPane.getHeight() - 10);
        itemDisplayContent.setMaxHeight(scrollPane.getHeight() - 10);

        if (scrollPane.getHeight() >= 620){
        scrollPaneContent.setMinHeight(scrollPane.getHeight() - 5);
        scrollPaneContent.setMaxHeight(scrollPane.getHeight() - 5);
        }
//        scrollPaneContent.setMinWidth(scrollPane.getWidth() - 5);
//        scrollPaneContent.setMaxWidth(scrollPane.getWidth() - 5);
//        scrollPaneContent.setMinHeight(scrollPane.getHeight() - 5);
//        scrollPaneContent.setMaxHeight(scrollPane.getHeight() - 5);
//        treeView.setMinWidth(mainStage.getWidth()/5);
//        treeView.setMaxWidth(mainStage.getWidth()/5);
//        addLawTypeButton.setMinWidth(mainStage.getWidth()/5);
//        addLawTypeButton.setMaxWidth(mainStage.getWidth()/5);
//        displayPaneType.setMinWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
//        displayPaneType.setMaxWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
//        displayPaneGroup.setMinWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
//        displayPaneGroup.setMaxWidth(mainStage.getWidth() - mainStage.getWidth()/5 - 40);
    }

    private void SetupTreeView(){
        TreeItem<LawType> root = new TreeItem<>();
        treeView.setShowRoot(false);
        treeView.getSelectionModel().clearSelection();
        treeView.setRoot(root);
        try {

            ArrayList<LawType> types = RESTConnector.GetAllTypes();
            for(LawType type: types){
                TreeItem<LawType> lawTypeItem = new TreeItem<>(type);
                root.getChildren().add(lawTypeItem);

                ArrayList<LawGroup> groups = RESTConnector.GetGroupsInType(type.getId());
                for(LawGroup group: groups){
                    TreeItem<LawType> lawGroupItem = new TreeItem<>(group);
                    lawTypeItem.getChildren().add(lawGroupItem);

                    ArrayList<LawOption> options = RESTConnector.GetOptionsInGroup(group.getId());
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
        nameText.setText(selectedItem.getName());
        signatureText.setText(selectedItem.getSignature());
        if (selectedItem.getClass() == LawType.class){
            descriptionArea.setVisible(false);
            optionAttributes1.setVisible(false);
            optionAttributes2.setVisible(false);

        } else if (selectedItem.getClass() == LawGroup.class){
            descriptionArea.setVisible(true);
            optionAttributes1.setVisible(false);
            optionAttributes2.setVisible(false);

            descriptionText.setText(((LawGroup)selectedItem).getDesc());

        } else if (selectedItem.getClass() == LawOption.class){
            descriptionArea.setVisible(true);
            optionAttributes1.setVisible(true);
            optionAttributes2.setVisible(true);

            descriptionText.setText(((LawOption)selectedItem).getDesc());

            statePowerOpinion.setText(((LawOption)selectedItem).getStatePowerOpinion().toString());
            militaryOpinion.setText(((LawOption)selectedItem).getMilitaryOpinion().toString());
            religiousUnityOpinion.setText(((LawOption)selectedItem).getReligiousUnityOpinion().toString());
            culturalToleranceOpinion.setText(((LawOption)selectedItem).getCulturalToleranceOpinion().toString());
            populismOpinion.setText(((LawOption)selectedItem).getPopulismOpinion().toString());

            effectsText.setText(((LawOption)selectedItem).getEffects());
        }

    }

    @FXML
    public void DisplayLawTypeCreation(){
        Deselect();
        SetupCreation();
    }
    @FXML
    public void Deselect(){
        treeView.getSelectionModel().clearSelection();
    }

    public void SetupCreation(){
        deleteButton.setVisible(false);
        saveButton.setVisible(false);
        addButton.setVisible(true);
        if (treeView.getSelectionModel().getSelectedIndex() == -1){
            descriptionArea.setVisible(false);
            optionAttributes1.setVisible(false);
            optionAttributes2.setVisible(false);
            ClearFields();
        }
    }

    @FXML
    public void SaveItem(){
        Long id = treeView.getSelectionModel().getSelectedItem().getValue().getId();
        RESTConnector.updateLawType(LawType.builder()
                .id(id).name(nameText.getText())
                .signature(signatureText.getText())
                .build()
        );
        SetupTreeView();
        treeView.getSelectionModel().clearSelection();
        ClearFields();
    }
    @FXML
    public void DeleteItem(){
        RESTConnector.DeleteLawType(treeView.getSelectionModel().getSelectedItem().getValue().getId());
        SetupTreeView();
        treeView.getSelectionModel().clearSelection();
        ClearFields();
    }

    @FXML
    public void AddNewItem(){
        RESTConnector.createNewLawType(LawType.builder().name(nameText.getText()).signature(signatureText.getText()).build());
        SetupTreeView();
        treeView.getSelectionModel().clearSelection();
        ClearFields();
    }

    private void ClearFields(){
        nameText.setText("");
        signatureText.setText("");
        descriptionText.setText("");
        statePowerOpinion.setText("");
        militaryOpinion.setText("");
        religiousUnityOpinion.setText("");
        culturalToleranceOpinion.setText("");
        populismOpinion.setText("");
        effectsText.setText("");
    }
}
