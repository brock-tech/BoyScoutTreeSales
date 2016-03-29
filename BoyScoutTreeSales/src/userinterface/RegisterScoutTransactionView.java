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

/**
 *
 * @author mike
 */
public class RegisterScoutTransactionView extends BaseView {
    static final String DATE_FORMAT = "";
    
    protected TextField firstNameField;
    protected TextField middleNameField;
    protected TextField lastNameField;
    protected TextField dobField;
    protected TextField phoneNumField;
    protected TextField emailField;
    protected TextField troopIdField;
    protected ComboBox statusBox;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;

    public RegisterScoutTransactionView(IModel model) {
        super(model, "ScoutFormView");
        
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
        
        firstNameField = new TextField();
        firstNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("First Name:", firstNameField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 0);
        
        middleNameField = new TextField();
        middleNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Middle Name:", middleNameField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 1, 0);
        
        lastNameField = new TextField();
        lastNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Last Name:", lastNameField);
        formItem.setPrefWidth(350);
        formGrid.add(formItem, 0, 1, 2, 1);
        
        troopIdField = new TextField();
        troopIdField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Troop ID:", troopIdField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 2);
        
        dobField = new TextField();
        dobField.setOnAction(submitHandler);
        dobField.setPromptText("yyyy-mm-dd");
        formItem = formItemBuilder.buildControl("Date of Birth:", dobField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 3);
        
        phoneNumField = new TextField();
        phoneNumField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Phone Number:", phoneNumField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 4);
        
        emailField = new TextField();
        emailField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl("Email:", emailField);
        formItem.setPrefWidth(350);
        formGrid.add(formItem, 0, 5, 2, 1);
        
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Active",
                "Inactive"
        );
        statusBox = new ComboBox(statusOptions);
        statusBox.setValue("Active");
        formItem = formItemBuilder.buildControl("Status:", statusBox);
        formItem.setPrefWidth(150);
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
            // Verify information in fields and submit
            Properties p = new Properties();
            
            String firstName = firstNameField.getText();
            if ((firstName == null) || firstName.equals("")) {
                displayErrorMessage("First Name required!");
                firstNameField.requestFocus();
            }
            p.setProperty("FirstName", firstName);
            
            // Middle Name can be null
            String middleName = middleNameField.getText();
            p.setProperty("MiddleName", middleName);
            
            String lastName = lastNameField.getText();
            if ((lastName == null) || lastName.equals("")) {
                displayErrorMessage("Last Name required!");
                lastNameField.requestFocus();
            }
            p.setProperty("LastName", lastName);
        }
    }
    
    /*
    private void clearForm() {
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        dobField.setText("");
        phoneNumField.setText("");
        emailField.setText("");
        troopIdField.setText("");
        
        this.requestLayout();
    }
    */
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
    }
}
