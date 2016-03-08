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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 */
public class TreeLotCoordinatorView extends View {
    
    private Button sellTreeButton;
    private Button openShiftButton;
    private Button closeShiftButton;
    private Button addTreeButton;
    private Button updateTreeButton;
    private Button removeTreeButton;
    private Button addTreeTypeButton;
    private Button updateTreeTypeButton;
    private Button addScoutButton;
    private Button updateScoutButton;
    private Button removeScoutButton;
    private Button doneButton;
    
    private MessageView statusLog;

    public TreeLotCoordinatorView(IModel model) {
        super(model, "TreeLotCoordinator");
        
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setPrefSize(600.0, 350.0);
        
        container.getChildren().add(this.createTitle());
        container.getChildren().add(this.createFormContents());
        
        getChildren().add(container);
        
        myModel.subscribe("TransactionError", this);
    }
    
    private Node createTitle() {
        HBox titleContainer = new HBox();
        titleContainer.setPrefSize(600.0, 15.0);
        titleContainer.setAlignment(Pos.CENTER);
        
        Text titleText = new Text("Boy Scout Christmas Tree Sales");
        titleText.setFont(Font.font("SansSerif Bold", 18.0));
        titleText.setFill(Color.GREEN);
        titleText.setTextAlignment(TextAlignment.CENTER);
        
        titleContainer.getChildren().add(titleText);
        return titleContainer;
    }
    
    private GridPane createFormContents() {
        GridPane grid = new GridPane();
        grid.setPrefSize(600.0, 300.0);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        grid.getColumnConstraints().add(new ColumnConstraints(150.0, 160.0, Double.MAX_VALUE));
        grid.getColumnConstraints().add(new ColumnConstraints(10.0, 130.0, Double.MAX_VALUE));
        grid.getColumnConstraints().add(new ColumnConstraints(140.0, 160.0, Double.MAX_VALUE));
        grid.getColumnConstraints().add(new ColumnConstraints(140.0, 160.0, Double.MAX_VALUE));
        
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        sellTreeButton = new Button("Sell Tree");
        sellTreeButton.setPrefSize(150.0, 30.0);
        sellTreeButton.setOnAction(buttonHandler);
        grid.add(sellTreeButton, 0, 0);
        
        openShiftButton = new Button("Open Shift");
        openShiftButton.setPrefSize(100.0, 30.0);
        openShiftButton.setOnAction(buttonHandler);
        grid.add(openShiftButton, 0, 2);
        
        closeShiftButton = new Button("Close Shift");
        closeShiftButton.setPrefSize(100.0, 30.0);
        closeShiftButton.setOnAction(buttonHandler);
        grid.add(closeShiftButton, 0, 3);
        
        
        
        HBox btnContainer = new HBox(10);
        btnContainer.setPrefWidth(600);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        doneButton = new Button("Done");
        doneButton.setOnAction(buttonHandler);
        btnContainer.getChildren().add(doneButton);
        grid.add(btnContainer, 0, 5, 4, 1);
        
        return grid;
    }
    
    private Node createStatusLog() {
        statusLog = new MessageView("");
        return statusLog;
    }
    
    private void processAction(Event evt) {
        Object eventSource = evt.getSource();
        
        if (eventSource.equals(doneButton)) {
            myModel.stateChangeRequest("Exit", null);
        }
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("TransactionError")) {
            statusLog.displayErrorMessage((String)value);
        }
    }
    
}
