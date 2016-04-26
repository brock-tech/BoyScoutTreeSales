//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
package userinterface;             

import impresario.IModel;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.TreeType;
import model.TreeTypeTableModel;
import static userinterface.BaseView.DEFAULT_WIDTH;

/**
 *
 * @author Andrew
 */
public class EditTreeTypeFormView extends BaseView {
    protected TextField barcodeSearch;    
    protected Button editButton;
    protected Button removeButton;
    protected Button cancelButton;
    protected TableView<TreeTypeTableModel> tableOfTreeTypes;
    
    public EditTreeTypeFormView(IModel model) {
        super(model, "EditTreeTypeTransactionView");
        
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TreeTypes", this);
        myModel.subscribe("ClearList", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> actionHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("table");
        
        Text welcomeText = new Text(myResources.getProperty("title"));
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.getStyleClass().add("information-text");
        content.getChildren().add(welcomeText);
        
        IFormItemStrategy formItemBuilder;
        Pane formItem;
        try {
            formItemBuilder = (IFormItemStrategy)Class.forName(
                    "userinterface.TopAlignFormItem").newInstance();
        }
        catch (Exception cnfe) {
            System.err.printf("Form Item Strategy error: " + cnfe.getMessage());
            return content;
        }
        
        HBox searchContainer = new HBox(20);
        searchContainer.setAlignment(Pos.CENTER);
        
        barcodeSearch = new TextField();
        barcodeSearch.setOnAction(actionHandler);
        barcodeSearch.setPromptText(myResources.getProperty("searchPrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("BarcodePrefix"), barcodeSearch);
        formItem.setPrefWidth(DEFAULT_WIDTH / 2);
        searchContainer.getChildren().add(formItem);
        
        content.getChildren().add(searchContainer);
        
        tableOfTreeTypes = new TableView<>();
        tableOfTreeTypes.getSelectionModel();
        tableOfTreeTypes.setEditable(false);
        
        
        TableColumn idColumn = new TableColumn(myResources.getProperty("ID"));
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(
                new PropertyValueFactory("ID"));
        idColumn.setVisible(false);
        
        TableColumn typeColumn = new TableColumn(myResources.getProperty("TypeDescription"));
        typeColumn.setMinWidth(275);
        typeColumn.setCellValueFactory(
                new PropertyValueFactory("TypeDescription"));
        
        TableColumn costColumn = new TableColumn(myResources.getProperty("Cost"));
        costColumn.setMinWidth(250);
        costColumn.setCellValueFactory(
                new PropertyValueFactory("Cost"));
        
        TableColumn barcodeColumn = new TableColumn(myResources.getProperty("BarcodePrefix"));
        barcodeColumn.setMinWidth(250);
        barcodeColumn.setCellValueFactory(
                new PropertyValueFactory("BarcodePrefix"));
        
        tableOfTreeTypes.getColumns().addAll(
                idColumn, 
                typeColumn, 
                costColumn,
                barcodeColumn
        );
        
        ScrollPane tableScrollPane = new ScrollPane();
        tableScrollPane.setPrefSize(500, 270);
        tableScrollPane.setContent(tableOfTreeTypes);
        content.getChildren().add(tableScrollPane);
        
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        
        editButton = new Button(myResources.getProperty("editButton"));
        editButton.setOnAction(actionHandler);
        editButton.setPrefWidth(100);
        buttonContainer.getChildren().add(editButton);
        
        removeButton = new Button(myResources.getProperty("removeButton"));
        removeButton.setOnAction(actionHandler);
        removeButton.setPrefWidth(100);
        buttonContainer.getChildren().add(removeButton);
        
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(actionHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);
        
        content.getChildren().add(buttonContainer);
        
        return content;
    }
    
    protected void getEntryTableModelValues() {
        
        
        ObservableList<TreeTypeTableModel> tableData = FXCollections.observableArrayList();
        try {
            Vector entryList = (Vector) myModel.getState("TreeTypes");
            Enumeration entries = entryList.elements();
            
            while (entries.hasMoreElements()) {
                TreeType nextTreeType = (TreeType)entries.nextElement();
                
                TreeTypeTableModel treeTypeEntry = new TreeTypeTableModel(nextTreeType.getTableListView());
                System.out.println("model data " + treeTypeEntry.getBarcodePrefix());
                tableData.add(treeTypeEntry);
            }
            tableOfTreeTypes.setItems(tableData);
            
        } catch (Exception e) {
            // TODO: Handle exceptions
        }
    }
    
    protected void processAction(Event event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            myModel.stateChangeRequest("Cancel", null);
        }
        else if (source == barcodeSearch) {
            Properties searchTerms = new Properties();
            searchTerms.setProperty("BarcodePrefix", barcodeSearch.getText());
            myModel.stateChangeRequest("SearchTreeTypes", searchTerms);
        }
        else if (source == removeButton) {
            TreeTypeTableModel itemSelected = tableOfTreeTypes.getSelectionModel().getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoSelection"));
            }
            else {
                myModel.stateChangeRequest("RemoveTreeType", itemSelected.getTreeTypeID());
                displayMessage(myResources.getProperty("deleteSuccess"));
            }
        }
        else if (source == editButton) {
            TreeTypeTableModel itemSelected = tableOfTreeTypes.getSelectionModel().getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoSelection"));
            }
            else {
                myModel.stateChangeRequest("EditTreeType", itemSelected.getTreeTypeID());
            }
        }
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
            
        } else if (key.equals("TreeTypes")) {
            getEntryTableModelValues();
        }
    }
    
}
