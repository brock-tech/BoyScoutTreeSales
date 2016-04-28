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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * 
 */
public class Tree extends EntityBase {
    private static final String myTableName = "Tree";
    protected Properties dependencies;
    private String updateStatusMessage = "";
    private ResourceBundle myMessages;

    public Tree(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);
        
        setDependencies();
        
        String query = String.format(
                "SELECT * FROM %s WHERE barcode = %s", 
                myTableName, 
                barcode);

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be exactly one book. Any more will be an error.
            if (size != 1) {
                throw new InvalidPrimaryKeyException(
                        String.format("Multiple Tree found with matching Bar Code : %s", barcode)
                );
            }
            else {
                // Copy all retrived data into persistent state.
                Properties retrievedTreeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } 
        else {
            throw new InvalidPrimaryKeyException(
                    String.format("No Tree found with Bar Code = %s ", barcode)
            );
        }
    }
    
    /**
     * @param props */
    //--------------------------------------------------------------------------
    public Tree(Properties props) {
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
    
    public static int compare(Tree t1, Tree t2) {
        String barcode1 = (String)t1.getState("BarCode");
        String barcode2 = (String)t2.getState("BarCode");
        return barcode1.compareTo(barcode2);
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
            
            whereClause.setProperty("BarCode", persistentState.getProperty("BarCode"));

            updatePersistentState(mySchema, persistentState, whereClause);

            updateStatusMessage = String.format(
                    "Data for Tree %s installed successfully in database!",
                    persistentState.getProperty("BarCode")
            );

        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Tree data in database!";
        }
    }
    
    public void insert() {
        try {
            insertPersistentState(mySchema, persistentState);

            updateStatusMessage = String.format(
                    "Data for new Tree : %s installed successfully in database!",
                    persistentState.getProperty("BarCode")
            );

        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Tree data in database!";
        }
    }
    
    public void delete() {
        try {
            Properties deleteClause = new Properties();
            deleteClause.setProperty("BarCode", persistentState.getProperty("BarCode"));
            
            deletePersistentState(mySchema, deleteClause);
            
            updateStatusMessage = String.format(
                    "Tree : %s has been successfully deleted!",
                    persistentState.getProperty("BarCode")
            );
        } catch (SQLException ex) {
            updateStatusMessage = "Error deleting Tree in database!";
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
        System.out.println("Status prior " + persistentState.getProperty("Status"));
        if(status.equals("Available"))
            return true;
        
        return false;
    }
    
    public void setSold() {
        // Set date of last update to today's date.
        LocalDateTime currentDate = LocalDateTime.now();
        String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        persistentState.setProperty("DateStatusUpdated", dateLastUpdate);    
        persistentState.setProperty("Status", "Sold");
        
        stateChangeRequest("Status", "Sold");
        stateChangeRequest("DateStatusUpdated", dateLastUpdate);
        System.out.println(persistentState.getProperty("Status"));
        System.out.println("jere");
    }
    
    /**
     * This method is needed solely to enable the Account information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
            Vector<String> v = new Vector<String>();

            v.addElement(persistentState.getProperty("BarCode"));
            v.addElement(persistentState.getProperty("TreeType"));
            v.addElement(persistentState.getProperty("SalePrice"));
            v.addElement(persistentState.getProperty("Notes"));
            v.addElement(persistentState.getProperty("Status"));

            return v;
    }

}
