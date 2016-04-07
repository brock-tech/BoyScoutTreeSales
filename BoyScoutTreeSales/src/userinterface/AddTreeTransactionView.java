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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 *
 * @author PHONG
 */
public class AddTreeTransactionView extends BaseView {
    static final String DATE_FORMAT = "";
    
    protected TextField treeBarCode;
    protected TextField salePrice;
    protected TextField nameField;
    protected TextField phoneNumField;
    protected TextField emailField;
    protected TextField dateField;
    protected TextField timeField;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;

    public AddTreeTransactionView(IModel model) {
        super(model, "TreeFormView");
        
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
        
        salePrice = new TextField();
        salePrice.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Sale Price:", salePrice);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 1);
        
        nameField = new TextField();
        nameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Customer Name:", nameField);
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 2);
        
        emailField = new TextField();
        emailField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Customer Email:", emailField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 3);
        
        phoneNumField = new TextField();
        phoneNumField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Customer Phone Number:", phoneNumField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 4);
        
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        dateField = new TextField();
        dateField.setText(dateFormat.format(date));
        dateField.setEditable(false);
        dateField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Current Date:", dateField);
        formItem.setPrefWidth(100);
        formGrid.add(formItem, 0, 5);
        
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        timeField = new TextField();
        timeField.setText(timeFormat.format(time));
        timeField.setEditable(false);
        timeField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Current Time:", timeField);
        formItem.setPrefWidth(100);
        formGrid.add(formItem, 0, 6);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
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
        
        return content;
    }
        
    private void processAction(Event event) {
        clearErrorMessage();
        
        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        //else if (event.getSource() == clearFormButton) {
        //    clearForm();
        //}
        else {
            if (validate()){
            // Verify information in fields and submit
                Properties p = new Properties();
                
                p.setProperty("barcode", treeBarCode.getText());
                p.setProperty("saleprice", salePrice.getText());
                p.setProperty("cname", nameField.getText());
                p.setProperty("cphoneNum", phoneNumField.getText());
                p.setProperty("cemail", emailField.getText());
                p.setProperty("date", dateField.getText());
                p.setProperty("time", timeField.getText());
                myModel.stateChangeRequest("Submit", p);
            }
        }
    }
    
    protected boolean validate(){
        String treeBar = treeBarCode.getText();
        String price = salePrice.getText();
        String name = nameField.getText();
        
        if ((treeBar == null) || treeBar.equals("")) {
            displayErrorMessage("Tree Bar Code required!");
            treeBarCode.requestFocus();
            return false;
        }
        else if ((price == null) || price.equals("")) {
            displayErrorMessage("Sale Price required!");
            salePrice.requestFocus();
            return false;
        }
        else if ((name == null) || name.equals("")) {
            displayErrorMessage("Name required!");
            nameField.requestFocus();
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
