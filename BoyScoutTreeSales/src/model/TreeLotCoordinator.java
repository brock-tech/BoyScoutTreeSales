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

// Project Imports
import event.Event;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import java.util.Hashtable;
import java.util.Properties;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/**
 *
 */
public class TreeLotCoordinator implements IModel, IView {
    private Properties dependencies;
    private ModelRegistry myRegistry;
    
    private Hashtable<String, Scene> myViews;
    private Stage myStage;
    
    private String transactionErrorMessage = "";
    
    public TreeLotCoordinator() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<>();
        
        myRegistry = new ModelRegistry("");
        if (myRegistry == null) {
            new Event(
                    Event.getLeafLevelClassName(this),
                    "TreeLotCoordinator",
                    "Could not instantiate Registry",
                    Event.ERROR
                );
        }
        
        setDependencies();
        
        createAndShowView();
    }
    
    private void setDependencies() {
        
    }
    
    private void createAndShowView() {
        Scene currentScene = (Scene)myViews.get("TreeLotCoordinatorView");
        
        if (currentScene ==  null) {
            View newView = ViewFactory.createView("TreeLotCoordinatorView", this);
            currentScene = new Scene(newView);
            myViews.put("TreeLotCoordinatorView", currentScene);
        }
        
        myStage.setScene(currentScene);
        myStage.sizeToScene();
        
        WindowPosition.placeCenter(myStage);
    }
    
    public void runTransaction(String transType) {
        
    }
    
    @Override
    public Object getState(String key) {
        if (key.equals("TransactionError")) {
            return transactionErrorMessage;
        }
        
        return "";
    }
    
    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("Exit")) {
            System.exit(0);
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    @Override 
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    
    @Override
    public void unSubscribe(String key, IView subscribe) {
        myRegistry.unSubscribe(key, subscribe);
    }
    
    @Override
    public void subscribe(String key, IView subscribe) {
        myRegistry.subscribe(key, subscribe);
    }
}
