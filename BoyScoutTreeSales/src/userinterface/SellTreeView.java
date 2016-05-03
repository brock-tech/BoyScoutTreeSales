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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Tree;
import model.TreeType;

/**
 *
 * @author Andrew
 */
public class SellTreeView extends BaseView {
    protected TextField sessionIDField;
    protected TextField transactionTypeField;
    protected TextField barcodeField;
    protected TextField transactionAmountField;
    protected TextField paymentMethodField;
    protected TextField customerNameField;
    protected TextField customerPhoneField;
    protected TextField customerEmailField;
    protected TextField transactionDateField;
    protected TextField transactionTimeField;
    protected String sessionID;
    protected Button checkButton;
    protected Button cashButton;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;



    public SellTreeView(IModel model) {
        super(model, "SellTreeView");

        myModel.subscribe("UpdateStatusMessage", this);
         myModel.subscribe("SaleToDisplay", this);
        sessionID = (String)myModel.getState("Session");
        System.out.println("session " + sessionID);
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
        formGrid.getStyleClass().addAll("pane2","grid");

        sessionIDField = new TextField();
        sessionIDField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("sessionIDField"),
                sessionIDField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1);


        barcodeField = new TextField();
        barcodeField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("barcodeField"),
                barcodeField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 1, 1);

        customerNameField = new TextField();
        customerNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("customerNameField"),
                customerNameField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 2);

        customerPhoneField = new TextField();
        customerPhoneField.setOnAction(submitHandler);
        customerPhoneField.setPromptText(myResources.getProperty("phoneNumPrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("customerPhoneField"),
                customerPhoneField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 1, 2);

        customerEmailField = new TextField();
        customerEmailField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("customerEmailField"),
                customerEmailField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0,3);

        transactionDateField = new TextField();
        transactionDateField.setOnAction(submitHandler);
        transactionDateField.setPromptText(myResources.getProperty("datePrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("transactionDateField"),
                transactionDateField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 1, 3);

        transactionTimeField = new TextField();
        transactionTimeField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("transactionTimeField"),
                transactionTimeField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 4);

        transactionAmountField = new TextField();
        transactionAmountField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("transactionAmountField"),
                transactionAmountField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 1, 4);

         HBox buttonContainer = new HBox(20);
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

    protected void processAction(Event event)
    {
        clearErrorMessage();

        if (event.getSource() == cancelButton)
        {
            myModel.stateChangeRequest("Cancel", "");
        }
        else
        {      // Verify information in fields
         if (validate())
         {
             // Submit data
             Properties newSaleData = new Properties();

             //newSaleData.setProperty("ID", idField.getText());
             newSaleData.setProperty("SessionID", sessionIDField.getText());
             newSaleData.setProperty("TransactionType", "Tree Sale");
             newSaleData.setProperty("Barcode", barcodeField.getText());

             newSaleData.setProperty("TransactionAmount", transactionAmountField.getText());
             newSaleData.setProperty("SessionID", sessionIDField.getText());
             newSaleData.setProperty("PaymentMethod", paymentMethodField.getText());

             newSaleData.setProperty("CustomerName", customerNameField.getText());
             newSaleData.setProperty("CustomerPhone", customerPhoneField.getText());
             newSaleData.setProperty("CustomerEmail", customerEmailField.getText());

             newSaleData.setProperty("TransactionDate", transactionDateField.getText());
             newSaleData.setProperty("TransactionTime", transactionTimeField.getText());

             LocalDateTime currentDate = LocalDateTime.now();
             String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
             newSaleData.setProperty("DateStatusUpdated", dateLastUpdate);
             
            
             //System.out.println("barcode " + barcodePrefixField.getText() );*/
             myModel.stateChangeRequest("Submit", newSaleData);
             displayMessage((String)myModel.getState("UpdateStatusMessage"));
         }
        }
    }

    protected boolean validate() {
        // Nothing can be null
        String value = sessionIDField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errSessionNull"));
            sessionIDField.requestFocus();
            return false;
        }
        value = barcodeField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errBarcodeNull"));
            barcodeField.requestFocus();
            return false;
        }
        value = transactionAmountField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errTransactionAmountNull"));
            transactionAmountField.requestFocus();
            return false;
        }
        value = customerNameField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errNameNull"));
            customerNameField.requestFocus();
            return false;
        }
        value = customerPhoneField.getText();
        if (!value.matches(myResources.getProperty("phoneNumFormat"))) {
            displayErrorMessage(myResources.getProperty("errPhoneNull"));
            customerPhoneField.requestFocus();
            return false;
        }
        value = customerEmailField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errEmailNull"));
            customerEmailField.requestFocus();
            return false;
        }
        value = transactionTimeField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errTransactionTimeNull"));
            transactionTimeField.requestFocus();
            return false;
        }
        return true;
    }       
    
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));

        }
        else if (key.equals("SaleToDisplay")) {
             Tree selectedTree = (Tree)value;
             TreeType selTree = null;
            try {
                selTree = new TreeType((String) selectedTree.getState("TreeType"),1);
                System.out.println(selTree.getState("Cost"));
                System.out.println(selectedTree.getState("TreeType"));
            } catch (InvalidPrimaryKeyException ex) {
                Logger.getLogger(SellTreeView.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (selectedTree != null) {

               LocalDateTime currentDate = LocalDateTime.now();
               String transactionTime = currentDate.format(DateTimeFormatter.ISO_TIME);
               String transactionDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
               transactionTimeField.setText(transactionTime);
               transactionDateField.setText(transactionDate);
               barcodeField.setText((String) selectedTree.getState("BarCode"));
               sessionIDField.setText(sessionID);
               transactionAmountField.setText((String) selTree.getState("Cost"));

            }
        }
    }

}
