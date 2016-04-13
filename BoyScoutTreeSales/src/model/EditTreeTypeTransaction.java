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
import java.util.Vector;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author Andrew
 */
public class EditTreeTypeTransaction extends Transaction {
    String updateStatusMessage;
    private Vector<TreeType> treeTypes;
    private static final String myTableName = "Tree_Type";
    private Properties persistentState;
    
    public EditTreeTypeTransaction() 
    {
        super();  
        treeTypes = new Vector<TreeType>();
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
            case "getTreeType":
                return this;
            case "getAllTrees":
                allTreeTypes();
                return treeTypes;
            case "getTreeList":
                return treeTypes;
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
            case "Search":
                if (value != null)
                {
                    persistentState = (Properties) value;
                    findTreeTypeByBacodePrefix(persistentState.getProperty("BarCode"));
                }
            case "Retrieve All":
                   allTreeTypes();
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
    
    
      private void findTreeTypeByBacodePrefix(String barcodePrefix){
        treeTypes.clear();
                
         String query = String.format(
                "SELECT * FROM %s WHERE (BarcodePrefix = %s)", myTableName,barcodePrefix);
        Vector allDataRetrieved = getSelectQueryResult(query);
        for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextTreeTypeData = (Properties)allDataRetrieved.elementAt(cnt);
                TreeType next = new TreeType(nextTreeTypeData);
                if (next != null)
                {
                    treeTypes.add(next);
                }
            }
    }

    private void allTreeTypes(){
        treeTypes.clear();

        String query = "SELECT ID, TypeDescription, Cost, BarcodePrefix FROM " + myTableName;
        Vector allDataRetrieved = getSelectQueryResult(query);
        for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextTreeTypeData = (Properties)allDataRetrieved.elementAt(cnt);
                TreeType next = new TreeType(nextTreeTypeData);
                if(next != null)
                {
                    treeTypes.add(next);
                }
            }
    }
    
    
    protected Vector<TreeType> getTreeTypes()
    {
        return treeTypes;
    }
    
}


