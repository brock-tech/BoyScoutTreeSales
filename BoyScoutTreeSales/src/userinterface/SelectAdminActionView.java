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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author mike
 */
public class SelectAdminActionView extends BaseView {
    Button addScoutButton;
    Button editScoutButton;
    Button addTreeButton;
    Button editTreeButton;
    Button addTreeTypeButton;
    Button editTreeTypeButton;
    Button backButton;
    

    public SelectAdminActionView(IModel model) {
        super(model, "SelectAdminActionView");
    }

    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        VBox formContent = new VBox(10);
        formContent.setPrefWidth(600.0);
        formContent.setAlignment(Pos.CENTER);
        
        Text promptText = new Text(myResources.getProperty("promptText"));
        promptText.setTextAlignment(TextAlignment.CENTER);
        promptText.getStyleClass().add("information-text");
        formContent.getChildren().add(promptText);
        
        GridPane grid = new GridPane();
        grid.setPrefWidth(600.0);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        addScoutButton = new Button(myResources.getProperty("addScoutButtonText"));
        addScoutButton.setPrefSize(150.0, 25.0);
        addScoutButton.setOnAction(buttonHandler);
        grid.add(addScoutButton, 0, 0);
        
        editScoutButton = new Button(myResources.getProperty("editScoutButtonText"));
        editScoutButton.setPrefSize(150.0, 25.0);
        editScoutButton.setOnAction(buttonHandler);
        grid.add(editScoutButton, 0, 1);
        
        addTreeButton = new Button(myResources.getProperty("addTreeButtonText"));
        addTreeButton.setPrefSize(150.0, 25.0);
        addTreeButton.setOnAction(buttonHandler);
        grid.add(addTreeButton, 1, 0);
        
        editTreeButton = new Button(myResources.getProperty("editTreeButtonText"));
        editTreeButton.setPrefSize(150.0, 25.0);
        editTreeButton.setOnAction(buttonHandler);
        grid.add(editTreeButton, 1, 1);
        
        addTreeTypeButton = new Button(myResources.getProperty("addTreeTypeButtonText"));
        addTreeTypeButton.setPrefSize(150.0, 25.0);
        addTreeTypeButton.setOnAction(buttonHandler);
        grid.add(addTreeTypeButton, 2, 0);
        
        editTreeTypeButton = new Button(myResources.getProperty("editTreeTypeButtonText"));
        editTreeTypeButton.setPrefSize(150.0, 25.0);
        editTreeTypeButton.setOnAction(buttonHandler);
        grid.add(editTreeTypeButton, 2, 1);
        
        formContent.getChildren().add(grid);
        
        backButton = new Button(myResources.getProperty("backButtonText"));
        backButton.setOnAction(buttonHandler);
        formContent.getChildren().add(backButton);
        
        return formContent;
    }
    
    protected void processAction(Event evt) {
        Object sender = evt.getSource();
        
        if (sender.equals(backButton)) {
            myModel.stateChangeRequest("CancelTransaction", "");
        }
        else if (sender.equals(addScoutButton)) {
            myModel.stateChangeRequest("RegisterScout", "");
        }
        else if (sender.equals(editScoutButton)) {
            myModel.stateChangeRequest("EditScout", "");
        }
        else if (sender.equals(addTreeButton)) {
            myModel.stateChangeRequest("AddTree", "");
        }
        else if (sender.equals(editTreeButton)) {
            myModel.stateChangeRequest("EditTree", "");
        }
        else if (sender.equals(addTreeTypeButton)) {
            myModel.stateChangeRequest("AddTreeType", "");
        }
        else if (sender.equals(editTreeTypeButton)) {
            myModel.stateChangeRequest("EditTreeType", "");
        }
    }

    @Override
    public void updateState(String key, Object value) {
        
    }
}
