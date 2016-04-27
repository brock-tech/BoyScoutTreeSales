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
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/**
 *
 * @author mike
 */
public class CloseSessionTransaction extends Transaction {

    String sessionId;
    Session sessionToClose;
    
    public CloseSessionTransaction(IModel tlc) {
        super();
        
        sessionId = (String) tlc.getState("OpenSessionId");
    }

    @Override
    protected void setDependencies() {
        Properties dependencies = new Properties();
        
        dependencies.put("DoYourJob", "TransactionError");
        dependencies.put("Confirm", "OpenSessionId,CancelTransaction,TransactionError");
        dependencies.put("Cancel", "CancelTransaction");
        
        myRegistry.setDependencies(dependencies);
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
    public void doYourJob() {
        BigDecimal checkTotal, cashTotal, startingCash, endingCash;
        
        try {
            sessionToClose = new Session(sessionId);
            
            SaleCollection sc = new SaleCollection();
            try {
                sc.lookupSalesForSession(sessionId);
            } catch (Exception e) { }
            
            Vector<Sale> allSales = (Vector<Sale>) sc.getState("Sales");
            checkTotal = new BigDecimal(0);
            cashTotal = new BigDecimal(0);
            
            for (Sale sale : allSales) {
                BigDecimal transAmount = sale.getTransactionAmount();
                String saleType = (String) sale.getState("PaymentMethod");
                
                if (saleType.equals("check"))
                    checkTotal = checkTotal.add(transAmount);
                
                else if (saleType.equals("cash"))
                    cashTotal = cashTotal.add(transAmount);
            }
            
            sessionToClose.setTotalCheckTransactionsAmount(checkTotal);
            
            startingCash = sessionToClose.getStartingCash();
            endingCash = startingCash.add(cashTotal);
            sessionToClose.setEndingCash(endingCash);
            
            sessionToClose.setEndTime(LocalTime.now());
            
            swapToView(createView());
            
        } catch (Exception exc) {
            transactionErrorMessage = exc.getMessage();
            stateChangeRequest("Cancel", null);
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
    
            case "Confirm":
                sessionToClose.update();
                transactionErrorMessage = (String) sessionToClose.getState("UpdateStatusMessage");
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
                
            case "SessionToClose":
                return sessionToClose;
        }
        return null;
    }
}
