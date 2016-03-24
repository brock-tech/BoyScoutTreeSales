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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 */
//==============================================================
public class TreeLotCoordinatorView extends BaseView {
    private Button sellTreeButton;
    private Button shiftButton;
    private Button manageButton;
    private Button exitButton;

    /** */
    //----------------------------------------------------------
    public TreeLotCoordinatorView(IModel model) {
        super(model, "TreeLotCoordinatorView");
        
        myModel.subscribe("TransactionError", this);
        myModel.subscribe("SessionStatus", this);
        
        // Get session status to determine the shift buttton text
        updateState("SessionStatus", myModel.getState("SessionStatus"));
    }
    
    /** */
    //----------------------------------------------------------
    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        VBox formContent = new VBox(10);
        formContent.setPrefWidth(DEFAULT_WIDTH);
        formContent.setAlignment(Pos.CENTER);
        
        Text welcomeText = new Text(myResources.getProperty("welcomeMessage"));
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        formContent.getChildren().add(welcomeText);
        
        sellTreeButton = new Button(myResources.getProperty("sellTreeButtonText"));
        sellTreeButton.setPrefSize(200.0, 30.0);
        sellTreeButton.setOnAction(buttonHandler);
        formContent.getChildren().add(sellTreeButton);
        
        shiftButton = new Button();
        shiftButton.setPrefSize(200.0, 30.0);
        shiftButton.setOnAction(buttonHandler);
        formContent.getChildren().add(shiftButton);
        
        manageButton = new Button(myResources.getProperty("manageButtonText"));
        manageButton.setPrefSize(200.0, 30.0);
        manageButton.setOnAction(buttonHandler);
        formContent.getChildren().add(manageButton);
        
        exitButton = new Button(myResources.getProperty("exitButtonText"));
        exitButton.setOnAction(buttonHandler);
        formContent.getChildren().add(exitButton);
        
        return formContent;
    }
    
    /**
     * 
     */
    //----------------------------------------------------------
    private void processAction(Event evt) {
        Object eventSource = evt.getSource();
        
        if (eventSource.equals(exitButton)) {
            myModel.stateChangeRequest("Exit", null);
        }
        else if (eventSource.equals(sellTreeButton)) {
            myModel.stateChangeRequest("SellTree", null);
        }
        else if (eventSource.equals(shiftButton)) {
            myModel.stateChangeRequest("ManageShift", null);
        }
        else if (eventSource.equals(manageButton)) {
            myModel.stateChangeRequest("Administration", null);
        }
    }

    /** */
    //----------------------------------------------------------
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("TransactionError")) {
            statusLog.displayErrorMessage((String)value);
        }
        else if (key.equals("SessionStatus")) {
            // Set button label based on the current session status
            if (value.equals("Open")) {
                shiftButton.setText(myResources.getProperty("shiftButtonTextClose"));
            }
            else {
                shiftButton.setText(myResources.getProperty("shiftButtonTextOpen"));
            }
        }
    }
}
