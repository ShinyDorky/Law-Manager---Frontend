package shinydorky.mos.law_generator_frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
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
import shinydorky.mos.law_generator_frontend.dto.LawGroupDto;
import shinydorky.mos.law_generator_frontend.dto.LawOptionDto;
import shinydorky.mos.law_generator_frontend.dto.LawTypeDto;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawType;
import shinydorky.mos.law_generator_frontend.replacer.LawGroupReplacer;
import shinydorky.mos.law_generator_frontend.replacer.LawOptionReplacer;
import shinydorky.mos.law_generator_frontend.rest.RESTConnector;

import java.util.*;

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
    private VBox nameArea;
    @FXML
    private TextField signatureText;
    @FXML
    private VBox signatureArea;
    @FXML
    private TextArea descriptionText;
    @FXML
    private VBox descriptionArea;
    @FXML
    private TextField placeInOrderText;
    @FXML
    private TextArea canKeepText;
    @FXML
    private TextArea canPassText;
    @FXML
    private TextArea onPassText;


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
    @FXML
    private Button addChildButton;
    @FXML
    private Button generateFilesButton;

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
                nameArea.setVisible(false);
                signatureArea.setVisible(false);
                descriptionArea.setVisible(false);
                optionAttributes1.setVisible(false);
                optionAttributes2.setVisible(false);
                deleteButton.setVisible(false);
                saveButton.setVisible(false);
                generateFilesButton.setVisible(false);
                addButton.setVisible(false);
                addChildButton.setVisible(false);
                ClearFields();
            } else {
                DisplayChosenItem(newValue.getValue());
                deleteButton.setVisible(true);
                saveButton.setVisible(true);
                addButton.setVisible(false);

                if (newValue.getValue().getItemDepth() == 0){
                    generateFilesButton.setVisible(false);
                } else {
                    generateFilesButton.setVisible(true);
                }

                if (newValue.getValue().getItemDepth() != 2){
                    addChildButton.setVisible(true);
                } else {
                    addChildButton.setVisible(false);
                }
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
    }

    /**
     * Make call to a locally running Backend instance and download all current data.
     */
    private void SetupTreeView(){
        TreeItem<LawType> root = new TreeItem<>();
        treeView.setShowRoot(false);
        treeView.getSelectionModel().clearSelection();
        treeView.setRoot(root);
        try {

            ArrayList<LawType> types = RESTConnector.GetAllTypes();
            for(LawType type: types){
                type.setItemDepth(0);
                TreeItem<LawType> lawTypeItem = new TreeItem<>(type);
                root.getChildren().add(lawTypeItem);

//                lawTypeItem.setExpanded(true);

                ArrayList<LawGroup> groups = RESTConnector.GetGroupsInType(type);
                for(LawGroup group: groups){
                    group.setItemDepth(1);
                    TreeItem<LawType> lawGroupItem = new TreeItem<>(group);
                    lawTypeItem.getChildren().add(lawGroupItem);

//                    lawGroupItem.setExpanded(true);

                    ArrayList<LawOption> options = RESTConnector.GetOptionsInGroup(group);
                    for(LawOption option: options){
                        option.setItemDepth(2);
                        TreeItem<LawType> lawOptionItem = new TreeItem<>(option);
                        lawGroupItem.getChildren().add(lawOptionItem);

//                        lawOptionItem.setExpanded(true);
                    }
                }
            }
        } catch (ResourceAccessException e){
            System.out.println("ERROR CONNECTING");
        }
    }

    private void DisplayChosenItem(LawType selectedItem){
        nameArea.setVisible(true);
        signatureArea.setVisible(true);
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
            if (Objects.equals(statePowerOpinion.getText(), "")) statePowerOpinion.setText("0");
            if (Objects.equals(militaryOpinion.getText(), "")) militaryOpinion.setText("0");
            if (Objects.equals(religiousUnityOpinion.getText(), "")) religiousUnityOpinion.setText("0");
            if (Objects.equals(culturalToleranceOpinion.getText(), "")) culturalToleranceOpinion.setText("0");
            if (Objects.equals(populismOpinion.getText(), "")) populismOpinion.setText("0");
            if (Objects.equals(placeInOrderText.getText(), "")) placeInOrderText.setText("0");
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
            placeInOrderText.setText(((LawOption)selectedItem).getPlaceInOrder().toString());

            canKeepText.setText(((LawOption)selectedItem).getCanKeep());
            canPassText.setText(((LawOption)selectedItem).getCanPass());
            onPassText.setText(((LawOption)selectedItem).getOnPass());

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

    /**
     * Display UI elements necessary for the creation of the desired Item
     */
    @FXML
    public void SetupCreation(){
        nameArea.setVisible(true);
        signatureArea.setVisible(true);
        deleteButton.setVisible(false);
        saveButton.setVisible(false);
        addChildButton.setVisible(false);
        generateFilesButton.setVisible(false);
        addButton.setVisible(true);
        if (treeView.getSelectionModel().getSelectedIndex() == -1){
            descriptionArea.setVisible(false);
            optionAttributes1.setVisible(false);
            optionAttributes2.setVisible(false);
            ClearFields();
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 0){
            descriptionArea.setVisible(true);
            optionAttributes1.setVisible(false);
            optionAttributes2.setVisible(false);
            ClearFields();
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 1){
            descriptionArea.setVisible(true);
            optionAttributes1.setVisible(true);
            optionAttributes2.setVisible(true);
            ClearFields();
        }
    }

    /**
     * Modify selected item by uploading a copy of it to the Backend with the new values.
     * Will reload UI and re-synchronize with the Backend database.
     */
    @FXML
    public void SaveItem(){
        if (treeView.getSelectionModel().isEmpty()){
            return;
        }
        if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 0){
            Long id = treeView.getSelectionModel().getSelectedItem().getValue().getId();
            RESTConnector.updateLawType(LawTypeDto.builder()
                    .id(id)
                    .name(nameText.getText())
                    .signature(signatureText.getText())
                    .build()
            );
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 1){
            Long id = treeView.getSelectionModel().getSelectedItem().getValue().getId();
            Long parentId = ((LawGroup)treeView.getSelectionModel().getSelectedItem().getValue()).getLawTypeId();
            RESTConnector.updateLawGroup(LawGroupDto.builder()
                    .id(id)
                    .name(nameText.getText())
                    .signature(signatureText.getText())
                    .desc(descriptionText.getText())
                    .lawTypeId(parentId)
                    .build()
            );
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 2){
            Long id = treeView.getSelectionModel().getSelectedItem().getValue().getId();
            Long parentId = ((LawOption)treeView.getSelectionModel().getSelectedItem().getValue()).getLawGroupId();
            if (Objects.equals(statePowerOpinion.getText(), "")) statePowerOpinion.setText("0");
            if (Objects.equals(militaryOpinion.getText(), "")) militaryOpinion.setText("0");
            if (Objects.equals(religiousUnityOpinion.getText(), "")) religiousUnityOpinion.setText("0");
            if (Objects.equals(culturalToleranceOpinion.getText(), "")) culturalToleranceOpinion.setText("0");
            if (Objects.equals(populismOpinion.getText(), "")) populismOpinion.setText("0");
            if (Objects.equals(placeInOrderText.getText(), "")) placeInOrderText.setText("0");
            RESTConnector.updateLawOption(LawOptionDto.builder()
                    .id(id)
                    .name(nameText.getText())
                    .signature(signatureText.getText())
                    .desc(descriptionText.getText())
                    .effects(effectsText.getText())
                    .statePowerOpinion(Integer.valueOf(statePowerOpinion.getText()))
                    .militaryOpinion(Integer.valueOf(militaryOpinion.getText()))
                    .religiousUnityOpinion(Integer.valueOf(religiousUnityOpinion.getText()))
                    .culturalToleranceOpinion(Integer.valueOf(culturalToleranceOpinion.getText()))
                    .populismOpinion(Integer.valueOf(populismOpinion.getText()))
                    .effects(effectsText.getText())
                    .placeInOrder(Integer.valueOf(placeInOrderText.getText()))
                    .canPass(canPassText.getText())
                    .canKeep(canKeepText.getText())
                    .onPass(onPassText.getText())
                    .passCost("")
                    .lawGroupId(parentId)
                    .build()
            );
        }
        RefreshView();
    }
    @FXML
    public void DeleteItem(){
        if (treeView.getSelectionModel().isEmpty()){
            return;
        }
        if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 0){
            RESTConnector.DeleteLawType(treeView.getSelectionModel().getSelectedItem().getValue().getId());
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 1){
            RESTConnector.DeleteLawGroup(treeView.getSelectionModel().getSelectedItem().getValue().getId());
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 2){
            RESTConnector.DeleteLawOption(treeView.getSelectionModel().getSelectedItem().getValue().getId());
        }
        RefreshView();
    }

    /**
     * Save newly created item by uploading it to the Backend. Will reload UI and re-synchronize with the Backend
     * database.
     */
    @FXML
    public void AddNewItem(){
        if (treeView.getSelectionModel().getSelectedIndex() == -1){
            RESTConnector.createNewLawType(LawTypeDto.builder()
                    .name(nameText.getText())
                    .signature(signatureText.getText())
                    .build()
            );
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 0){
            RESTConnector.createNewLawGroup(LawGroupDto.builder()
                    .name(nameText.getText())
                    .signature(signatureText.getText())
                    .desc(descriptionText.getText())
                            .lawTypeId(treeView.getSelectionModel().getSelectedItem().getValue().getId())
                    .build()
            );
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 1){
            if (Objects.equals(statePowerOpinion.getText(), "")) statePowerOpinion.setText("0");
            if (Objects.equals(militaryOpinion.getText(), "")) militaryOpinion.setText("0");
            if (Objects.equals(religiousUnityOpinion.getText(), "")) religiousUnityOpinion.setText("0");
            if (Objects.equals(culturalToleranceOpinion.getText(), "")) culturalToleranceOpinion.setText("0");
            if (Objects.equals(populismOpinion.getText(), "")) populismOpinion.setText("0");
            if (Objects.equals(placeInOrderText.getText(), "")) placeInOrderText.setText("0");
            RESTConnector.createNewLawOption(LawOptionDto.builder()
                    .name(nameText.getText())
                    .signature(signatureText.getText())
                    .desc(descriptionText.getText())
                    .effects(effectsText.getText())
                    .statePowerOpinion(Integer.valueOf(statePowerOpinion.getText()))
                    .militaryOpinion(Integer.valueOf(militaryOpinion.getText()))
                    .religiousUnityOpinion(Integer.valueOf(religiousUnityOpinion.getText()))
                    .culturalToleranceOpinion(Integer.valueOf(culturalToleranceOpinion.getText()))
                    .populismOpinion(Integer.valueOf(populismOpinion.getText()))
                    .effects(effectsText.getText())
                    .placeInOrder(Integer.valueOf(placeInOrderText.getText()))
                    .canPass(canPassText.getText())
                    .canKeep(canKeepText.getText())
                    .onPass(onPassText.getText())
                    .passCost("")
                            .lawGroupId(treeView.getSelectionModel().getSelectedItem().getValue().getId())
                    .build()
            );
        }
        RefreshView();
    }

    /**
     * Generate all the necessary files for the selected Item
     */
    @FXML
    public void GenerateFiles(){
        if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 1){
            LawGroupReplacer.WriteAllFiles((LawGroup) treeView.getSelectionModel().getSelectedItem().getValue());
        }
        else if (treeView.getSelectionModel().getSelectedItem().getValue().getItemDepth() == 2){
            LawOptionReplacer.WriteAllFiles((LawOption) treeView.getSelectionModel().getSelectedItem().getValue());
        }
    }

    @FXML
    public void RefreshView() {
        Stack<Long> itemsToExpand = new Stack<>();

        TreeItem<LawType> selectedItem = treeView.getSelectionModel().getSelectedItem();
        while (selectedItem != null &&selectedItem.getValue() != null){
            itemsToExpand.push(selectedItem.getValue().getId());
            selectedItem = selectedItem.getParent();
        }

        if (!itemsToExpand.isEmpty()){
            int selectionIndex = 0;
            SetupTreeView();
            treeView.getSelectionModel().clearSelection();
            ClearFields();

            ObservableList<TreeItem<LawType>> children = treeView.getRoot().getChildren();
            TreeItem<LawType> toExpand = null;
            Long seekingId = itemsToExpand.pop();
            for (TreeItem<LawType> typeTreeItem : children){
                if (Objects.equals(typeTreeItem.getValue().getId(), seekingId)){
                    toExpand = typeTreeItem;
                    selectionIndex = treeView.getRow(typeTreeItem);
                }
            }
            while (toExpand != null && !itemsToExpand.isEmpty()){
                toExpand.setExpanded(true);
                seekingId = itemsToExpand.pop();
                ObservableList<TreeItem<LawType>> toExpandCandidates = toExpand.getChildren();
                toExpand = null;
                for (TreeItem<LawType> candidate: toExpandCandidates){
                    if (Objects.equals(candidate.getValue().getId(), seekingId)){
                        toExpand = candidate;
                        selectionIndex = treeView.getRow(candidate);
                    }
                }
            }
            treeView.getSelectionModel().select(selectionIndex);
        }
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
        placeInOrderText.setText("");
        canKeepText.setText("");
        canPassText.setText("");
        onPassText.setText("");
    }
}
