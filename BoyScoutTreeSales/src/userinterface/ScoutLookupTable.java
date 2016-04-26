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

import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Scout;
import static userinterface.BaseView.DEFAULT_WIDTH;

/**
 *
 * @author mike
 */
public class ScoutLookupTable extends Group {
    protected TextField firstNameSearchField;
    protected TextField lastNameSearchField;
    protected TableView<ScoutTableModel> tableOfScouts;
    
    protected Locale myLocale;
    protected ResourceBundle myResources;
    
    protected EventHandler<ActionEvent> onAction;
    
    public ScoutLookupTable() {
        myLocale = SystemLocale.getInstance();
        myResources = ResourceBundle.getBundle("userinterface.i18n.EditScoutTransactionView", myLocale);
        
        createContent();
    }
    
    private void createContent() {
        Object ref = this;
        EventHandler<ActionEvent> eventPasser = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ActionEvent newEvent = new ActionEvent(ref, event.getTarget());
                onAction.handle(newEvent);
            }
        };
        
        IFormItemStrategy formItemBuilder;
        try {
            formItemBuilder = (IFormItemStrategy)Class.forName(
                    "userinterface.TopAlignFormItem").newInstance();
        }
        catch (Exception cnfe) {
            System.err.printf("Form Item Strategy error: " + cnfe.getMessage());
            return;
        }
        
        firstNameSearchField = new TextField();
        firstNameSearchField.setPromptText(myResources.getString("searchPrompt"));
        firstNameSearchField.setOnAction(eventPasser);
        Pane firstNameFormItem = formItemBuilder.buildControl(
                myResources.getString("firstName"), firstNameSearchField);
        firstNameFormItem.setPrefWidth(DEFAULT_WIDTH / 2);
        
        lastNameSearchField = new TextField();
        lastNameSearchField.setOnAction(eventPasser);
        lastNameSearchField.setPromptText(myResources.getString("searchPrompt"));
        Pane lastNameFormItem = formItemBuilder.buildControl(
                myResources.getString("lastName"), lastNameSearchField);
        lastNameFormItem.setPrefWidth(DEFAULT_WIDTH / 2);
        
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER);
        searchContainer.getChildren().addAll(firstNameFormItem, lastNameFormItem);
        
        TableColumn idColumn = new TableColumn(myResources.getString("id"));
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(
                new PropertyValueFactory("id"));
        idColumn.setVisible(false);
        
        TableColumn firstNameColumn = new TableColumn(myResources.getString("firstName"));
        firstNameColumn.setMinWidth(125);
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory("firstName"));
        
        TableColumn middleNameColumn = new TableColumn(myResources.getString("middleName"));
        middleNameColumn.setMinWidth(125);
        middleNameColumn.setCellValueFactory(
                new PropertyValueFactory("middleName"));
        
        TableColumn lastNameColumn = new TableColumn(myResources.getString("lastName"));
        lastNameColumn.setMinWidth(125);
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory("lastName"));
        
        TableColumn memberIdColumn = new TableColumn(myResources.getString("memberId"));
        memberIdColumn.setMinWidth(125);
        memberIdColumn.setCellValueFactory(
                new PropertyValueFactory("memberId"));
        
        TableColumn dateOfBirthColumn = new TableColumn(myResources.getString("dateOfBirth"));
        dateOfBirthColumn.setMinWidth(125);
        dateOfBirthColumn.setCellValueFactory(
                new PropertyValueFactory("dateOfBirth"));
        
        TableColumn phoneNumberColumn = new TableColumn(myResources.getString("phoneNumber"));
        phoneNumberColumn.setMinWidth(200);
        phoneNumberColumn.setCellValueFactory(
                new PropertyValueFactory("phoneNumber"));
        
        TableColumn emailColumn = new TableColumn(myResources.getString("email"));
        emailColumn.setMinWidth(300);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory("email"));
        
        TableColumn statusColumn = new TableColumn(myResources.getString("status"));
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory("status"));
        
        TableColumn dateUpdateColumn = new TableColumn(myResources.getString("dateStatusUpdated"));
        dateUpdateColumn.setMinWidth(150);
        dateUpdateColumn.setCellValueFactory(
                new PropertyValueFactory("dateStatusUpdated"));
        
        tableOfScouts = new TableView<>();
        tableOfScouts.getSelectionModel();
        tableOfScouts.setEditable(false);
        tableOfScouts.getColumns().addAll(
                idColumn, firstNameColumn, middleNameColumn, lastNameColumn,
                memberIdColumn, dateOfBirthColumn, phoneNumberColumn,
                emailColumn, statusColumn, dateUpdateColumn
        );
        
        ScrollPane tableScrollPane = new ScrollPane();
        tableScrollPane.setPrefSize(DEFAULT_WIDTH, 300);
        tableScrollPane.setContent(tableOfScouts);
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10, 10, 10, 10));
        
        content.getChildren().addAll(searchContainer, tableScrollPane);
        
        this.getChildren().add(content);
    }
    
    public void setOnAction(EventHandler<ActionEvent> handler) {
        onAction = handler;
    }
    
    public Properties getSearchTerms() {
        Properties searchTerms = new Properties();
        searchTerms.setProperty("FirstName", firstNameSearchField.getText());
        searchTerms.setProperty("LastName", lastNameSearchField.getText());
        return searchTerms;
    }
    
    public ScoutTableModel getSelectedItem() {
        return tableOfScouts.getSelectionModel().getSelectedItem();
    }
    
    public void setItems(Vector tableData) {
        getEntryTableModelValues(tableData);
    }
    
    protected void getEntryTableModelValues(Vector tableData) {
        ObservableList<ScoutTableModel> obsTableData = FXCollections.observableArrayList();
        try {
            Enumeration entries = tableData.elements();
            
            while (entries.hasMoreElements()) {
                Scout nextScout = (Scout)entries.nextElement();
                
                ScoutTableModel scoutEntry = new ScoutTableModel(nextScout.getTableListView());
                obsTableData.add(scoutEntry);
            }
            tableOfScouts.setItems(obsTableData);
            
        } catch (Exception e) {
            // TODO: Handle exceptions
        }
    }
}
