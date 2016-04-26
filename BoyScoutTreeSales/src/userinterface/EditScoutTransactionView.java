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
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Scout;

/**
 *
 * @author mike
 */
public class EditScoutTransactionView extends BaseView {
    //protected TextField firstNameSearchField;
    //protected TextField lastNameSearchField;    
    protected Button editButton;
    protected Button removeButton;
    protected Button cancelButton;
    //protected TableView<ScoutTableModel> tableOfScouts;
    protected ScoutLookupTable lookupTable;
    
    public EditScoutTransactionView(IModel model) {
        super(model, "EditScoutTransactionView");
        
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("Scouts", this);
        myModel.subscribe("ClearList", this);
    }

    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> actionHandler = new EventHandler<ActionEvent>() {
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
                    "userinterface.TopAlignFormItem").newInstance();
        }
        catch (Exception cnfe) {
            System.err.printf("Form Item Strategy error: " + cnfe.getMessage());
            return content;
        }
        
        lookupTable = new ScoutLookupTable();
        lookupTable.setOnAction(actionHandler);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        editButton = new Button(myResources.getProperty("editButton"));
        editButton.setOnAction(actionHandler);
        editButton.setPrefWidth(100);
        buttonContainer.getChildren().add(editButton);
        
        removeButton = new Button(myResources.getProperty("removeButton"));
        removeButton.setOnAction(actionHandler);
        removeButton.setPrefWidth(100);
        buttonContainer.getChildren().add(removeButton);
        
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(actionHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);
        
        content.getChildren().addAll(lookupTable, buttonContainer);
        
        return content;
    }
    
    protected void processAction(Event event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            myModel.stateChangeRequest("Back", null);
        }
        else if (source == lookupTable) {
            myModel.stateChangeRequest("SearchScouts", lookupTable.getSearchTerms());
        }
        else if (source == removeButton) {
            ScoutTableModel itemSelected = lookupTable.getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoScoutSelected"));
            }
            else {
                myModel.stateChangeRequest("RemoveScout", itemSelected.getId());
            }
        }
        else if (source == editButton) {
            ScoutTableModel itemSelected = lookupTable.getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoScoutSelected"));
            }
            else {
                myModel.stateChangeRequest("EditScout", itemSelected.getId());
            }
        }
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
            
        } else if (key.equals("Scouts")) {
            Vector scouts = (Vector)value;
            lookupTable.setItems(scouts);
        }
    }
    
}
