//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
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


import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.EditTreeTypeTransaction;
import model.TreeTypeTableModel;
import model.TreeTypeCollection;

/**
 *
 * @author Andrew
 */
public class EditTreeTypeFormView extends BaseView {
    
    protected TextField barcodePrefixField;
    protected TextField descriptionField;
    protected TextField costField;
    protected TextField searchField;
    protected Button submitButton;
    protected Button searchButton;
    protected Button searchAllButton;
    protected TableView<TreeTypeTableModel> treeTypeTable;
    protected Button cancelButton;

    
    
    public EditTreeTypeFormView(IModel model) {
        super(model, "TreeTypeFormView");
      
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
        
        treeTypeTable = new TableView<TreeTypeTableModel>();
        treeTypeTable.getSelectionModel();
        
        TableColumn IDColumn = new TableColumn("ID") ;
        IDColumn.setMinWidth(100);
        IDColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTypeTableModel, String>("ID"));

        TableColumn TypeDescriptionColumn = new TableColumn("TypeDescription") ;
        TypeDescriptionColumn.setMinWidth(100);
        TypeDescriptionColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTypeTableModel, String>("TypeDescription"));

        TableColumn CostColumn = new TableColumn("Cost") ;
        CostColumn.setMinWidth(100);
        CostColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTypeTableModel, String>("Cost"));
        
        TableColumn BarcodePrefixColumn = new TableColumn("BarcodePrefix") ;
        BarcodePrefixColumn.setMinWidth(100);
        BarcodePrefixColumn.setCellValueFactory(
                      new PropertyValueFactory<TreeTypeTableModel, String>("BarcodePrefix"));
        
        
        //String className = new Exception().getStackTrace()[0].getClassName();
        //if(className.equals("EditTreeTypeTransaction"))
        //{
            searchField = new TextField();
            searchField.setOnAction(submitHandler);
            formItem = formItemBuilder.buildControl(
                    myResources.getProperty("searchField"),
                    searchField
            );
            formItem.setPrefWidth(300);
            formGrid.add(formItem, 0, 3);
        //}
       
         HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        
        searchAllButton = new Button(myResources.getProperty("searchAllButton"));
        searchAllButton.setOnAction(submitHandler);
        searchAllButton.setPrefWidth(100);
        buttonContainer.getChildren().add(searchAllButton);
        
        searchButton = new Button(myResources.getProperty("searchButton"));
        searchButton.setOnAction(submitHandler);
        searchButton.setPrefWidth(100);
        buttonContainer.getChildren().add(searchButton);
        
        submitButton = new Button(myResources.getProperty("submitButton"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);
        
//        clearFormButton = new Button("Clear Form");
//        clearFormButton.setOnAction(submitHandler);
//        clearFormButton.setPrefWidth(100);
//        buttonContainer.getChildren().add(clearFormButton);
        
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);
        
        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        
        return content;
    }
    
    protected void processAction(Event event) {
        clearErrorMessage();
        
        if (event.getSource() == cancelButton) 
        {
            myModel.stateChangeRequest("Cancel", "");
        }
        if(event.getSource() == searchButton)
        {
        
        }
        if(event.getSource() == searchAllButton)
        {
        
        }
        
            if(!searchField.getText().equals(""))
            {
                 MessageFormat formatter = new MessageFormat("", myLocale);
                try
                {
                    TreeTypeCollection searchedTT = new TreeTypeCollection();
                    searchedTT.findTypesWithBarcodePrefix(searchField.getText());  
                }
                catch(InvalidPrimaryKeyException e)
                {
                    searchField.setText("Tree Type Not Found");
                }

            }
            else {
                // Verify information in fields
                if (validate()) 
                {
                    // Submit data
                    Properties newTreeTypeData = new Properties();
                   
                    newTreeTypeData.setProperty("TypeDescription", descriptionField.getText());
                    newTreeTypeData.setProperty("Cost", costField.getText());
                    newTreeTypeData.setProperty("BarcodePrefix", barcodePrefixField.getText());
                   
                    myModel.stateChangeRequest("Submit", newTreeTypeData);
                }
            }
        }
    
    protected boolean validate() {
        // First Name is NOT NULL
        String value = barcodePrefixField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errBarcodeNull"));
            barcodePrefixField.requestFocus();
            return false;
        }

        value = costField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errCostNull"));
            costField.requestFocus();
            return false;
        }
        
        return true;
    }   
    
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
    }
}

