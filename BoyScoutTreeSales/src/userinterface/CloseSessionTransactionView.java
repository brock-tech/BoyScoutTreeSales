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
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
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
import model.Sale;

/**
 *
 * @author mike
 */
public class CloseSessionTransactionView extends BaseView {
    protected TextField EndTime;
    protected TextField TotalCheckTransactionAmount;
    protected TextField EndingCash;
    protected Button CloseButton;
    protected Button cancelButton;

    public CloseSessionTransactionView(IModel model) {
        super(model, "CloseShiftTransactionView");
        
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
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
                
        TotalCheckTransactionAmount = new TextField();
        TotalCheckTransactionAmount.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("totalCheckTransactionAmount"),
                TotalCheckTransactionAmount
        );
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 0);
        
        EndingCash = new TextField();
        EndingCash.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("endCash"),
                EndingCash
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 1);

        LocalDateTime currentDateTime = LocalDateTime.now();
        
        EndTime = new TextField(currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        EndTime.setOnAction(submitHandler);

        formItem = formItemBuilder.buildControl(
                myResources.getProperty("endTime"),
                EndTime
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 2);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        CloseButton = new Button(myResources.getProperty("closeButton"));
        CloseButton.setOnAction(submitHandler);
        CloseButton.setPrefWidth(100);
        buttonContainer.getChildren().add(CloseButton);
                
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);
        
        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        
        return content;
    }
    
    private void processAction(Event event) {
        Object source = event.getSource();
        Properties props = new Properties();
        
        clearErrorMessage();
        props.setProperty("SessionId", "1");
        if (source == CloseButton) {
            myModel.stateChangeRequest("searchTransactions", props);
            float check = (float)myModel.getState("checkAmount");
            float cash = (float)myModel.getState("cashAmount");
            TotalCheckTransactionAmount.setText(Float.toString(check));
            EndingCash.setText(Float.toString(cash));
        }
        
        else if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
    } 

    @Override
    public void updateState(String key, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
