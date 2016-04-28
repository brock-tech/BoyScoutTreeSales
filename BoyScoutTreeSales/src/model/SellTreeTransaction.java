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

import exception.InvalidPrimaryKeyException;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;
import impresario.IModel;

/**
 *
 */
public class SellTreeTransaction extends Transaction {
  String updateStatusMessage;
  protected Tree selectedTree;
    String sessionID;
  public SellTreeTransaction(IModel tlc)
  {
      super();
      sessionID = (String)tlc.getState("OpenSessionId");
  }
  
  @Override
  protected void setDependencies() {
    Properties dependencies = new Properties();
    dependencies.put("Submit", "TransactionError,UpdateStatusMessage");
    dependencies.put("Cancel", "CancelTransaction");
   dependencies.put("SubmitBarcode", "TransactionError,UpdateStatusMessage,SaleToDisplay");

    myRegistry.setDependencies(dependencies);
  }

  @Override
  protected void getMessagesBundle() {
    myMessages = ResourceBundle.getBundle("model.i18n.TreeSaleTransaction", myLocale);
  }

  @Override
  protected Scene createView() {
    Scene currentScene = myViews.get("EnterTreeBarcodeView");

    if(currentScene == null) {
      View newView = ViewFactory.createView("EnterTreeBarcodeView", this);
      currentScene = new Scene(newView);
      myViews.put("EnterTreeBarcodeView", currentScene);
    }

    currentScene.getStylesheets().add("userinterface/style.css");
    return currentScene;
  }
  
  
    private void createAndShowSaleView() {
        Scene currentScene = (Scene) myViews.get("SellTreeTransactionView");
        if (currentScene == null) {
            View newView = ViewFactory.createView("SellTreeTransactionView", this);
            currentScene = new Scene(newView);
            
            currentScene.getStylesheets().add("userinterface/style.css");
            
            myViews.put("SellTreeTransactionView", currentScene);
        }
        
        swapToView(currentScene);
    }

  @Override
  public Object getState(String key) {
    switch (key) {
      case "TransactionError":
        return transactionErrorMessage;
      case "UpdateStatusMessage":
        return updateStatusMessage;
        case "SaleToDisplay":
                return selectedTree;  
        case "Session":
            return sessionID;
      default:
        return null;
    }
  }

  @Override
  public void stateChangeRequest(String key, Object value) {
    switch (key) {
      case "DoYourJob":
        doYourJob();
        break;
    case "SubmitBarcode":
           processBarcode((String)value);
           break;
      case "Submit":
        processTransaction((Properties)value);
        break;
    }
    myRegistry.updateSubscribers(key, this);
  }
  
  
      protected void processBarcode(String bc) {
        try {
            selectedTree = new Tree(bc);
            createAndShowSaleView();
        } catch (InvalidPrimaryKeyException ex) {
            transactionErrorMessage = "Error: No Tree exists under that Bar Code!";
            updateStatusMessage = transactionErrorMessage;
        }
    }
      

  private void processTransaction(Properties p) {
    updateStatusMessage = "";
    transactionErrorMessage = "";
    //Existing Transaction
    try {
      String saleID = p.getProperty("ID");

      Sale oldSale = new Sale(saleID);
      updateStatusMessage = String.format(myMessages.getString("multipleTransFoundMsg"),
                  p.getProperty("ID"));
      transactionErrorMessage = updateStatusMessage;
    } catch (InvalidPrimaryKeyException exc) {
      //Add new Transaction
      Sale sale = new Sale(p);
      sale.insert();
      updateStatusMessage = String.format(myMessages.getString("insertSuccessMsg"),
                  p.getProperty("CustomerName"));
      transactionErrorMessage = updateStatusMessage;
    }
    try{
         Tree treeSold = new Tree(p.getProperty("Barcode"));
            if(treeSold.isAvailable())
            {
                treeSold.setSold();
                updateStatusMessage = String.format(myMessages.getString("insertSuccessMsg"),
                  p.getProperty("CustomerName"));
                  transactionErrorMessage = updateStatusMessage;
            }
            else {
                updateStatusMessage = String.format(myMessages.getString("treeNotAvailable"),
                  p.getProperty("Barcode"));
                  transactionErrorMessage = updateStatusMessage;
            }
    }
    catch(InvalidPrimaryKeyException exc)
    {
      updateStatusMessage = String.format(myMessages.getString("treeBarcodeNotFound"),
                  p.getProperty("Barcode"));
      transactionErrorMessage = updateStatusMessage;
    }
  }
}
