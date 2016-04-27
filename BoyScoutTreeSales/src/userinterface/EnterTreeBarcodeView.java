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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author mike
 */
public class EnterTreeBarcodeView extends BaseView {
    TextField barcodeField;
    Button cancelButton;

    public EnterTreeBarcodeView(IModel model) {
        super(model, "EnterTreeBarcodeView");
        
        myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    protected Node createContent() {
        VBox content = new VBox(25);
        content.setPrefWidth(400);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("table");
        
        Text welcomeText = new Text(myResources.getProperty("title"));
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.getStyleClass().add("information-text");
        content.getChildren().add(welcomeText);
        
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        barcodeField = new TextField();
        barcodeField.setOnAction(submitHandler);
        IFormItemStrategy formItemBuilder = FormItemFactory.getFormItem("TopAlignFormItem");
        Pane formItem = formItemBuilder.buildControl(
                myResources.getProperty("enterBarcode"), 
                barcodeField);
        formItem.setPrefWidth(400);
        
        cancelButton = new Button(myResources.getProperty("cancel"));
        cancelButton.setPrefWidth(100);
        cancelButton.setOnAction(submitHandler);
        
        content.getChildren().addAll(formItem, cancelButton);
        
        return content;
    }
    
    protected void processAction(Event event) {
        Object sender = event.getSource();
        if (sender == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        else if (sender == barcodeField) {
            if (validate()) {
                myModel.stateChangeRequest("SubmitBarcode", barcodeField.getText());
            } 
        }
    }
    
    private boolean validate() {
        return true;
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)value);
        }
    }
    
}
