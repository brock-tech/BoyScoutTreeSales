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
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/**
 *
 */
public class TreeLotCoordinator implements IModel, IView {

    private Properties dependencies;
    private ModelRegistry myRegistry;

    private Locale myLocale;
    private ResourceBundle myMessages;

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

        setDependencies();
        
        loadLocale();
        
        myMessages = ResourceBundle.getBundle("model.i18n.TreeLotCoordinator", myLocale);

        createAndShowView();
    }

    private void setDependencies() {
        
    }
    
    private void loadLocale() {
        try {
            PropertyFile config = new PropertyFile("config.ini");
            String localeSpecification = config.getProperty("locale");
            setLocale(localeSpecification);
        }
        catch (Exception e) {
            System.out.println("BoyScoutTreeSales: Failed to determine locale. Using default (en_US).");
            setLocale("eu_US");
        }
    }
    
    public void setLocale(String localeSpecifier) {
        String[] specifierParts = localeSpecifier.split("_");
        String language = specifierParts[0];
        if (specifierParts.length == 1) {
            myLocale = new Locale(language);
        }
        else {
            String country = specifierParts[1];
            myLocale = new Locale(language, country);
        }
    }

    private void createAndShowView() {
        Scene currentScene = (Scene) myViews.get("TreeLotCoordinatorView");

        if (currentScene == null) {
            View newView = ViewFactory.createView("TreeLotCoordinatorView", this);
            currentScene = new Scene(newView);
            myViews.put("TreeLotCoordinatorView", currentScene);
        }

        myStage.setScene(currentScene);
        myStage.sizeToScene();

        WindowPosition.placeCenter(myStage);
    }

    @Override
    public Object getState(String key) {
        if (key.equals("TransactionError")) {
            return transactionErrorMessage;
        }
        
        if (key.equals("Locale")) {
            return myLocale;
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
            case "TransactionChoice":
                runTransaction((String)value);
                break;
            case "TransactionError":
                transactionErrorMessage = (String)value;
                break;
            case "Done":
                createAndShowView();
                break;
            case "Exit":
                System.exit(0);
            default:
                break;
        }

        myRegistry.updateSubscribers(key, this);
    }
    
    
    public void runTransaction(String transType) {
        Transaction trans = TransactionFactory.createTransaction(transType, this);
        
        trans.subscribe("CancelTransaction", this);
        trans.stateChangeRequest("DoYourJob", "");
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
