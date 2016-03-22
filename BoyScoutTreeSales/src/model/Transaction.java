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

import event.Event;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;

/**
 *
 */
public abstract class Transaction implements IView, IModel {
    
    // For Impresario
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    
    protected Locale myLocale;
    protected ResourceBundle myMessages;
    
    protected Stage myStage;
    protected Hashtable<String, Scene> myViews;
    
    protected TreeLotCoordinator myCoordinator;
    protected String transactionErrorMessage;
    
    public Transaction(TreeLotCoordinator tlc) {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<>();
        myCoordinator = tlc;
        myRegistry = new ModelRegistry("Transaction");
        
        myLocale = (Locale)myCoordinator.getState("Locale");
        
        setDependencies();
        
        getMessagesBundle();
    }
    
    protected abstract void setDependencies();
    
    protected abstract void getMessagesBundle();
    
    protected abstract Scene createView();
    
    public void doYourJob() {
        Scene newScene = createView();
        
        swapToView(newScene);
    }
    
    public void swapToView(Scene newScene) {
        if (newScene == null) {
            System.out.println("Transaction.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }
        
        myStage.setScene(newScene);
        myStage.sizeToScene();
        
        WindowPosition.placeCenter(myStage);
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public abstract Object getState(String key);
    
    @Override
    public abstract void stateChangeRequest(String key, Object value);

    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }
}
