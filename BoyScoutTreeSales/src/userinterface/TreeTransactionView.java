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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import model.TreeTransaction;


/**
 *
 * @author PHONG
 */
public class TreeTransactionView extends BaseView {
    static final String DATE_FORMAT = "";
    
    protected TextField treeBarCode;
    protected ComboBox treeType;
    protected TextField salePrice;
    protected ComboBox status;
    protected TextField notes;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;

    public TreeTransactionView(IModel model) {
        super(model, "TreeTransactionView");
        
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TreeToDisplay", this);
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
        

        treeBarCode = new TextField();
        treeBarCode.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("TreeBarCode"), 
                treeBarCode);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 0);
        
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

        notes = new TextField();
        notes.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("Notes"),
                notes);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 3);
        
        ObservableList<String> options = FXCollections.observableArrayList(
            myResources.getProperty("Available"),
            myResources.getProperty("Unavailable")
        );
        
        status = new ComboBox(options);
        formItem = formItemBuilder.buildControl("Status:", status);
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 4);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        submitButton = new Button(myResources.getProperty("Submit"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);
        
        cancelButton = new Button(myResources.getProperty("Cancel"));
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
// Verify information in fields and submit
                Properties p = new Properties();
                                
                p.setProperty("isNew", "new");
                p.setProperty("BarCode", treeBarCode.getText());
                p.setProperty("TreeType", treeType.getValue().toString().substring(0, 
                getIntTreeType(treeType.getValue().toString())));
                p.setProperty("SalePrice", salePrice.getText());
                p.setProperty("Notes", notes.getText());
                p.setProperty("Status", status.getValue().toString());
                myModel.stateChangeRequest("Submit", p);
            }
        }
    }
    
    protected boolean validate(){
        String treeBar = treeBarCode.getText();
        String price = salePrice.getText();
        
        if ((treeBar == null) || treeBar.equals("") || isNumber(treeBar) == false) {
            displayErrorMessage(myResources.getProperty("NoBarCode"));
            treeBarCode.requestFocus();
            return false;
        }
        else if ((price == null) || price.equals("")) {
            displayErrorMessage(myResources.getProperty("NoSalePrice"));
            salePrice.requestFocus();
            return false;
        }
        return true;
    }
    
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
        else if (key.equals("TreeToDisplay")) {
            IModel selectedTree = (IModel)value;
            if (selectedTree != null) {
                treeBarCode.setText((String) selectedTree.getState("BarCode"));
                //treeType.setText((String) selectedTree.getState("MiddleName"));
                salePrice.setText((String) selectedTree.getState("SalePrice"));
                notes.setText((String) selectedTree.getState("Notes"));
                //status.setText((String) selectedTree.getState("DateOfBirth"));
            }
        }
    }
   
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
    
    protected boolean isNumber(String string) {
    try {
        Long.parseLong(string);
    } catch (Exception e) {
        return false;
    }
    return true;
}
}
