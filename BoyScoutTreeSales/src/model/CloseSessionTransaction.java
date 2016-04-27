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

import impresario.IModel;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class CloseSessionTransaction extends Transaction {
    String sessionId;
    Session currentSession;
    
    
    public CloseSessionTransaction(IModel tlc) {
        super();
        
        sessionId = (String) tlc.getState("OpenSessionID");
        
        try {
            currentSession = new Session(sessionId);
        } catch (Exception exc) {
            transactionErrorMessage = exc.getMessage();
            tlc.stateChangeRequest("TransactionError", transactionErrorMessage);
            tlc.stateChangeRequest("CancelTransaction", null);
        }
    }

    @Override
    protected void setDependencies() {
        
    }

    @Override
    protected void getMessagesBundle() {
        
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("CloseSessionTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("CloseSessionTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("CloseSessionTransactionView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        return currentScene;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getState(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
