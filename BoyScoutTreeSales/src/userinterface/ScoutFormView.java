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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

/**
 *
 * @author mike
 */
public class ScoutFormView extends BaseView {
    
    protected TextField firstNameField;
    protected TextField middleNameField;
    protected TextField lastNameField;
    protected DatePicker dobField;
    protected TextField phoneNumField;
    protected TextField emailField;
    protected TextField memberIdField;
    protected Label phoneNumPrompt;
    
    protected Button submitButton;
    protected Button cancelButton;

    public ScoutFormView(IModel model) {
        super(model, "ScoutFormView");
        
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("ScoutToDisplay", this);
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
        
        Text title = new Text(myResources.getProperty("promptText"));
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

        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.getStyleClass().addAll("pane","grid");
        
        firstNameField = new TextField();
        firstNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("firstNameField"),
                firstNameField
        );
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 0, 2, 1);
        
        middleNameField = new TextField();
        middleNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("middleNameField"),
                middleNameField
        );
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 1, 2, 1);
        
        lastNameField = new TextField();
        lastNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("lastNameField"),
                lastNameField
        );
        formItem.setPrefWidth(350);
        formGrid.add(formItem, 0, 2, 2, 1);
        
        memberIdField = new TextField();
        memberIdField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(myResources.getProperty("troopIdField"),
                memberIdField
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 3, 2, 1);
        
        dobField = new DatePicker();
        dobField.setValue(LocalDate.now().minusYears(10));
        dobField.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? 
                        DateTimeFormatter.ofPattern(myResources.getProperty("dateFormat")).format(date) : "";
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty())
                    return LocalDate.parse(string, 
                            DateTimeFormatter.ofPattern(myResources.getProperty("dateFormat")));
                else return null;
            }
        });
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("dobField"),
                dobField
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 4);
        
        
        phoneNumField = new TextField();
        phoneNumField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                phoneNumPrompt.setVisible(newValue);
            }
        });
        phoneNumField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("phoneNumField"),
                phoneNumField
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 5);
        
        phoneNumPrompt = new Label(myResources.getProperty("phoneNumPrompt"));
        phoneNumPrompt.setVisible(false);
        formGrid.add(phoneNumPrompt, 1, 5);
        
        emailField = new TextField();
        emailField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("emailField"),
                emailField
        );
        formItem.setPrefWidth(350);
        formGrid.add(formItem, 0, 6, 2, 1);
        
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
        
        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        
        return content;
    }
    
    protected void processAction(Event event) {
        clearErrorMessage();
        
        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        else if (event.getSource() == submitButton) {
            // Verify information in fields
            if (validate()) {
                // Submit data
                Properties newScoutData = new Properties();
                newScoutData.setProperty("FirstName", firstNameField.getText());
                
                newScoutData.setProperty("MiddleName", middleNameField.getText());
                
                newScoutData.setProperty("LastName", lastNameField.getText());
                
                newScoutData.setProperty("MemberID", memberIdField.getText());
                
                newScoutData.setProperty("DateOfBirth", dobField.getValue().format(
                        DateTimeFormatter.ISO_DATE));
                
                newScoutData.setProperty("PhoneNumber", phoneNumField.getText());
                
                newScoutData.setProperty("Email", emailField.getText());
                
                newScoutData.setProperty("Status", "Active");
                
                myModel.stateChangeRequest("Submit", newScoutData);
            }
        }
    }
    
    protected boolean validate() {
        // First Name is NOT NULL
        String value = firstNameField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errFirstNameNull"));
            firstNameField.requestFocus();
            return false;
        }
        
        // Middle name can be null, but add an empty field modifier
        value = middleNameField.getText();
        if ((value == null) || "".equals(value)) {
            middleNameField.setText("<empty>");
        }
        
        // Last Name is NOT NULL
        value = lastNameField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errLastNameNull"));
            lastNameField.requestFocus();
            return false;
        }
        
        value = memberIdField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errTroopIdNull"));
            memberIdField.requestFocus();
            return false;
        }
        
        // Date of Birth is not null, and must match format
        LocalDate date = dobField.getValue();
        if (date.isAfter(LocalDate.now().minusYears(8))) {
            displayErrorMessage(myResources.getProperty("errDOBFormat"));
            dobField.requestFocus();
            return false;
        }
        
        // Phone number is not null and must match format
        value = phoneNumField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errPhoneNumNull"));
            phoneNumField.requestFocus();
            return false;
        }
        if (!value.matches(myResources.getProperty("phoneNumPattern"))) {
            displayErrorMessage(myResources.getProperty("errPhoneNumFormat"));
            phoneNumField.requestFocus();
            return false;
        }
        
        // Email is not null
        value = emailField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errEmailNull"));
            phoneNumField.requestFocus();
            return false;
        }
        
        return true;
    }   
    
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
        else if (key.equals("ScoutToDisplay")) {
            IModel selectedScout = (IModel)value;
            if (selectedScout != null) {
                firstNameField.setText((String) selectedScout.getState("FirstName"));
                middleNameField.setText((String) selectedScout.getState("MiddleName"));
                lastNameField.setText((String) selectedScout.getState("LastName"));
                memberIdField.setText((String) selectedScout.getState("MemberID"));
                dobField.setValue(LocalDate.parse((String)selectedScout.getState("DateOfBirth"),
                        DateTimeFormatter.ISO_LOCAL_DATE));
                phoneNumField.setText((String) selectedScout.getState("PhoneNumber"));
                emailField.setText((String) selectedScout.getState("Email"));
            }
        }
    }
}
