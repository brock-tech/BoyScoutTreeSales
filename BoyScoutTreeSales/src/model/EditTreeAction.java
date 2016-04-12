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
import java.util.Vector;
/**
 *
 * @author PHONG
 */
public class EditTreeAction extends Transaction {
    String updateStatusMessage;
    private Vector<Tree> trees;
    private static final String myTableName = "Tree";
    private Properties persistentState;
    
    public EditTreeAction(){
        super();
        
        trees = new Vector<Tree>();
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
        Scene currentScene = myViews.get("EditTreeActionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("EditTreeActionView", this);
            currentScene = new Scene(newView);
            myViews.put("EditTreeActionView", currentScene);
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
            case "getTree":
                return this;
            case "getAllTrees":
                findAllTrees();
                return trees;
            case "getTreeList":
                return trees;
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
            case "Search":
                if (value != null){
                    persistentState = (Properties) value;
                    findTreeByBarCode(persistentState.getProperty("BarCode"));
                }
                else
                    findAllTrees();
                break;
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    private void processTransaction(Properties p) {

    }
    
    private void findTreeByBarCode(String barcode){
        trees.clear();
        String query = "SELECT * FROM " + myTableName + " WHERE BarCode LIKE '%" + barcode + "%';";
        
        System.out.println(query);
        Vector allDataRetrieved = getSelectQueryResult(query);
        for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextTreeData = (Properties)allDataRetrieved.elementAt(cnt);
                Tree tree = new Tree(nextTreeData);
                if (tree != null)
                {
                    trees.add(tree);
                }
            }
    }

    private void findAllTrees(){
        trees.clear();

        String query = "SELECT BarCode, TreeType, SalePrice, CName, CPhoneNum, CEmail, " +
              "DateStatusUpdated, TimeStatusUpdated FROM " + myTableName;
        Vector allDataRetrieved = getSelectQueryResult(query);
        for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextTreeData = (Properties)allDataRetrieved.elementAt(cnt);
                Tree tree = new Tree(nextTreeData);
                if (tree != null)
                {
                    trees.add(tree);
                }
            }
    }
    
    private Vector<Tree> getTrees(){
        return trees;
    }
}
