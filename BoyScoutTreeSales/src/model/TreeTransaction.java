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
import java.util.Enumeration;
import java.util.Properties;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;
import java.util.Vector;

/**
 *
 * @author PHONG
 */
public class TreeTransaction extends Transaction {
    String updateStatusMessage;
    Vector treeType;
    Vector data;
    Properties persistentState;
    
    public TreeTransaction(){
        super();
        
        persistentState = null;
    }
 
    public TreeTransaction(Properties props){
        super();
        setDependencies();
        
        persistentState = new Properties();

        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
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
        Scene currentScene = myViews.get("TreeTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("TreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("TreeTransactionView", currentScene);
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
            case "getTreeTransaction":
                return this;
            case "getTreeType":
                loadTreeType();
                return treeType;
            case "getProperties":
                getProperties();
                return persistentState;
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
        //existing Tree
        try {
            String treeBarCode = p.getProperty("BarCode");
            
            Tree oldTree = new Tree(treeBarCode);
            
            updateStatusMessage = "Tree with Bar Code "+treeBarCode+" already exists.";
            transactionErrorMessage = updateStatusMessage;
            
        } catch (InvalidPrimaryKeyException exc) { 
            // Add new Tree
            Tree tree = new Tree(p);
            tree.update();
            updateStatusMessage = (String)tree.getState("UpdateStatusMessage");
            transactionErrorMessage = updateStatusMessage;
        }
    }
    
    private void loadTreeType(){
        String query = "SELECT Id, TypeDescription FROM Tree_Type";
        treeType = getSelectQueryResult(query);
    }
    
    private Properties getProperties(){
        return persistentState;
    }
}
