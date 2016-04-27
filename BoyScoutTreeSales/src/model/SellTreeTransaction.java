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

/**
 *
 */
public class SellTreeTransaction extends Transaction {
  String updateStatusMessage;

  public SellTreeTransaction() {
    super();
  }

  @Override
  protected void setDependencies() {
    Properties dependencies = new Properties();
    dependencies.put("Submit", "TransactionError,UpdateStatusMessage");
    dependencies.put("Cancel", "CancelTransaction");

    myRegistry.setDependencies(dependencies);
  }

  @Override
  protected void getMessagesBundle() {
    myMessages = ResourceBundle.getBundle("model.i18n.TreeSaleTransaction", myLocale);
  }

  @Override
  protected Scene createView() {
    Scene currentScene = myViews.get("SellTreeTransactionView");

    if(currentScene == null) {
      View newView = ViewFactory.createView("SellTreeTransactionView", this);
      currentScene = new Scene(newView);
      myViews.put("SellTreeTransactionView", currentScene);
    }

    currentScene.getStylesheets().add("userinterface/style.css");
    return currentScene;
  }

  @Override
  public Object getState(String key) {
    switch (key) {
      case "TransactionError":
        return transactionErrorMessage;
      case "UpdateStatusMessage":
        return updateStatusMessage;
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
      case "Submit":
        processTransaction((Properties)value);
        break;
    }
    myRegistry.updateSubscribers(key, this);
  }

  private void processTransaction(Properties p) {
    updateStatusMessage = "";
    transactionErrorMessage = "";
    Tree treeSold = null;
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
                  p.getProperty("ID"));
      transactionErrorMessage = updateStatusMessage;
    }
    try{
         treeSold = new Tree(p.getProperty("Barcode"));
        updateStatusMessage = String.format(myMessages.getString("treeBarcodeNotFound"),
                  p.getProperty("Barcode"));
      transactionErrorMessage = updateStatusMessage;
    }
    catch(InvalidPrimaryKeyException exc)
    {
        if(treeSold != null)
        {
            if(treeSold.isAvailable())
                    treeSold.setSold();
        }
    }
  }
}
