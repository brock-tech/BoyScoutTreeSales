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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import static userinterface.BaseView.DEFAULT_WIDTH;

/**
 *
 * @author mike
 */
public class OpenShiftFormView extends BaseView {
    private static final int SHIFT_HOURS = 3;
    
    protected TextField scoutNameField;
    protected TextField memberIdField;
    protected TimeEntry startTimeField;
    protected TimeEntry endTimeField;
    protected TextField companionNameField;
    protected Spinner companionHoursField;
    
    protected TextField startCashField;
    protected Button submitButton;
    protected Button backButton;

    public OpenShiftFormView(IModel model) {
        super(model, "OpenShiftFormView");
        
        myModel.subscribe("SelectedScout", this);
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
        
        LocalDateTime currentDateTime = (LocalDateTime)myModel.getState("CurrentDateTime");
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        
        Text titleText = new Text(myResources.getProperty("subtitleText"));
        titleText.setTextAlignment(TextAlignment.CENTER);
        
        HBox titleContainer = new HBox(10);
        titleContainer.setPrefSize(DEFAULT_WIDTH, 40.0);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().add(titleText);
        
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        IFormItemStrategy formItemBuilder;
        Pane formItem;
        try {
            formItemBuilder = (IFormItemStrategy)Class.forName(
                    "userinterface.TopAlignFormItem"
            ).newInstance();
        }
        catch (Exception cnfe) {
            System.err.printf("Form Item Strategy error: " + cnfe.getMessage());
            return content;
        }
        
        scoutNameField = new TextField();
        scoutNameField.setEditable(false);
        formItem = formItemBuilder.buildControl(myResources.getProperty("scoutName"), scoutNameField);
        formItem.setPrefWidth(500);
        formGrid.add(formItem, 0, 0);
        
        memberIdField = new TextField();
        memberIdField.setEditable(false);
        formItem = formItemBuilder.buildControl(myResources.getProperty("memberId"), memberIdField);
        formItem.setPrefWidth(500);
        formGrid.add(formItem, 0, 1);
        
        try {
            startTimeField = (TimeEntry) Class.forName(
                    myResources.getProperty("timeEntry")).newInstance();
            endTimeField = (TimeEntry) Class.forName(
                    myResources.getProperty("timeEntry")).newInstance();
        } catch (Exception cnfe) {
            System.err.printf("Class load error: " + cnfe.getMessage());
            return content;
        }
        
        startTimeField.setTime(currentDateTime.toLocalTime());
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("startTime"), startTimeField);
        formGrid.add(formItem, 0, 2);
        
        endTimeField.setTime(currentDateTime.toLocalTime().plusHours(SHIFT_HOURS));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("endTime"), endTimeField);
        formGrid.add(formItem, 0, 3);
        
        companionNameField = new TextField();
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("companionName"), companionNameField);
        formItem.setPrefWidth(500);
        formGrid.add(formItem, 0, 4);
        
        companionHoursField = new Spinner(0, 24, SHIFT_HOURS);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("companionHours"), companionHoursField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 5);
        
        submitButton = new Button(myResources.getProperty("submit"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        
        backButton = new Button(myResources.getProperty("back"));
        backButton.setOnAction(submitHandler);
        backButton.setPrefWidth(100);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(submitButton, backButton);
        formGrid.add(buttonContainer, 0, 6);
        
        content.getChildren().addAll(titleContainer, formGrid);
        return content;
    }
    
    private void processAction(ActionEvent event) {
        Object source = event.getSource();
        
        if (source == submitButton) {
            if (validate()) {
                Properties p = new Properties();
                p.setProperty("StartTime", startTimeField.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
                p.setProperty("EndTime", endTimeField.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
                p.setProperty("CompanionName", companionNameField.getText());
                p.setProperty("CompanionHours", companionHoursField.getValue().toString());
                myModel.stateChangeRequest("SubmitShift", p);
            }
        }
        else if (source == backButton) {
            myModel.stateChangeRequest("Back", null);
        }
    }
    
    private boolean validate() {
        if (startTimeField.getTime() == null) {
            displayErrorMessage(myResources.getProperty("errInvalidStartTime"));
            startTimeField.requestFocus();
            return false;
        }
        if (endTimeField.getTime() == null) {
            displayErrorMessage(myResources.getProperty("errInvalidEndTime"));
            startTimeField.requestFocus();
            return false;
        }
        String companionName = companionNameField.getText();
        if (companionName == null || "".equals(companionName)) {
            displayErrorMessage(myResources.getProperty("errCompanionNameNull"));
            startTimeField.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
            
        } else if (key.equals("SelectedScout")) {
            IModel scout = (IModel)value;
            String name = String.format("%1$s %2$s",
                    scout.getState("FirstName"),
                    scout.getState("LastName"));
            scoutNameField.setText(name);
            memberIdField.setText((String) scout.getState("MemberID"));
        }
        companionNameField.requestFocus();
    }
}
