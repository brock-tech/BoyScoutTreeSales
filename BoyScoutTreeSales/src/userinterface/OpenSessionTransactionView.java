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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
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
public class OpenSessionTransactionView extends BaseView {    
    protected DatePicker dateField;
    protected TimeEntry startTimeField;
    protected TextField startCashField;
    protected TextArea notesField;
    //protected ListView<ScoutListModel> scoutsList;
    protected Button submitButton;
    protected Button cancelButton;

    public OpenSessionTransactionView(IModel model) {
        super(model, "OpenSessionTransactionView");
        
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
        
        HBox titleContainer = new HBox(10);
        titleContainer.setPrefSize(DEFAULT_WIDTH, 40.0);
        titleContainer.setAlignment(Pos.CENTER);
        
        Text titleText = new Text(myResources.getProperty("titleText"));
        titleText.setTextAlignment(TextAlignment.CENTER);
        
        titleContainer.getChildren().add(titleText);
        content.getChildren().add(titleContainer);
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        dateField = new DatePicker(currentDateTime.toLocalDate());
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("selectDate"),
                dateField
        );
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 0);
        
        try {
            startTimeField = (TimeEntry) Class.forName(
                    myResources.getProperty("timeEntry")).newInstance();
        }
        catch (Exception cnfe) {
            System.err.printf("Class load error: " + cnfe.getMessage());
            return content;
        }
        startTimeField.setTime(currentDateTime.toLocalTime());
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("enterStartTime"),
                startTimeField
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1);
        
        HBox startCashContainer = new HBox(5);
        startCashContainer.setAlignment(Pos.CENTER_LEFT);
        Text cashSymbol = new Text(myResources.getProperty("currency"));
        startCashField = new TextField();
        startCashField.setPrefWidth(100);
        startCashContainer.getChildren().addAll(cashSymbol, startCashField);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("enterStartCash"),
                startCashContainer
        );
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 2);
        
        notesField = new TextArea();
        notesField.setPrefRowCount(4);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("notes"),
                notesField);
        formItem.setPrefWidth(500);
        formGrid.add(formItem, 0, 4);
        
        submitButton = new Button(myResources.getProperty("submit"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        
        cancelButton = new Button(myResources.getProperty("cancel"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(submitButton, cancelButton);
        formGrid.add(buttonContainer, 0, 5);
        
        content.getChildren().addAll(formGrid, buttonContainer);
        
        return content;
    }
    
    private void processAction(Event event) {
        Object source = event.getSource();
        
        if (source == submitButton) {
            if (validate()) {
                Properties p = new Properties();
                p.setProperty("StartDate", dateField.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                p.setProperty("StartTime", startTimeField.getTime().format(
                        DateTimeFormatter.ofPattern("HH:mm")));
                p.setProperty("EndTime", "<empty>");
                p.setProperty("StartingCash", startCashField.getText());
                p.setProperty("EndingCash", "<empty>");
                p.setProperty("TotalCheckTransactionsAmount", "<empty>");
                p.setProperty("Notes", notesField.getText());
                myModel.stateChangeRequest("SubmitSession", p);
            }
        }
        else if (source == cancelButton) {
            myModel.stateChangeRequest("Done", null);
        }
    }
    
    private boolean validate() {
        if (startTimeField.getTime() == null) {
            displayErrorMessage(myResources.getProperty("errTimeInvalid"));
            startTimeField.requestFocus();
            return false;
        }
        
        startCashField.setText(startCashField.getText().trim());
        String cashInput = startCashField.getText();
        if (cashInput == null || cashInput.equals("")) {
            displayErrorMessage(myResources.getProperty("errCashNull"));
            startCashField.requestFocus();
            return false;
        }
        if (!cashInput.matches(myResources.getProperty("cashFormat"))) {
            displayErrorMessage(myResources.getProperty("errCashInvalid"));
            startCashField.requestFocus();
            return false;
        }
        return true;
    }
    

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)value);
        }
    }
    
}
