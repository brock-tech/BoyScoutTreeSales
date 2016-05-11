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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 *
 * @author PHONG
 */
public class AddTreeTransactionView extends BaseView {
    protected TextField barCodeField;
//    protected ComboBox treeType;
//    protected TextField salePrice;
//    protected ComboBox statusBox;
    protected TextArea notesField;
    protected Button submitButton;
    protected Button cancelButton;

    public AddTreeTransactionView(IModel model) {
        super(model, "AddTreeTransactionView");
        
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
        
        
        IFormItemStrategy formItemBuilder = FormItemFactory.getFormItem("TopAlignFormItem");
        Pane formItem;
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        
        Text title = new Text(myResources.getProperty("promptTitle"));
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("information-text");
        content.getChildren().add(title);
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        //formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        formGrid.getStyleClass().addAll("pane1","grid");

        barCodeField = new TextField();
        barCodeField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(myResources.getProperty("barCodeField"), 
                barCodeField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 0);

        notesField = new TextArea();
        notesField.setPrefRowCount(5);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("notesField"),
                notesField);
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1);
        
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        
        submitButton = new Button(myResources.getProperty("submitButton"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);
        
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);
        
        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        
        return content;
    }
        
    private void processAction(Event event) {
        clearErrorMessage();
        
        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        else {            
            if (validate()){                
                Properties p = new Properties();
                String barCode = barCodeField.getText();
                p.setProperty("BarCode", barCode);
                
                //p.setProperty("TreeType", (String)myModel.getState("TreeTypeId"));                
                p.setProperty("Notes", notesField.getText());                
                p.setProperty("Status", "Available");
                LocalDateTime currentDate = LocalDateTime.now();
                String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                p.setProperty("DateStatusUpdated", dateLastUpdate);
                myModel.stateChangeRequest("Submit", p);
            }
        }
    }
    
    protected boolean validate(){
        String treeBar = barCodeField.getText();
        
        if ((treeBar == null) || "".equals(treeBar)) {
            displayErrorMessage(myResources.getProperty("errBarCodeMissing"));
            barCodeField.requestFocus();
            return false;
        }
        if (!treeBar.matches("[0-9]+")) {
            displayErrorMessage(myResources.getProperty("errBarCodeInvalid"));
            barCodeField.requestFocus();
            return false;
        }
        /*
        myModel.stateChangeRequest("TreeType", treeBar);
        if (myModel.getState("TreeTypeId") == null){
            displayErrorMessage(myResources.getProperty("errBarCodePrefix"));
            barCodeField.requestFocus();
            return false;
        }
        */
        String notes = notesField.getText();
        if ((notes == null) || "".equals(notes)) {
            notesField.setText("<empty>");
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
