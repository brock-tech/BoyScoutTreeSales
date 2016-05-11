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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;

/**
 *
 * @author Andrew
 */
public class SellTreeView extends BaseView {
    protected Label barcodeDisplay;
    protected Label itemDescDisplay;
    protected Label costDisplay;
    
    protected TextField amountPaidField; // Has focus
    protected TextField customerNameField;
    protected TextField customerPhoneField;
    protected TextField customerEmailField;
    
    protected Label customerNamePrompt;
    protected Label customerPhonePrompt;
    
    protected RadioButton cashButton;
    protected RadioButton checkButton;
    
    protected Button submitButton;
    protected Button cancelButton;



    public SellTreeView(IModel model) {
        super(model, "SellTreeView");

        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("SelectedTree", this);
        myModel.subscribe("SelectedTreeType", this);
    }

    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);

        IFormItemStrategy formItemBuilder, currencyItemBuilder;
        Pane formItem;
        try {
            formItemBuilder = (IFormItemStrategy)Class.forName(
                    "userinterface.TopAlignFormItem").newInstance();
            currencyItemBuilder = (IFormItemStrategy)Class.forName(
                    "userinterface.TopAlignStandardCurrencyItem").newInstance();
        }
        catch (Exception cnfe) {
            System.err.printf("Form Item Strategy error: " + cnfe.getMessage());
            return content;
        }
        
        GridPane itemInfoGrid = new GridPane();
        itemInfoGrid.setHgap(10);
        itemInfoGrid.setVgap(10);
        itemInfoGrid.getStyleClass().addAll("pane2","grid");
        itemInfoGrid.setPrefWidth(DEFAULT_WIDTH);
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT);
        itemInfoGrid.getColumnConstraints().addAll(col1, col2);
        
        Label barcodeLabel = new Label(myResources.getProperty("barcode"));
        barcodeDisplay = new Label();
        itemInfoGrid.add(barcodeLabel, 0, 0);
        itemInfoGrid.add(barcodeDisplay, 1, 0);
        
        Label itemDescLabel = new Label(myResources.getProperty("itemDescription"));
        itemDescDisplay = new Label();
        itemInfoGrid.add(itemDescLabel, 0, 1);
        itemInfoGrid.add(itemDescDisplay, 1, 1);
        
        Label costLabel = new Label(myResources.getProperty("cost"));
        costDisplay = new Label();
        itemInfoGrid.add(costLabel, 0, 2);
        itemInfoGrid.add(costDisplay, 1, 2);
        
        content.getChildren().add(itemInfoGrid);
        
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.getStyleClass().addAll("pane2","grid");
        
        HBox paymentMethodContainer = new HBox(25);
        paymentMethodContainer.setAlignment(Pos.CENTER_LEFT);
        
        ToggleGroup paymentTG = new ToggleGroup();
        cashButton = new RadioButton(myResources.getProperty("cashButton"));
        cashButton.setToggleGroup(paymentTG);
        cashButton.setSelected(true);
        
        checkButton = new RadioButton(myResources.getProperty("checkButton"));
        checkButton.setToggleGroup(paymentTG);
        
        paymentMethodContainer.getChildren().addAll(cashButton, checkButton);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("paymentMethod"),
                paymentMethodContainer
        );
        formGrid.add(formItem, 0, 0, 2, 1);
        
        
        amountPaidField = new TextField();
        amountPaidField.setOnAction(submitHandler);
        formItem = currencyItemBuilder.buildControl(
                myResources.getProperty("amountPaidField"),
                amountPaidField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1, 2, 1);
        
        
        customerNameField = new TextField();
        customerNameField.setOnAction(submitHandler);
        customerNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                customerNamePrompt.setVisible(newValue);
            }
        });
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("customerNameField"),
                customerNameField
        );
        formItem.setPrefWidth(300);
        customerNamePrompt = new Label(myResources.getProperty("customerNamePrompt"));
        customerNamePrompt.setVisible(false);
        formGrid.add(formItem, 0, 2);
        formGrid.add(customerNamePrompt, 1, 2);

        customerPhoneField = new TextField();
        customerPhoneField.setOnAction(submitHandler);
        customerPhoneField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                customerPhonePrompt.setVisible(newValue);
            }
        });
        customerPhonePrompt = new Label(myResources.getProperty("customerPhonePrompt"));
        customerPhonePrompt.setVisible(false);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("customerPhoneField"),
                customerPhoneField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 3);
        formGrid.add(customerPhonePrompt, 1, 3);

        customerEmailField = new TextField();
        customerEmailField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("customerEmailField"),
                customerEmailField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 4);

        content.getChildren().add(formGrid);
        
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        submitButton = new Button(myResources.getProperty("submitButton"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);

        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);

        content.getChildren().add(buttonContainer);

        return content;
    }

    protected void processAction(Event event) {
        clearErrorMessage();

        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        else // Verify information in fields
        if (validate()) {
            // Submit data
            Properties newSaleData = new Properties();
            
            if (cashButton.isSelected()) {
                newSaleData.setProperty("PaymentMethod", "cash");
            }
            else {
                newSaleData.setProperty("PaymentMethod", "check");
            }
            
            newSaleData.setProperty("TransactionAmount", amountPaidField.getText());
            newSaleData.setProperty("CustomerName", customerNameField.getText());
            newSaleData.setProperty("CustomerPhone", customerPhoneField.getText());
            newSaleData.setProperty("CustomerEmail", customerEmailField.getText());
            
            myModel.stateChangeRequest("Submit", newSaleData);
        }
    }

    protected boolean validate() {
        // Nothing can be null
        String value;
        value = amountPaidField.getText();
        if (!value.matches(myResources.getProperty("currencyPattern"))) {
            displayErrorMessage(myResources.getProperty("errTransactionAmountNull"));
            amountPaidField.requestFocus();
            return false;
        }
        
        value = customerNameField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errNameNull"));
            customerNameField.requestFocus();
            return false;
        }
        value = customerPhoneField.getText().trim();
        if (!value.matches(myResources.getProperty("phoneNumPattern"))) {
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
        return true;
    }       
    
    @Override
    public void updateState(String key, Object value) {
        switch (key) {
            case "UpdateStatusMessage":
                displayMessage((String)myModel.getState("UpdateStatusMessage"));
                
            case "SelectedTree": {
                IModel tree = (IModel)myModel.getState("SelectedTree");
                barcodeDisplay.setText((String)tree.getState("BarCode"));
                break;
            }
            
            case "SelectedTreeType": 
                IModel treeType = (IModel)myModel.getState("SelectedTreeType");
                itemDescDisplay.setText((String)treeType.getState("TypeDescription"));
                costDisplay.setText(String.format(myResources.getProperty("currencyMsgFormat"),
                        (String)treeType.getState("Cost")));
                amountPaidField.requestFocus();
                break;
        }
    }

}
