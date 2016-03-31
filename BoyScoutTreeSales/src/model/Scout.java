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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import userinterface.SystemLocale;

/**
 *
 */
public class Scout extends EntityBase {
    private static final String myTableName = "Scout";
    
    protected Properties dependencies;
    
    private String updateStatusMessage = "";
    
    private Locale myLocale;
    private ResourceBundle myMessages;

    /**
     * @param troopId
     * @throws exception.InvalidPrimaryKeyException */
    //--------------------------------------------------------------------------
    public Scout(String troopId) throws InvalidPrimaryKeyException {
        super(myTableName);
        
        setDependencies();
        
        
        
        String query = String.format(
                "SELECT * FROM %s WHERE (TroopID = %s)",
                myTableName,
                troopId);
        
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be exactly one book. Any more will be an error.
            if (size != 1) {
                throw new InvalidPrimaryKeyException(
                        String.format("Multiple Scouts found with matching Troop ID : %s", troopId)
                );
            }
            else {
                // Copy all retrived data into persistent state.
                Properties retrievedScoutData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedScoutData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedScoutData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } 
        else {
            throw new InvalidPrimaryKeyException(
                    String.format("No Scout found with Troop ID = %s ", troopId)
            );
        }
        
        
    }
    
    /**
     * @param props */
    //--------------------------------------------------------------------------
    public Scout(Properties props) {
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
    
    private void getMessageBundle() {
        myLocale = SystemLocale.getInstance();
        
        myMessages = ResourceBundle.getBundle("model.i18n.Scout", myLocale);
    }
    
    /**
     * @param scout1
     * @param scout2
     * @return  */
    //--------------------------------------------------------------------------
    public static int compare(Scout scout1, Scout scout2) {
        String name1 = (String)scout1.getState("lastName");
        String name2 = (String)scout2.getState("lastName");
        
        return name1.compareTo(name2);
    }
    
    /** */
    //--------------------------------------------------------------------------
    public void update() {
        updateStateInDatabase();
    }
    
    /** */
    //--------------------------------------------------------------------------
    private void updateStateInDatabase() {
        // Set date of last update to today's date
        LocalDateTime currentDate = LocalDateTime.now();
        String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        persistentState.setProperty("DateLastUpdate", dateLastUpdate);
        
        try {
            if (persistentState.getProperty("ScoutID") != null) { // Insert New
                Properties whereClause = new Properties();

                whereClause.setProperty("ScoutID", persistentState.getProperty("ScoutID"));

                updatePersistentState(mySchema, persistentState, whereClause);
                
                
            } 
            else { // Update Existing
                Integer scoutId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ScoutID", scoutId.toString());
                
                
                
            }
        } 
        catch (SQLException ex) {
            updateStatusMessage = "Error in installing Scout data in database!";
        }
    }

    /**
     * @param key
     * @return  */
    //--------------------------------------------------------------------------
    @Override
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;
        
        return persistentState.getProperty(key);
    }

    /**
     * @param key
     * @param value */
    //--------------------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }
    
    public Vector<String> getTableListView() {
        Vector<String> v = new Vector<>();
        
        return v;
    }
    
    /**
     * @param tableName */
    //--------------------------------------------------------------------------
    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    
}
