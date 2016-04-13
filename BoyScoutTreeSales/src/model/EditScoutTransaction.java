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
public class EditScoutTransaction extends Transaction {
    protected String updateStatusMessage;
    
    protected ScoutCollection scoutCollection;
    
    private Scout selectedScout;
    
    public EditScoutTransaction() {
        super();
    }

    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        dependencies.put("SearchScouts", "Scouts,TransactionError");
        dependencies.put("RemoveScout", "Scouts,TransactionError,UpdateStatusMessage");
        dependencies.put("EditScout", "ScoutToDisplay");
        dependencies.put("Submit", "Scouts,TransactionError,UpdateStatusMessage");
        dependencies.put("Back", "CancelTransaction");
        
        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
        //myMessages = ResourceBundle.getBundle("model.i18n.EditScoutTransaction", myLocale);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("EditScoutTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("EditScoutTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("EditScoutTransactionView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        return currentScene;
    }
    
    private void createAndShowScoutView() {
        Scene currentScene = myViews.get("ScoutFormView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("ScoutDataView", this);
            currentScene = new Scene(newView);
            currentScene.getStylesheets().add("userinterface/style.css");
            myViews.put("ScoutFormView", currentScene);
        }
        
        swapToView(currentScene);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "SearchScouts":
                searchScouts((Properties)value);
                break;
            case "RemoveScout":
                removeScout((String)value);
                break;
            case "EditScout":
                editScout((String)value);
                break;
            case "Submit":
                updateSelectedScout((Properties)value);
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
            case "Scouts":
                return scoutCollection.getState("Scouts");
            case "ScoutToDisplay":
                return selectedScout;
            default:
                return null;
        }
    }
    
    protected void searchScouts(Properties props) {
        String firstName = props.getProperty("FirstName");
        String lastName = props.getProperty("LastName");
        
        scoutCollection = new ScoutCollection();
        
        try {
            scoutCollection.lookupScoutsByName(firstName, lastName);
        } catch (Exception e) {
            transactionErrorMessage = e.getMessage();
        }
    }
    
    protected void removeScout(String scoutId) {
        selectedScout = scoutCollection.retrieve(scoutId);
        String firstName = (String)selectedScout.getState("FirstName");
        String lastName = (String)selectedScout.getState("LastName");
        
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION, String.format(
                "Are you sure you want to set '%s %s' to 'Inactive' ?",
                firstName,
                lastName));
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedScout.setInactive();
            selectedScout.update();
        }
    }
    
    protected void editScout(String scoutId) {
        selectedScout = scoutCollection.retrieve(scoutId);
        
        createAndShowScoutView();
    }
    
    protected void updateSelectedScout(Properties p) {
        
        Enumeration allKeys = p.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = p.getProperty(nextKey);

            if (nextValue != null) {
                selectedScout.stateChangeRequest(nextKey, nextValue);
            }
        }
        
        selectedScout.update();
        updateStatusMessage = (String)selectedScout.getState("UpdateStatusMessage");
        transactionErrorMessage = updateStatusMessage;
        
        swapToView(createView());
    }
}
