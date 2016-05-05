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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author PHONG
 */
public class EditTreeTransaction extends Transaction {
    protected String updateStatusMessage;
    protected Tree selectedTree;
    
    public EditTreeTransaction(){
        super();
    }
    
    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        dependencies.put("SubmitBarcode", "TransactionError,UpdateStatusMessage,TreeToDisplay");
        dependencies.put("UpdateTree", "TransactionError,UpdateStatusMessage");
        dependencies.put("Cancel", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
       myMessages = ResourceBundle.getBundle("model.i18n.TreeTransaction", myLocale);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("EnterTreeBarcodeView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("EnterTreeBarcodeView", this);
            currentScene = new Scene(newView);
            myViews.put("EnterTreeBarcodeView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        return currentScene;
    }

    private void createAndShowTreeView() {
        Scene currentScene = (Scene) myViews.get("EditTreeTransactionView");
        if (currentScene == null) {
            View newView = ViewFactory.createView("EditTreeTransactionView", this);
            currentScene = new Scene(newView);
            
            currentScene.getStylesheets().add("userinterface/style.css");
            
            myViews.put("EditTreeTransactionView", currentScene);
        }
        
        swapToView(currentScene);
    }
        
    @Override
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
                
            case "UpdateStatusMessage":
                return updateStatusMessage;
                
            case "TreeToDisplay":
                return selectedTree;              

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
                
            case "SubmitBarcode":
                processBarcode((String)value);
                break;
                
            case "UpdateTree":
                processTransaction((Properties)value);
                break;
                
                /*
            case "Search":
                if (value != null){
                    persistentState = (Properties) value;
                    findTreesByBarCode(persistentState.getProperty("BarCode"));
                }
                else
                    findAllTrees();
                break;
            case "Modify":
                selectedTree = findTreeByBarCode((String)value);
                createAndShowTreeTransactionView();
                break;
            case "Cancel":
                swapToView(createView());
                break;
                */
        }

        myRegistry.updateSubscribers(key, this);
    }
    
    protected void processBarcode(String bc) {
        try {
            selectedTree = new Tree(bc);
            createAndShowTreeView();
        } catch (InvalidPrimaryKeyException ex) {
            transactionErrorMessage = myMessages.getString("treeNotFoundMsg");
        }
    }
    
    protected void processTransaction(Properties p) {
        if (!selectedTree.getState("Status").equals(p.getProperty("Status"))) {
            LocalDateTime currentDate = LocalDateTime.now();
            String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            selectedTree.stateChangeRequest("DateStatusUpdated", dateLastUpdate);
        }
        selectedTree.stateChangeRequest("Notes", p.getProperty("Notes"));
        
        selectedTree.update();
        updateStatusMessage = (String)selectedTree.getState("UpdateStatusMessage");
        transactionErrorMessage = updateStatusMessage;
        
        swapToView(createView());
    }
    
    /*
    private Tree findTreeByBarCode(String barcode){
        String query = "SELECT * FROM " + myTableName + " WHERE BarCode = " + barcode + ";";    
    
        Vector allDataRetrieved = getSelectQueryResult(query);
        Properties nextTreeData = (Properties)allDataRetrieved.elementAt(0);
        Tree tree = new Tree(nextTreeData);                
        return (tree);
    }
    
    private void findTreesByBarCode(String barcode){
        trees.clear();
        String query = "SELECT * FROM " + myTableName + " WHERE BarCode LIKE '%" + barcode + "%';";
        
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

        String query = "SELECT BarCode, TreeType, SalePrice, " +
              "Notes, Status FROM " + myTableName;
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

    public void switchToScene(Scene nextScene) {
        myStage.setScene(nextScene);
        myStage.sizeToScene();

        WindowPosition.placeCenter(myStage);
    }
    */
}
