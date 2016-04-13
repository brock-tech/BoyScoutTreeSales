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

        /** */
    //--------------------------------------------------------------------------
    private void setDependencies() {
        dependencies = new Properties();
        
        myRegistry.setDependencies(dependencies);
    }
    
        /** */
    //--------------------------------------------------------------------------
    public void update() {
        updateStateInDatabase();
    }
    
    /** */
    //--------------------------------------------------------------------------
    private void updateStateInDatabase() {
        if ((persistentState.getProperty("isNew") == null) &&
                (persistentState.getProperty("BarCode") != null)) { // Update Existing
            try {
                persistentState.remove("isNew");

                Properties whereClause = new Properties();

                //whereClause.setProperty("ScoutID", persistentState.getProperty("ScoutID"));
                whereClause.setProperty("BarCode", persistentState.getProperty("BarCode"));

                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = String.format(
                    "Data for new Tree : %s installed successfully in database!",
                    persistentState.getProperty("BarCode")
                );

            } catch (SQLException ex) {
                updateStatusMessage = "Error in installing Tree data in database!";
            }
        } 
        else { // Insert New
            try {
                persistentState.remove("isNew");

                insertPersistentState(mySchema, persistentState);

                updateStatusMessage = String.format(
                    "Data for new Tree : %s installed successfully in database!",
                    persistentState.getProperty("BarCode")
                );

            } catch (SQLException ex) {
                updateStatusMessage = "Error in installing Tree data in database!";
            }
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

            v.addElement(persistentState.getProperty("BarCode"));
            v.addElement(persistentState.getProperty("TreeType"));
            v.addElement(persistentState.getProperty("SalePrice"));
            v.addElement(persistentState.getProperty("Notes"));
            v.addElement(persistentState.getProperty("Status"));

            return v;
    }

}
