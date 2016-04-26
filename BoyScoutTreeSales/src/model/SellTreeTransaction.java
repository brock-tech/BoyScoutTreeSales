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
import java.util.Vector;

import exception.InvalidPrimaryKeyException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import userinterface.SystemLocale;

/**
 *
 */
public class SellTreeTransaction extends Sale {
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

    currentScene.getStylesheets().add("userinterface/styles.css");
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
        DoYourJob();
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

    //Existing Transaction
    try {
      String saleID = p.getProperty("ID");

      Sale oldSale = new Sale(saleID);
      updateStatusMessage = String.format(myMessages.getString("multipleTransFoundMsg"),
                  p.getProperty("ID"));
      transactionErrorMessage = updateStatusMessage;
      
    } catch (InvalidPrimaryKeyException exc) {

    }
    /*try {
      String memberId = p.getProperty("ID");

      Sale treeSale = new Sale(memberId);
      memberId = (String)treeSale.getState("ID");

      MessageFormat formatter = new MessageFormat(
              myMessages.getString("")*/

    }
  }

}
