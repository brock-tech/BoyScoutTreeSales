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
 * @author PHONG
 */
public class AddTreeTransaction extends Transaction {
    String updateStatusMessage;
    
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
        
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddTreeTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("AddTreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddTreeTransactionView", currentScene);
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
        updateStatusMessage = "";
        transactionErrorMessage = "";
        
        try {
            String troopId = p.getProperty("TroopID");
            
            Scout oldScout = new Scout(troopId);
            
            updateStatusMessage = "Scout with Troop ID "+troopId+" already exists.";
            transactionErrorMessage = updateStatusMessage;
            
        } catch (InvalidPrimaryKeyException exc) { 
            
            // Add new Scout
            Scout scout = new Scout(p); 
            scout.update();
            updateStatusMessage = (String)scout.getState("UpdateStatusMessage");
            transactionErrorMessage = updateStatusMessage;
        }
    }
}
