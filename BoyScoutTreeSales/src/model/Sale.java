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
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Enumeration;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 *
 */
public class Sale extends EntityBase {
    private static final String myTableName = "Transaction";
    protected Properties dependencies;
    private String updateStatusMessage = "";
    private ResourceBundle myMessages;

    public Sale(String id) throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();

        String query = String.format(
                "SELECT * FROM %s WHERE ID = %s",
                myTableName,
                id);

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be exactly one book. Any more will be an error.
            if (size != 1) {
                throw new InvalidPrimaryKeyException(
                        String.format("Multiple Transactions found with matching ID : %s", barcode)
                );
            }
            else {
                // Copy all retrived data into persistent state.
                Properties retrievedTransactionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTransactionData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTransactionData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        else {
            throw new InvalidPrimaryKeyException(
                    String.format("No Transaction found with ID = %s ", id)
            );
        }
    }

    /**
     * @param props */
    //--------------------------------------------------------------------------
    public Sale(Properties props) {
        super(myTableName);
        setDependencies();

        persistentState = new Properties();

        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    public static int compare(Transaction t1, Transaction t2) {
        String id1 = (String)t1.getState("ID");
        String id2 = (String)t2.getState("ID");
        return id1.compareTo(id2);
    }

        /** */
    //--------------------------------------------------------------------------
    private void setDependencies() {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    /** */
    //--------------------------------------------------------------------------
    public void update() {
        try {
            Properties whereClause = new Properties();

            whereClause.setProperty("ID", persistentState.getProperty("ID"));

            updatePersistentState(mySchema, persistentState, whereClause);

            updateStatusMessage = String.format(
                    "Data for Transaction %s installed successfully in database!",
                    persistentState.getProperty("ID")
            );

        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Transaction data in database!";
        }
    }

    public void insert() {
        try {
            insertPersistentState(mySchema, persistentState);

            updateStatusMessage = String.format(
                    "Data for new Transaction : %s installed successfully in database!",
                    persistentState.getProperty("ID")
            );

        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding Transaction data in database!";
        }
    }

    public void delete() {
        try {
            Properties deleteClause = new Properties();
            deleteClause.setProperty("ID", persistentState.getProperty("ID"));

            deletePersistentState(mySchema, deleteClause);

            updateStatusMessage = String.format(
                    "Transaction : %s has been successfully deleted!",
                    persistentState.getProperty("ID")
            );
        } catch (SQLException ex) {
            updateStatusMessage = "Error deleting Transaction in database!";
        }
    }

    @Override
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;
        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (persistentState.containsKey(key)) {
            persistentState.setProperty(key, (String)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }

    public boolean isAvailable() {
        String status = persistentState.getProperty("Status");
        return status.equals("Available");
    }

    /**
     * This method is needed solely to enable the Account information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
            Vector<String> v = new Vector<String>();

            v.addElement(persistentState.getProperty("ID"));
            v.addElement(persistentState.getProperty("SessionID"));
            v.addElement(persistentState.getProperty("TransactionType"));
            v.addElement(persistentState.getProperty("Barcode"));
            v.addElement(persistentState.getProperty("TransactionAmount"));
            v.addElement(persistentState.getProperty("PaymentMethod"));
            v.addElement(persistentState.getProperty("CustomerName"));
            v.addElement(persistentState.getProperty("CustomerPhone"));
            v.addElement(persistentState.getProperty("CustomerEmail"));
            v.addElement(persistentState.getProperty("TransactionDate"));
            v.addElement(persistentState.getProperty("TransactionTime"));
            v.addElement(persistentState.getProperty("DateStatusUpdated"));

            return v;
    }

}
