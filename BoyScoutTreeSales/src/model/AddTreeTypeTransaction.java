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

import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;

/**
 *
 * @author Andrew
 */
public class AddTreeTypeTransaction extends Transaction {
    String updateStatusMessage;
    
    public AddTreeTypeTransaction() {
        super();
    }

    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        dependencies.put("Submit", "TransactionError, UpdateStatusMessage");
        dependencies.put("Cancel", "CancelTransaction");
        
        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() 
    {
             myMessages = ResourceBundle.getBundle("model.i18n.TreeType", myLocale);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddTreeTypeTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("AddTreeTypeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddTreeTypeTransactionView", currentScene);
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
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    private void processTransaction(Properties p) {
             updateStatusMessage = "";
            transactionErrorMessage = "";
           MessageFormat formatter = new MessageFormat("", myLocale);
           try 
           {
                String barcodePrefix = p.getProperty("BarcodePrefix");
                
                TreeType oldTreeType = new TreeType(barcodePrefix);
                barcodePrefix = (String)oldTreeType.getState("BarcodePrefix");
                
               updateStatusMessage = String.format(myMessages.getString("multipleTTFoundMsg"), 
                       p.getProperty("BarcodePrefix"));
                transactionErrorMessage = updateStatusMessage;
           } 
           catch (InvalidPrimaryKeyException exc) 
           { 
                // Add new TreeType
                String barcodePrefix = p.getProperty("BarcodePrefix");
                
                TreeType newTreeType = new TreeType(p);
               // System.out.println("try here " + barcodePrefix);
                
                newTreeType.update();
            updateStatusMessage = String.format(myMessages.getString("insertSuccessMsg"), 
             newTreeType.getState("ID"),barcodePrefix) ;
                transactionErrorMessage = updateStatusMessage;
            }
           System.out.println(transactionErrorMessage);
    }
}

