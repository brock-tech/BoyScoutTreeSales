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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author mike
 */
public class CloseSessionTransactionView extends BaseView {    
    protected TimeEntry endTimeField;
    protected TextField endCashField;
    protected TextField totalCheckAmtField;
    protected TextArea notesField;
    
    protected Button submitButton;
    protected Button cancelButton;

    public CloseSessionTransactionView(IModel model) {
        super(model, "CloseSessionTransactionView");
    }
    
    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        IModel session = (IModel) myModel.getState("SessionToClose");
        
        VBox content = new VBox(10);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        
        Text promptText = new Text(myResources.getProperty("promptText"));
        promptText.setTextAlignment(TextAlignment.CENTER);
        promptText.getStyleClass().add("information-text");
        
        HBox promptContainer = new HBox();
        promptContainer.setAlignment(Pos.CENTER);
        promptContainer.getChildren().add(promptText);
        
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50d);
        col1.setHalignment(HPos.RIGHT);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50d);
        col2.setHalignment(HPos.LEFT);
        
        grid.getColumnConstraints().addAll(col1, col2);
        
        LocalDate startDate = LocalDate.parse(
                (String)session.getState("StartDate"),
                DateTimeFormatter.ISO_LOCAL_DATE);
        String dateFormat = myResources.getProperty("dateFormat");
        Label dateLabel = new Label(myResources.getProperty("startDateLabel"));
        Label dateValueLabel = new Label(startDate.format(
                DateTimeFormatter.ofPattern(dateFormat)));
        grid.add(dateLabel, 0, 0);
        grid.add(dateValueLabel, 1, 0);
        
        LocalTime startTime = LocalTime.parse(
                (String)session.getState("StartTime"),
                DateTimeFormatter.ISO_LOCAL_TIME);
        String timeFormat = myResources.getProperty("timeFormat");
        Label startTimeLabel = new Label(myResources.getProperty("startTimeLabel"));
        Label startTimeValueLabel = new Label(
                startTime.format(DateTimeFormatter.ofPattern(timeFormat)));
        grid.add(startTimeLabel, 0, 1);
        grid.add(startTimeValueLabel, 1, 1);
        
        String cashFormat = myResources.getProperty("currencyMsgFormat");
        Label startCashLabel = new Label(myResources.getProperty("startCashLabel"));
        Label startCashValueLabel = new Label(String.format(cashFormat,
                (String)session.getState("StartingCash")));
        grid.add(startCashLabel, 0, 2);
        grid.add(startCashValueLabel, 1, 2);
        
        Label endTimeLabel = new Label(myResources.getProperty("endTimeField"));
        grid.add(endTimeLabel, 0, 3);
        
        Label endCashLabel = new Label(myResources.getProperty("endCashField"));
        grid.add(endCashLabel, 0, 4);
        
        Label checkTotalLabel = new Label(myResources.getProperty("checkTotalField"));
        grid.add(checkTotalLabel, 0, 5);
        
        Node notesFormItem;
        try {
            endTimeField = (TimeEntry) Class.forName(
                    myResources.getProperty("timeEntryClass")).newInstance();
            endTimeField.setTime((String)session.getState("EndTime"));
            grid.add(endTimeField, 1, 3);
            
            endCashField = new TextField((String)session.getState("EndingCash"));
            endCashField.setPrefWidth(100);
            IFormItemStrategy currencyItemBuilder = (IFormItemStrategy) Class.forName(
                    myResources.getProperty("currencyFormItemClass")).newInstance();
            Pane formItem = currencyItemBuilder.buildControl(null, endCashField);
            grid.add(formItem, 1, 4);
            
            totalCheckAmtField = new TextField((String)session.getState("TotalCheckTransactionsAmount"));
            totalCheckAmtField.setPrefWidth(100);
            formItem = currencyItemBuilder.buildControl(null, totalCheckAmtField);
            grid.add(formItem, 1, 5);
            
            IFormItemStrategy formItemBuilder = (IFormItemStrategy)Class.forName(
                    "userinterface.TopAlignFormItem"
            ).newInstance();
            
            notesField = new TextArea((String)session.getState("Notes"));
            notesField.setPrefRowCount(4);
            notesField.setPrefWidth(500);
            notesFormItem = formItemBuilder.buildControl(
                    myResources.getProperty("notesField"),
                    notesField);
        }
        catch (Exception cnfe) {
            System.err.printf("Class load error: " + cnfe.getMessage());
            return content;
        }
        
        submitButton = new Button(myResources.getProperty("submitButton"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(submitButton, cancelButton);
        
        content.setPadding(new Insets(15));
        content.getChildren().addAll(promptContainer, grid, notesFormItem, buttonContainer);
        
        return content;
    }
    
    private void processAction(ActionEvent event) {
        Object source = event.getSource();
        if (source == submitButton) {
            if (validate()) {
                Properties p = new Properties();

                p.setProperty("EndTime", endTimeField.getTime());
                p.setProperty("EndingCash", endCashField.getText());
                p.setProperty("TotalCheckTransactionsAmount", totalCheckAmtField.getText());
                
                if (notesField.getText() == null || notesField.getText().isEmpty())
                    p.setProperty("Notes", "<empty>");
                else 
                    p.setProperty("Notes", notesField.getText());

                myModel.stateChangeRequest("Confirm", p);
            }
        }
        else if (source == cancelButton) {
            myModel.stateChangeRequest("Cancel", null);
        }
    }
    
    private boolean validate() {
        if (endTimeField.getTime() == null) {
            displayErrorMessage(myResources.getProperty("errEndTimeInvalid"));
            endTimeField.requestFocus();
            return false;
        }
        
        String value = endCashField.getText();
        if (!value.matches(myResources.getProperty("currencyPattern"))) {
            displayErrorMessage(myResources.getProperty("errEndTimeInvalid"));
            endCashField.requestFocus();
            return false;
        }
        
        value = totalCheckAmtField.getText();
        if (!value.matches(myResources.getProperty("currencyPattern"))) {
            displayErrorMessage(myResources.getProperty("errEndTimeInvalid"));
            totalCheckAmtField.requestFocus();
            return false;
        }
        
        return true;
    }   
    
    @Override
    public void updateState(String key, Object value) {
        
    }
}
