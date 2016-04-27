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
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class CloseShiftTransaction extends Transaction {
    
    protected SaleCollection saleCollection;
    protected float totalCash;
    protected float totalCheck;
    
    public CloseShiftTransaction() {
        super();
    }

    @Override
    protected void setDependencies() {
  //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void getMessagesBundle() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("CloseShiftTransactionView");
        
        if (currentScene == null) {
            View newView = ViewFactory.createView("CloseShiftTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("CloseShiftTransactionView", currentScene);
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
            case "Submit":
                break;
            case "searchTransactions":
                searchTransactions((Properties)value);
                totalAmount();
            case "Cancel":
                swapToView(createView());
                break;
        }
        
        myRegistry.updateSubscribers(key, this);
    }
    
    void closeSession(Properties p) {
        
    }

    @Override
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
        //    case "UpdateStatusMessage":
        //        return updateStatusMessage;
            case "checkAmount":
                return totalCheck;
            case "cashAmount":
                return totalCash;
            default:
                return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    protected void searchTransactions(Properties props) {
        String sessionId = props.getProperty("SessionId");
        
        saleCollection = new SaleCollection();
        
        try {
            saleCollection.lookupTransactionBySessionId(sessionId);
        } catch (Exception e) {
            transactionErrorMessage = e.getMessage();
        }
    }
    
    protected void totalAmount(){
        Vector entryList = (Vector)saleCollection.getState("Transactions");
        Enumeration entries = entryList.elements();            
        while (entries.hasMoreElements()) {
            Sale nextSale = (Sale)entries.nextElement();
            Vector<String> view = nextSale.getEntryListView();
            if (view.elementAt(2).equalsIgnoreCase("check"))
                totalCheck += Float.parseFloat(view.elementAt(4));
            else if (view.elementAt(2).equalsIgnoreCase("cash"))
                totalCash += Float.parseFloat(view.elementAt(4));
        }
    }
}
