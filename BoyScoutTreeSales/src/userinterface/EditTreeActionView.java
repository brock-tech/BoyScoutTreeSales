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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.EditTreeAction;
import model.Tree;
import model.TreeTableModel;


/**
 *
 * @author PHONG
 */
public class EditTreeActionView extends BaseView {
    static final String DATE_FORMAT = "";
    
    protected TextField treeBarCode;
    protected Button searchButton;
    protected TableView<TreeTableModel> tableOfTrees;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;

    public EditTreeActionView(IModel model) {
        super(model, "EditTreeActionView");
        
        myModel.subscribe("UpdateStatusMessage", this);
    }
    
    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        IFormItemStrategy formItemBuilder = FormItemFactory.getFormItem("TopAlignFormItem");
        Pane formItem;
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        treeBarCode = new TextField();
        treeBarCode.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Tree Barcode:", treeBarCode);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 0);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        tableOfTrees = new TableView<TreeTableModel>();
        tableOfTrees.getSelectionModel();
        
        TableColumn BarCodeColumn = new TableColumn("BarCode") ;
        BarCodeColumn.setMinWidth(100);
        BarCodeColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("BarCode"));

        TableColumn TreeTypeColumn = new TableColumn("TreeType") ;
        TreeTypeColumn.setMinWidth(100);
        TreeTypeColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("TreeType"));

        TableColumn SalePriceColumn = new TableColumn("SalePrice") ;
        SalePriceColumn.setMinWidth(100);
        SalePriceColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("SalePrice"));

        TableColumn CNameColumn = new TableColumn("CName") ;
        CNameColumn.setMinWidth(100);
        CNameColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("CName"));

        TableColumn CPhoneNumColumn = new TableColumn("CPhoneNum") ;
        CPhoneNumColumn.setMinWidth(100);
        CPhoneNumColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("CPhoneNum"));

        TableColumn CemailColumn = new TableColumn("Cemail") ;
        CemailColumn.setMinWidth(100);
        CemailColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("Cemail"));
        
        TableColumn DateStatusUpdatedColumn = new TableColumn("DateStatusUpdated") ;
        DateStatusUpdatedColumn.setMinWidth(100);
        DateStatusUpdatedColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("DateStatusUpdated"));
        
        TableColumn TimeStatusUpdatedColumn = new TableColumn("TimeStatusUpdated") ;
        TimeStatusUpdatedColumn.setMinWidth(100);
        TimeStatusUpdatedColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTableModel, String>("TimeStatusUpdated"));
        
        tableOfTrees.getColumns().addAll(BarCodeColumn, TreeTypeColumn, SalePriceColumn, 
            CNameColumn, CPhoneNumColumn, CemailColumn, DateStatusUpdatedColumn, TimeStatusUpdatedColumn);

        ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setPrefSize(520, 150);
                    scrollPane.setContent(tableOfTrees);

        formGrid.add(scrollPane, 0, 1);
        
        searchButton = new Button("Search");
        searchButton.setOnAction(submitHandler);
        searchButton.setPrefWidth(100);
        buttonContainer.getChildren().add(searchButton);
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);
        
//        clearFormButton = new Button("Clear Form");
//        clearFormButton.setOnAction(submitHandler);
//        clearFormButton.setPrefWidth(100);
//        buttonContainer.getChildren().add(clearFormButton);
        
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);

        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        getEntryTableModelValues();
        return content;
    }
        
    private void processAction(Event event) {
        clearErrorMessage();
        Properties props = new Properties();
        
        props.setProperty("BarCode", treeBarCode.getText());
        System.out.println(props);
        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        else if (event.getSource() == searchButton){
            myModel.stateChangeRequest("Search", props);
            getEntryTableModelValuesByBarCode();
        }
        //else if (event.getSource() == clearFormButton) {
        //    clearForm();
        //}
    }
    
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
    }
    
    protected void getEntryTableModelValues()
    {
        ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            EditTreeAction editTree = (EditTreeAction)myModel.getState("getTree");
            Vector entryList = (Vector)editTree.getState("getAllTrees");

            Enumeration entries = entryList.elements();
            
            while (entries.hasMoreElements() == true)
            {
                Tree nextTree = (Tree)entries.nextElement();

                Vector<String> view = nextTree.getEntryListView();
                // add this list entry to the list
                TreeTableModel nextTableRowData = new TreeTableModel(view);

                tableData.add(nextTableRowData);

                }
                tableOfTrees.setItems(tableData);
            }
            catch (Exception e) {//SQLException e) {
                    // Need to handle this exception
            }
    }
    
    protected void getEntryTableModelValuesByBarCode()
    {
        ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            EditTreeAction editTree = (EditTreeAction)myModel.getState("getTree");
            Vector entryList = (Vector)editTree.getState("getTreeList");

            Enumeration entries = entryList.elements();
            
            while (entries.hasMoreElements() == true)
            {
                Tree nextTree = (Tree)entries.nextElement();

                Vector<String> view = nextTree.getEntryListView();
                // add this list entry to the list
                TreeTableModel nextTableRowData = new TreeTableModel(view);

                tableData.add(nextTableRowData);

                }
                tableOfTrees.setItems(tableData);
            }
            catch (Exception e) {//SQLException e) {
                    // Need to handle this exception
            }
    }
}
