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

import java.time.LocalDateTime;
import java.util.Properties;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class OpenSessionTransaction extends Transaction {
    LocalDateTime currentDateTime = LocalDateTime.now();
    String updateStatusMessage = "";
    
    String sessionId = "";
    String selectedScoutId = "";
    ScoutCollection scoutSearchResults = new ScoutCollection();
    ScoutCollection scoutsOnShift = new ScoutCollection();
    
    public OpenSessionTransaction() {
        super();
    }

    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        
        dependencies.put("SubmitSession", "OpenSessionId,TransactionError,UpdateStatusMessage");
        dependencies.put("SearchScouts", "SearchResults");
        dependencies.put("SelectScout", "SelectedScout,TransactionError,UpdateStatusMessage");
        dependencies.put("SubmitShift", "ScoutsOnShift,TransactionError,UpdateStatusMessage");
        dependencies.put("Done", "CancelTransaction");
        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected void getMessagesBundle() {
        
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("OpenSessionTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("OpenSessionTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("OpenSessionTransactionView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        return currentScene;
    }
    
    protected void createAndShowNewShiftView() {
        Scene currentScene = myViews.get("OpenShiftLookupView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("OpenShiftLookupView", this);
            currentScene = new Scene(newView);
            myViews.put("OpenShiftLookupView", currentScene);
            
            Properties p = new Properties();
            p.setProperty("FirstName", "");
            p.setProperty("LastName", "");
            stateChangeRequest("SearchScouts", p);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        swapToView(currentScene);
    }
    
    protected void createAndShowShiftFormView() {
        Scene currentScene = myViews.get("OpenShiftFormView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("OpenShiftFormView", this);
            currentScene = new Scene(newView);
            myViews.put("OpenShiftFormView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        swapToView(currentScene);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
                
            case "SubmitSession":
                createNewSession((Properties)value);
                break;
                
            case "SearchScouts":
                searchScouts((Properties)value);
                break;
                
            case "SelectScout":
                selectedScoutId = (String)value;
                if (scoutsOnShift.retrieve(selectedScoutId) != null) {
                    updateStatusMessage = "Scout already added to session!";
                }
                else {
                    createAndShowShiftFormView();
                }
                break;
            
            case "SubmitShift":
                submitShift((Properties)value);
                break;
                
            case "Back":
                createAndShowNewShiftView();
                break;
                
            case "Done":
                break;
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    void createNewSession(Properties p) {
        updateStatusMessage = "";
        transactionErrorMessage = "";
        
        Session newSession = new Session(p);
        newSession.update();
        
        sessionId = (String) newSession.getState("ID");
        updateStatusMessage = (String) newSession.getState("UpdateStatusMessage");
        transactionErrorMessage = updateStatusMessage;
        
        if ((sessionId != null) && (!"".equals(sessionId))) {
            createAndShowNewShiftView();          
        }
    }
    
    private void searchScouts(Properties searchTerms) {
        String firstName = searchTerms.getProperty("FirstName");
        String lastName = searchTerms.getProperty("LastName");
        
        scoutSearchResults = new ScoutCollection();
        
        try {
            scoutSearchResults.lookupScoutsByName(firstName, lastName);
        } catch (Exception e) {
            updateStatusMessage = e.getMessage();
            transactionErrorMessage = updateStatusMessage;
        }
    }
    
    void submitShift(Properties p) {     
        updateStatusMessage = "";
        transactionErrorMessage = "";
        
        p.setProperty("SessionID", sessionId);
        p.setProperty("ScoutID", selectedScoutId);
        
        Shift newShift = new Shift(p);
        newShift.update();
        updateStatusMessage = (String) newShift.getState("UpdateStatusMessage");
        transactionErrorMessage = updateStatusMessage;
        
        String shiftId = (String) newShift.getState("ID");
        if ((sessionId != null) && (!"".equals(sessionId))) {
            scoutsOnShift.addScout(scoutSearchResults.retrieve(selectedScoutId));
            createAndShowNewShiftView();
        }
    }
    

    @Override
    public Object getState(String key) {
        switch (key) {
            case "CurrentDateTime":
                return currentDateTime;
                
            case "OpenSessionId":
                return sessionId;
                
            case "SearchResults":
                return scoutSearchResults.getState("Scouts");
                
            case "SelectedScout":
                return scoutSearchResults.retrieve(selectedScoutId);
                
            case "ScoutsOnShift":
                return scoutsOnShift.getState("Scouts");
                
            case "UpdateStatusMessage":
                return updateStatusMessage;
                
            case "TransactionErrorMessage":
                return transactionErrorMessage;
                
            default:
                return null;
        }
    }
    
}
