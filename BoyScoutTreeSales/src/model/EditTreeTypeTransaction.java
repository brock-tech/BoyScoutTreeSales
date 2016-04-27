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

import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class EditTreeTypeTransaction extends Transaction {
    protected String updateStatusMessage;
    
    protected TreeTypeCollection treeTypeCollection;
    
    private TreeType selectedTreeType;
    
    public EditTreeTypeTransaction() {
        super();
    }

    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        dependencies.put("SearchTreeTypes", "TreeTypes,TransactionError");
        dependencies.put("EditTreeType", "TreeTypeToDisplay");
        dependencies.put("Submit", "TreeTypes,TransactionError,UpdateStatusMessage");
        dependencies.put("Cancel", "CancelTransaction");
        
        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
        //myMessages = ResourceBundle.getBundle("model.i18n.EditScoutTransaction", myLocale);
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
    
    private void createAndShowTreeTypeView() {
        Scene currentScene = myViews.get("TreeTypeDataView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("TreeTypeDataView", this);
            currentScene = new Scene(newView);
            currentScene.getStylesheets().add("userinterface/style.css");
            myViews.put("TreeTypeDataView", currentScene);
        }
        
        swapToView(currentScene);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "SearchTreeTypes":
                searchTreeTypes((Properties)value);
                break;
            case "EditTreeType":
                editTreeType((String)value);
                break;
            case "Submit":
                updateSelectedTreeType((Properties)value);
                break;
            case "Cancel":
                swapToView(createView());
                break;
        }
        
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
            case "UpdateStatusMessage":
                return updateStatusMessage;
            case "TreeTypes":
                return treeTypeCollection.getState("TreeTypes");
            case "TreeTypeToDisplay":
                return selectedTreeType;
            default:
                return null;
        }
    }
    
    protected void searchTreeTypes(Properties props) {
        String barcodePrefix = props.getProperty("BarcodePrefix");
        
        treeTypeCollection = new TreeTypeCollection();
        
        try {
            treeTypeCollection.lookupTreeTypesByBarcode(barcodePrefix);
            System.out.println("barcode is " + barcodePrefix);
        } catch (Exception e) {
            transactionErrorMessage = e.getMessage();
        }
    }
    
    protected void removeTreeType(String treeTypeId) {
        selectedTreeType = treeTypeCollection.retrieve(treeTypeId);
        String barcodePrefix = (String)selectedTreeType.getState("BarcodePrefix");
        
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION, String.format(
                "Are you sure you want to remove '%s'?",
                barcodePrefix));
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedTreeType.remove();
        updateStatusMessage = (String)selectedTreeType.getState("deleteSuccessMsg");
        transactionErrorMessage = updateStatusMessage;

        }
    }
    
    protected void editTreeType(String treeTypeId) {
        selectedTreeType = treeTypeCollection.retrieve(treeTypeId);
        System.out.println("Collection retrieved " + treeTypeCollection.retrieve(treeTypeId));
        createAndShowTreeTypeView();
    }
    
    protected void updateSelectedTreeType(Properties p) {
        
        Enumeration allKeys = p.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = p.getProperty(nextKey);

            if (nextValue != null) {
                selectedTreeType.stateChangeRequest(nextKey, nextValue);
            }
        }
        
        selectedTreeType.update();
        updateStatusMessage = (String)selectedTreeType.getState("UpdateStatusMessage");
        transactionErrorMessage = updateStatusMessage;
        
        swapToView(createView());
    }
}
