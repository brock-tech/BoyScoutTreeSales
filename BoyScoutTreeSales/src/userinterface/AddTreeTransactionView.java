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
import javafx.geometry.Insets;
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
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));

        barCodeField = new TextField();
        barCodeField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(myResources.getProperty("treeBarCode"), 
                barCodeField);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 0);
        
        /*
        getTreeTypeField();
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("TreeType"),
                treeType);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 1);
        
        salePrice = new TextField();
        salePrice.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("SalePrice"),
                salePrice);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 2);
        */

        notesField = new TextArea();
        notesField.setPrefRowCount(5);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("notes"),
                notesField);
        formItem.setPrefWidth(300);
        formGrid.add(formItem, 0, 1);
        
        /*
        ObservableList<String> options = FXCollections.observableArrayList(
            myResources.getProperty("Available"),
            myResources.getProperty("Unavailable")
        );
        
        statusBox = new ComboBox(options);
        formItem = formItemBuilder.buildControl("Status:", statusBox);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 4);
        */
        
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
        
        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        else {
            if (validate()){                
                Properties p = new Properties();
                String barCode = barCodeField.getText();
                p.setProperty("BarCode", barCode);
                
                String barCodePrefix = barCode.substring(0, 2);
                p.setProperty("TreeType", barCodePrefix);
                
                p.setProperty("Notes", notesField.getText());
                
                p.setProperty("Status", "Available");
                
                //LocalDateTime currentDate = LocalDateTime.now();
                //String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                //p.setProperty("DateStatusUpdated", dateLastUpdate);
                //p.setProperty("SalePrice", "0");
                
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
   
    /*
    protected void getTreeTypeField(){
        //TreeTransaction treeTransaction = (TreeTransaction)myModel.getState("getTreeTransaction");
        TreeTransaction treeTransaction = new TreeTransaction();
        Vector treeTypeList = (Vector)treeTransaction.getState("getTreeType");
        ArrayList newList = new ArrayList();
        String str = new String();
        
        for(int i = 0; i < treeTypeList.size(); i++){
            str = treeTypeList.get(i).toString().replace("{ID=","");
            str = str.replace(",",".");
            str = str.replace("TypeDescription=","");
            str = str.replace("}", "");
            newList.add(str);
        }
 
        List<String> optionList = newList;
        ObservableList<String> options = FXCollections.observableArrayList(optionList);
        treeType = new ComboBox(options);
    }
    
    protected int getIntTreeType(String str){
        for (int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '.'){
                return i;
            }
        }
        return 1;
    }
    
    protected void getData(){
    }
    
    protected String getProp(String oldstring){
        String str = new String();

        str = oldstring.replace("{BarCode=", "");
        str = oldstring.replace("}", "");
        return str;
    }
    */
}
