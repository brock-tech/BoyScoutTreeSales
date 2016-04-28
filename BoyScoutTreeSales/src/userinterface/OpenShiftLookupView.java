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
import java.util.Optional;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import static userinterface.BaseView.DEFAULT_WIDTH;

/**
 *
 * @author mike
 */
public class OpenShiftLookupView extends BaseView {
    
    
    protected ScoutLookupTable lookupTable;
    protected ListView<ScoutListModel> scoutsList;
    protected Button selectButton;
    protected Button doneButton;

    public OpenShiftLookupView(IModel model) {
        super(model, "OpenShiftLookupView");
        
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("SearchResults", this);
        myModel.subscribe("ScoutsOnShift", this);
    }

    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> actionHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        VBox content = new VBox(10);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5, 20, 5, 20));
        
        Text promptText = new Text(myResources.getProperty("promptText"));
        promptText.setTextAlignment(TextAlignment.CENTER);
        promptText.getStyleClass().add("information-text");
        
        HBox promptContainer = new HBox();
        promptContainer.setPrefWidth(DEFAULT_WIDTH);
        promptContainer.setAlignment(Pos.CENTER);
        promptContainer.getChildren().add(promptText);
        
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
        
        scoutsList = new ListView<>();
        scoutsList.setPrefSize(DEFAULT_WIDTH, 100);
        
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        
        selectButton = new Button(myResources.getProperty("select"));
        selectButton.setOnAction(actionHandler);
        selectButton.setPrefWidth(200);
        buttonContainer.getChildren().add(selectButton);
        
        doneButton = new Button(myResources.getProperty("done"));
        doneButton.setOnAction(actionHandler);
        doneButton.setPrefWidth(200);
        buttonContainer.getChildren().add(doneButton);
        
        content.getChildren().addAll(promptContainer, lookupTable, scoutsList, buttonContainer);
        
        return content;
    }
    
    private void processAction(ActionEvent event) {
        Object source = event.getSource();
        if (source == lookupTable) {
            myModel.stateChangeRequest("SearchScouts", lookupTable.getSearchTerms());
        }
        else if (source == selectButton) {
            ScoutTableModel itemSelected = lookupTable.getSelectedItem();
            if (itemSelected == null) {
                displayErrorMessage(myResources.getProperty("errNoScoutSelected"));
            }
            else {
                myModel.stateChangeRequest("SelectScout", itemSelected.getId());
            }
        }
        else if (source == doneButton) {
            myModel.stateChangeRequest("Done", null);
        }
    }
    
    private void getListEntryModelViews(Vector listData) {
        ObservableList<ScoutListModel> obsListData = FXCollections.observableArrayList();
        try {
            Enumeration entries = listData.elements();
            
            while (entries.hasMoreElements()) {
                IModel nextScout = (IModel)entries.nextElement();
                
                ScoutListModel scoutEntry = new ScoutListModel(nextScout);
                obsListData.add(scoutEntry);
            }
            scoutsList.setItems(obsListData);
        } catch (Exception e) {
            // TODO: Handle exceptions
        }
    }

    @Override
    public void updateState(String key, Object value) {
        switch (key) {
            case "UpdateStatusMessage":
                displayMessage((String)myModel.getState("UpdateStatusMessage"));
                break;
                
            case "SearchResults":
                    Vector scouts = (Vector)value;
                    lookupTable.setItems(scouts);
                    break;
                    
            case "ScoutsOnShift":
                    scouts = (Vector)value;
                    getListEntryModelViews(scouts);
                    break;
                    
            default:
                break;
        }
    }
    
}
