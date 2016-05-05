//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author PHONG
 */
public class AddTreeTransaction extends Transaction {
    String updateStatusMessage;
    String treeTypeId;
    
    public AddTreeTransaction(){
        super();
    }
    
     @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        dependencies.put("Submit", "TransactionError,UpdateStatusMessage");
        dependencies.put("Cancel", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
        myMessages = ResourceBundle.getBundle("model.i18n.TreeTransaction", myLocale);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddTreeTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("AddTreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddTreeTransactionView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        return currentScene;
    }

    @Override
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
            case "UpdateStatusMessage":
                return updateStatusMessage;
            case "TreeTypeId":
                return treeTypeId;
            default:
                return null;
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "Submit":
                processTransaction((Properties)value);
                break;
            case "TreeType":
                checkTreeType((String)value);
                break;
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    private void checkTreeType(String barcode){
        String CodePrefix = barcode.substring(0, 2);
        Properties p = new Properties();
        try{
            TreeType type = new TreeType(CodePrefix);
            p = (Properties)type.getState("getProperties");
            treeTypeId = p.getProperty("ID");
        }
        catch (InvalidPrimaryKeyException e){
            treeTypeId = null;
        }   
    }
    
    private void processTransaction(Properties p) {
        updateStatusMessage = "";
        transactionErrorMessage = "";
        
        //existing Tree
        try {
            String treeBarCode = p.getProperty("BarCode");
            
            Tree oldTree = new Tree(treeBarCode);
            updateStatusMessage = String.format(myMessages.getString("multipleTFoundMsg"), 
                       p.getProperty("BarCode"));
            transactionErrorMessage = updateStatusMessage;
            
        } catch (InvalidPrimaryKeyException exc) { 
            // Add new Tree
            Tree tree = new Tree(p);
            tree.insert();
            updateStatusMessage = String.format(myMessages.getString("insertSuccessMsg"), 
                       p.getProperty("BarCode"));
            transactionErrorMessage = updateStatusMessage;
        }
    }
}
