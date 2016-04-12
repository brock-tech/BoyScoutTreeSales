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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.EditTreeTypeTransaction;
import model.TreeTypeCollection;

/**
 *
 * @author Andrew
 */
public class TreeTypeFormView extends BaseView {
    
    protected TextField barcodePrefixField;
    protected TextField descriptionField;
    protected TextField costField;
    protected TextField searchField;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;

    
    
    public TreeTypeFormView(IModel model) {
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
        
        barcodePrefixField = new TextField();
        barcodePrefixField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("barcodePrefixField"),
                barcodePrefixField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 0);
        
        descriptionField = new TextField();
        descriptionField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("descriptionField"),
                descriptionField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1);
        
        costField = new TextField();
        costField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("costField"),
                costField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 2);
        
        
        String className = new Exception().getStackTrace()[0].getClassName();
        if(className.equals("EditTreeTypeTransaction"))
        {
            searchField = new TextField();
            searchField.setOnAction(submitHandler);
            formItem = formItemBuilder.buildControl(
                    myResources.getProperty("searchField"),
                    searchField
            );
            formItem.setPrefWidth(300);
            formGrid.add(formItem, 0, 3);
        }
       
         HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
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
        else
        {
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
                    newTreeTypeData.setProperty("barcodePrefix", barcodePrefixField.getText());
                    newTreeTypeData.setProperty("description", descriptionField.getText());
                    newTreeTypeData.setProperty("cost", costField.getText());

                    myModel.stateChangeRequest("Submit", newTreeTypeData);
                }
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
