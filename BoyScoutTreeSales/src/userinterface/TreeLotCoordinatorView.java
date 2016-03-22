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
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        GridPane grid = new GridPane();
        grid.setPrefWidth(DEFAULT_WIDTH);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        Text subTitle = new Text(myResources.getProperty("subTitle"));
        subTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(subTitle, 0, 0);
        
        sellTreeButton = new Button(myResources.getProperty("sellTreeButtonText"));
        sellTreeButton.setPrefSize(200.0, 30.0);
        sellTreeButton.setOnAction(buttonHandler);
        grid.add(sellTreeButton, 0, 1);
        GridPane.setHalignment(sellTreeButton, HPos.CENTER);
        
        shiftButton = new Button();
        shiftButton.setPrefSize(200.0, 30.0);
        shiftButton.setOnAction(buttonHandler);
        grid.add(shiftButton, 0, 2);
        GridPane.setHalignment(shiftButton, HPos.CENTER);
        
        manageButton = new Button(myResources.getProperty("manageButtonText"));
        manageButton.setPrefSize(200.0, 30.0);
        manageButton.setOnAction(buttonHandler);
        grid.add(manageButton, 0, 3);
        GridPane.setHalignment(manageButton, HPos.CENTER);
        
        
        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        exitButton = new Button(myResources.getProperty("exitButtonText"));
        exitButton.setOnAction(buttonHandler);
        btnContainer.getChildren().add(exitButton);
        grid.add(btnContainer, 0, 5);
        
        return grid;
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
