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
import java.util.ResourceBundle;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;
import impresario.IModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class SellTreeTransaction extends Transaction {
    private String updateStatusMessage;
    
    protected Tree selectedTree;
    protected TreeType selectedTreeType;
    protected String sessionID;

    public SellTreeTransaction(IModel tlc) {
        super();
        sessionID = (String) tlc.getState("OpenSessionId");
    }

    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        dependencies.put("Submit", "TransactionError,UpdateStatusMessage");
        dependencies.put("Cancel", "CancelTransaction");
        dependencies.put("SubmitBarcode", "TransactionError,UpdateStatusMessage,"
                + "SelectedTree,SelectedTreeType");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
        myMessages = ResourceBundle.getBundle("model.i18n.TreeSaleTransaction", myLocale);
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

    private void createAndShowSaleView() {
        Scene currentScene = (Scene) myViews.get("SellTreeTransactionView");
        if (currentScene == null) {
            View newView = ViewFactory.createView("SellTreeTransactionView", this);
            currentScene = new Scene(newView);

            currentScene.getStylesheets().add("userinterface/style.css");

            myViews.put("SellTreeTransactionView", currentScene);
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
            case "SelectedTree":
                return selectedTree;
            case "SelectedTreeType":
                return selectedTreeType;
            case "Session":
                return sessionID;
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
                processBarcode((String) value);
                break;
            case "Submit":
                processTransaction((Properties) value);
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    protected void processBarcode(String bc) {
        try {
            selectedTree = new Tree(bc);
            
            String status = (String)selectedTree.getState("Status");
            if (status.equals("Sold")) {
                throw new InvalidPrimaryKeyException(
                        "Error: That tree has already been sold!");
            }
            
            String typeId = (String)selectedTree.getState("TreeType");
            
            selectedTreeType = new TreeType(typeId);
            
            createAndShowSaleView();
        } catch (InvalidPrimaryKeyException ex) {
            transactionErrorMessage = myMessages.getString("treeNotFound");
            updateStatusMessage = transactionErrorMessage;
        }
    }

    private void processTransaction(Properties p) {
        updateStatusMessage = "";
        transactionErrorMessage = "";
        
        LocalDateTime currentTime = LocalDateTime.now();
        
        p.setProperty("SessionID", sessionID);
        
        p.setProperty("TransactionType", "TreeSale");
        
        p.setProperty("Barcode", (String)selectedTree.getState("BarCode"));
        
        p.setProperty("TransactionDate", currentTime.format(DateTimeFormatter.ISO_DATE));
        
        p.setProperty("TransactionTime", currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        
        p.setProperty("DateStatusUpdated", currentTime.format(DateTimeFormatter.ISO_DATE));
        
        Sale newSale = new Sale(p);

        // Update tree
        selectedTree.setSold();
        selectedTree.update();

        newSale.insert();
        
        updateStatusMessage = (String)newSale.getState("UpdateStatusMessage");
        transactionErrorMessage = updateStatusMessage;
        
        swapToView(createView());
    }
}
