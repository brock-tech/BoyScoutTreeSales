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
public class AddScoutTransaction extends Transaction {
    
    

    public AddScoutTransaction(TreeLotCoordinator tlc) {
        super(tlc);
    }

    @Override
    protected void setDependencies() {
        subscribe("TransactionErrorMessage", this);
    }

    @Override
    protected void getMessagesBundle() {
        
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddScoutTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("AddScoutTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddScoutTransactionView", currentScene);
        }
        
        return currentScene;
    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        }
        if (key.equals("AddScout")) {
            addScout((Properties)value);
        }
    }
    
    private void addScout(Properties p) {
        Scout scout = new Scout(p);
        
        scout.update();
    }
}
