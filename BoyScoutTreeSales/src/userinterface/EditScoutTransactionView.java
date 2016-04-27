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
import model.Scout;

/**
 *
 * @author mike
 */
public class EditScoutTransactionView extends BaseView {
    protected TextField firstNameSearchField;
    protected TextField lastNameSearchField;    
    protected Button editButton;
    protected Button removeButton;
    protected Button cancelButton;
    protected TableView<ScoutTableModel> tableOfScouts;
    
    public EditScoutTransactionView(IModel model) {
        super(model, "EditScoutTransactionView");
        
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("Scouts", this);
        myModel.subscribe("ClearList", this);
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
        
        Text title = new Text(myResources.getProperty("title"));
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("information-text");
        content.getChildren().add(title);
        
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
        
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER);
        
        firstNameSearchField = new TextField();
        firstNameSearchField.setOnAction(actionHandler);
        firstNameSearchField.setPromptText(myResources.getProperty("searchPrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("firstName"), firstNameSearchField);
        formItem.setPrefWidth(DEFAULT_WIDTH / 2);
        searchContainer.getChildren().add(formItem);
        
        lastNameSearchField = new TextField();
        lastNameSearchField.setOnAction(actionHandler);
        lastNameSearchField.setPromptText(myResources.getProperty("searchPrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("lastName"), lastNameSearchField);
        formItem.setPrefWidth(DEFAULT_WIDTH / 2);
        searchContainer.getChildren().add(formItem);
        
        content.getChildren().add(searchContainer);
        
        tableOfScouts = new TableView<>();
        tableOfScouts.getSelectionModel();
        tableOfScouts.setEditable(false);
        
        TableColumn idColumn = new TableColumn(myResources.getProperty("id"));
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(
                new PropertyValueFactory("id"));
        idColumn.setVisible(false);
        
        TableColumn firstNameColumn = new TableColumn(myResources.getProperty("firstName"));
        firstNameColumn.setMinWidth(125);
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory("firstName"));
        
        TableColumn middleNameColumn = new TableColumn(myResources.getProperty("middleName"));
        middleNameColumn.setMinWidth(125);
        middleNameColumn.setCellValueFactory(
                new PropertyValueFactory("middleName"));
        
        TableColumn lastNameColumn = new TableColumn(myResources.getProperty("lastName"));
        lastNameColumn.setMinWidth(125);
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory("lastName"));
        
        TableColumn memberIdColumn = new TableColumn(myResources.getProperty("memberId"));
        memberIdColumn.setMinWidth(125);
        memberIdColumn.setCellValueFactory(
                new PropertyValueFactory("memberId"));
        
        TableColumn dateOfBirthColumn = new TableColumn(myResources.getProperty("dateOfBirth"));
        dateOfBirthColumn.setMinWidth(125);
        dateOfBirthColumn.setCellValueFactory(
                new PropertyValueFactory("dateOfBirth"));
        
        TableColumn phoneNumberColumn = new TableColumn(myResources.getProperty("phoneNumber"));
        phoneNumberColumn.setMinWidth(200);
        phoneNumberColumn.setCellValueFactory(
                new PropertyValueFactory("phoneNumber"));
        
        TableColumn emailColumn = new TableColumn(myResources.getProperty("email"));
        emailColumn.setMinWidth(300);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory("email"));
        
        TableColumn statusColumn = new TableColumn(myResources.getProperty("status"));
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory("status"));
        
        TableColumn dateUpdateColumn = new TableColumn(myResources.getProperty("dateStatusUpdated"));
        dateUpdateColumn.setMinWidth(150);
        dateUpdateColumn.setCellValueFactory(
                new PropertyValueFactory("dateStatusUpdated"));
        
        tableOfScouts.getColumns().addAll(
                idColumn, 
                firstNameColumn, 
                middleNameColumn,
                lastNameColumn,
                memberIdColumn,
                dateOfBirthColumn,
                phoneNumberColumn,
                emailColumn,
                statusColumn,
                dateUpdateColumn
        );
        
        ScrollPane tableScrollPane = new ScrollPane();
        tableScrollPane.setPrefSize(DEFAULT_WIDTH, 300);
        tableScrollPane.setContent(tableOfScouts);
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
        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
        try {
            Vector entryList = (Vector) myModel.getState("Scouts");
            Enumeration entries = entryList.elements();
            
            while (entries.hasMoreElements()) {
                Scout nextScout = (Scout)entries.nextElement();
                
                ScoutTableModel scoutEntry = new ScoutTableModel(nextScout.getTableListView());
                tableData.add(scoutEntry);
            }
            tableOfScouts.setItems(tableData);
            
        } catch (Exception e) {
            // TODO: Handle exceptions
        }
    }
    
    protected void processAction(Event event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            myModel.stateChangeRequest("Back", null);
        }
        else if (source == firstNameSearchField || source == lastNameSearchField) {
            Properties searchTerms = new Properties();
            searchTerms.setProperty("FirstName", firstNameSearchField.getText());
            searchTerms.setProperty("LastName", lastNameSearchField.getText());
            myModel.stateChangeRequest("SearchScouts", searchTerms);
        }
        else if (source == removeButton) {
            ScoutTableModel itemSelected = tableOfScouts.getSelectionModel().getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoScoutSelected"));
            }
            else {
                myModel.stateChangeRequest("RemoveScout", itemSelected.getId());
            }
        }
        else if (source == editButton) {
            ScoutTableModel itemSelected = tableOfScouts.getSelectionModel().getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoScoutSelected"));
            }
            else {
                myModel.stateChangeRequest("EditScout", itemSelected.getId());
            }
        }
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
            
        } else if (key.equals("Scouts")) {
            getEntryTableModelValues();
        }
    }
    
}
