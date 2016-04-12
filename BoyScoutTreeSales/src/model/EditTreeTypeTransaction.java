//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
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
 * @author Andrew
 */
public class EditTreeTypeTransaction extends Transaction {
    String updateStatusMessage;
    
    public EditTreeTypeTransaction() {
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
             myMessages = ResourceBundle.getBundle("model.i18n.EditTreeTypeTransaction", myLocale);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("EditTreeTypeTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("EditTreeTypeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("EditTreeTypeTransactionView", currentScene);
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
           MessageFormat formatter = new MessageFormat("", myLocale);
           try 
           {
                String barcodePrefix = p.getProperty("barcodePrefix");

                TreeType searchedTreeType = new TreeType(barcodePrefix);

                formatter.applyPattern("TTNotFound");
                updateStatusMessage = formatter.format(new Object[] { barcodePrefix });
                transactionErrorMessage = updateStatusMessage;
           } 
           catch (Exception exc) 
           { 
                // Add new TreeType
                String barcodePrefix = p.getProperty("barcodePrefix");
                TreeType searchedTreeType = new TreeType(p); 
                searchedTreeType.getTableListView();

                /*formatter.applyPattern("insertSuccessMsg");
                updateStatusMessage = formatter.format(new Object[] { barcodePrefix });
                transactionErrorMessage = updateStatusMessage;*/
            }
    }
}


