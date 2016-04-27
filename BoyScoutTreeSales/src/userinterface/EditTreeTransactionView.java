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
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import model.Tree;

/**
 *
 * @author PHONG
 */
public class EditTreeTransactionView extends BaseView {
    protected TextField barCodeField;
    protected TextArea notesField;
    protected ComboBox statusBox;
    protected Button submitButton;
    protected Button cancelButton;

    public EditTreeTransactionView(IModel model) {
        super(model, "EditTreeTransactionView");
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TreeToDisplay", this);
        
        notesField.requestFocus();
    }
    
    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        IFormItemStrategy formItemBuilder = FormItemFactory.getFormItem("TopAlignFormItem");
        Pane formItem;
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        barCodeField = new TextField();
        barCodeField.setEditable(false);
        formItem = formItemBuilder.buildControl(myResources.getProperty("treeBarcode"),
                barCodeField);
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 0);
        
        notesField = new TextArea();
        notesField.setPrefRowCount(5);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("notes"),
                notesField);
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1);
        
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Available",
                "Damaged",
                "Sold"
        );
        statusBox = new ComboBox(statusOptions);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("statusBox"),
                statusBox
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 2);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        submitButton = new Button(myResources.getProperty("submit"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);
        
        cancelButton = new Button(myResources.getProperty("cancel"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);

        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        return content;
    }
        
    private void processAction(Event event) {
        clearErrorMessage();
        Object source = event.getSource();
        
        if (source == cancelButton) {
            myModel.stateChangeRequest("Cancel", null);
        }
        else if (source == submitButton){
            if (validate()) {
                Properties p = new Properties();
                p.setProperty("Notes", notesField.getText());
                p.setProperty("Status", (String)statusBox.getValue());
                
                myModel.stateChangeRequest("UpdateTree", p);
            }
        }
    }
    
    private boolean validate() {
        return true;
    }
    
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
        else if (key.equals("TreeToDisplay")) {
            Tree tree = (Tree)value;
            barCodeField.setText((String)tree.getState("BarCode"));
            notesField.setText((String)tree.getState("Notes"));
            statusBox.setValue((String)tree.getState("Status"));
        }
    }
}
