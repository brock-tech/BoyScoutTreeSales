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
import java.util.Properties;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

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
        dependencies.put("Submit", "TransactionError");
        dependencies.put("Done", "CancelTransaction");
        
        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
        
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddTreeTypeTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("AddTreeTypeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddTreeTypeTransactionView", currentScene);
        }
        
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
        try {
            String typeId = p.getProperty("barcodePrefix");
            
            TreeType oldTreeType = new TreeType(typeId);
            
            updateStatusMessage = "TreeType with Barcode Prefix "+typeId+" already exists.";
            transactionErrorMessage = updateStatusMessage;
            
        } catch (Exception exc) { 
            
            // Add new TreeType
            TreeType newTreeType = new TreeType(p); 
            newTreeType.update();
            updateStatusMessage = (String)newTreeType.getState("UpdateStatusMessage");
            transactionErrorMessage = updateStatusMessage;
        }
    }
}

