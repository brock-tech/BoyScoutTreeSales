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

import java.util.Properties;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class OpenShiftTransaction extends Transaction {
    
    public OpenShiftTransaction() {
        super();
    }

    @Override
    protected void setDependencies() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void getMessagesBundle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("OpenShiftTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("OpenShiftTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("OpenShiftTransactionView", currentScene);
        }
        
        currentScene.getStylesheets().add("userinterface/style.css");
        
        return currentScene;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "SubmitSessionInfo":
                createNewSession((Properties)value);
                break;
            case "EditTreeType":
                break;
            case "RemoveTreeType":
                break;
            case "Submit":
                break;
            case "Cancel":
                swapToView(createView());
                break;
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    void createNewSession(Properties p) {
        
    }

    @Override
    public Object getState(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
