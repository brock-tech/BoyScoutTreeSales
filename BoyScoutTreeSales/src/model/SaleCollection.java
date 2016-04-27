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
import impresario.IView;
import java.util.Properties;
import java.util.Vector;

/**
 * 
 */
public class SaleCollection extends EntityBase implements IView {
    private static final String myTableName = "Transaction";
    
    private Vector<Sale> transactions;
    
    public SaleCollection() {
        super(myTableName);
        
        transactions = new Vector<>();
    }

    @Override
    public Object getState(String key) {
        if (key.equals("Transactions")) {
            return transactions;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    
    public void lookupTransactionBySessionId(String sessionId) 
            throws InvalidPrimaryKeyException {
        
        String query = "SELECT TransactionType, TransactionAmount, PaymentMethod FROM "+myTableName
                +" WHERE SessionId = " + sessionId + ";";
        
        Vector allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            transactions = new Vector<>();
            for (Object nextTransactionData : allDataRetrieved) {
                Sale nextTransaction = new Sale((Properties)nextTransactionData);
                transactions.add(nextTransaction);
            }
        }
        else {
            throw new InvalidPrimaryKeyException(
                    String.format("No Transactions found with SessionId = '%s'", sessionId));
        }
    }
    
    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(myTableName);
        }
    }
}
