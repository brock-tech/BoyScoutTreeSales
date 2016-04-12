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
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class RegisterScoutTransaction extends Transaction {
    String updateStatusMessage;
    
    public RegisterScoutTransaction() {
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
        myMessages = ResourceBundle.getBundle("model.i18n.RegisterScoutTransaction", myLocale);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("RegisterScoutTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("RegisterScoutTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("RegisterScoutTransactionView", currentScene);
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
        
        try {
            String troopId = p.getProperty("TroopID");
            
            Scout oldScout = new Scout(troopId);
            troopId = (String)oldScout.getState("TroopID");
            
            MessageFormat formatter = new MessageFormat(
                    myMessages.getString("scoutAlreadyExistsMsg"),
                    myLocale
            );
            
            updateStatusMessage = formatter.format(new Object[] { troopId });
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
