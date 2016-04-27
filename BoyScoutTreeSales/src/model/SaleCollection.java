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
import exception.InvalidPrimaryKeyException;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author mike
 */
public class SaleCollection extends EntityBase {
    private static final String myTableName = "Transaction";
    
    Vector<Sale> sales;

    public SaleCollection() {
        super(myTableName);
        
        sales = new Vector<>();
    }
    
    private int findIndexToAdd(Sale s) {
        int low = 0;
        int high = sales.size()-1;
        int middle;
        
        while (low <= high) {
            middle = (low + high) / 2;
            Sale midSale = sales.elementAt(middle);

            int result = Sale.compare(s, midSale);

            if (result == 0) {
                return middle;
            } else if (result < 0) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return low;
    }
    
    private void addSale(Sale s) {
        int index = findIndexToAdd(s);
        sales.insertElementAt(s, index);
    }

    @Override
    public Object getState(String key) {
        if (key.equals("Sales")) {
            return sales;
        }
        return null;
    }
    
    public Sale retrieve(String id) {
        Sale retValue = null;
        for (Sale s : sales) {
            String nextScoutId = (String)s.getState("ID");
            if (id.equals(nextScoutId)) {
                retValue = s;
                break;
            }
        }
        return retValue;
    }
    
    public void lookupSalesForSession(String sessionId) 
            throws InvalidPrimaryKeyException {
        String query = String.format("SELECT * FROM %1$s WHERE (SessionID = %2$s)",
                myTableName, sessionId);
        
        Vector allDataRetrieved = getSelectQueryResult(query);
        
        if ((allDataRetrieved == null) || allDataRetrieved.isEmpty()) {
                throw new InvalidPrimaryKeyException(
                    String.format("No Transactions found for Session %1$s",
                    sessionId));
        }
        
        sales = new Vector<>();
        for (Object entry : allDataRetrieved) {
            Sale newSale = new Sale((Properties)entry);
            addSale(newSale);
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(myTableName);
        }
    }
}
