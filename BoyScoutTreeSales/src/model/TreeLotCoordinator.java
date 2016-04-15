//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//*********************************************************************
package model;

// Project Imports
import common.PropertyFile;
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
import userinterface.SystemLocale;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/**
 *
 */
public class TreeLotCoordinator implements IModel, IView {

    private Properties dependencies;
    private final ModelRegistry myRegistry;

    private Locale myLocale;
    private final ResourceBundle myMessages;

    private final Hashtable<String, Scene> myViews;
    private final Stage myStage;

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
        
        // Determine Locale
        try {
            PropertyFile config = new PropertyFile("config.ini");
            String language = config.getProperty("language");
            String country = config.getProperty("country");
            SystemLocale.setLocale(language, country);
        }
        catch (Exception e) {
            System.out.println("BoyScoutTreeSales: Failed to determine locale. Using default (en_US).");
            
            SystemLocale.setLocale("en", "US");
        }
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.TreeLotCoordinator", myLocale);
        
        setDependencies();

        createAndShowMyView();
    }

    private void setDependencies() {
        dependencies = new Properties();
        
        myRegistry.setDependencies(dependencies);
    }
    
    private void createAndShowMyView() {
        Scene nextScene = (Scene) myViews.get("TreeLotCoordinatorView");

        if (nextScene == null) {
            View newView = ViewFactory.createView("TreeLotCoordinatorView", this);
            nextScene = new Scene(newView);
            nextScene.getStylesheets().add("userinterface/style.css");
            myViews.put("TreeLotCoordinatorView", nextScene);
        }
        
        switchToScene(nextScene);
    }
    
    private void createAndShowAdminView() {
        Scene nextScene = (Scene) myViews.get("SelectAdminActionView");

        if (nextScene == null) {
            View newView = ViewFactory.createView("SelectAdminActionView", this);
            nextScene = new Scene(newView);
            nextScene.getStylesheets().add("userinterface/style.css");
            myViews.put("SelectAdminActionView", nextScene);
        }
        
        switchToScene(nextScene);
    }

    public void switchToScene(Scene nextScene) {
        myStage.setScene(nextScene);
        myStage.sizeToScene();

        WindowPosition.placeCenter(myStage);
    } 

    @Override
    public Object getState(String key) {
        if (key.equals("TransactionError")) {
            return transactionErrorMessage;
        }
        
        return "";
    }
    
    
    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "Administration":
                createAndShowAdminView();
                break;
                
            case "SellTree":
            case "OpenShift":
            case "CloseShift":
            case "RegisterScout":
            case "EditScout":
            case "AddTree":
            case "EditTree":
            case "RemoveTree":
            case "AddTreeType":
            case "EditTreeType":
                doTransaction(key);
                break;
                
            case "SessionStatus": 
                break;
                
            case "TransactionError":
                transactionErrorMessage = (String)value;
                break;
                
            case "CancelTransaction":
                createAndShowMyView();
                break;
                
            case "Exit":
                System.exit(0);
                
            default: break;
        }

        myRegistry.updateSubscribers(key, this);
    }
    
    
    public void doTransaction(String transType) {
        Transaction trans = TransactionFactory.createTransaction(transType);
        
        if (trans != null) {
            trans.subscribe("CancelTransaction", this);
            trans.stateChangeRequest("DoYourJob", "");
        }
        else {
            new Event(
                    Event.getLeafLevelClassName(this),
                    "TreeLotCoordinator",
                    "Could not instantiate delegate for '"+transType+"' transaction type." ,
                    Event.ERROR
            );
            System.out.println("FATAL ERROR: '"+transType+"' is not a valid transaction!");
        }
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
