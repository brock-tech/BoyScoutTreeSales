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
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.Scout", myLocale);
        
        String query = String.format(
                "SELECT * FROM %s WHERE (TroopID = %s)",
                myTableName,
                troopId);
        
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        
        MessageFormat formatter = new MessageFormat("", myLocale);
        
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be exactly one book. Any more will be an error.
            if (size != 1) {
                formatter.applyPattern(myMessages.getString("multipleScoutsFoundMsg"));
                throw new InvalidPrimaryKeyException(formatter.format(new Object[] {troopId}));
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
            formatter.applyPattern(myMessages.getString("scoutNotFoundMsg"));
            throw new InvalidPrimaryKeyException(formatter.format(new Object[] {troopId}));
        }
    }
    
    /**
     * @param props */
    //--------------------------------------------------------------------------
    public Scout(Properties props) {
        super(myTableName);
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.Scout", myLocale);
        
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
        
        MessageFormat formatter = new MessageFormat("", myLocale);
        Object[] firstAndLastName = new Object[] {
            persistentState.getProperty("FirstName"),
            persistentState.getProperty("LastName")
        };
        
        if (persistentState.getProperty("ScoutID") != null) { // Update Existing
            try {
                Properties whereClause = new Properties();

                whereClause.setProperty("ScoutID", persistentState.getProperty("ScoutID"));

                updatePersistentState(mySchema, persistentState, whereClause);

                formatter.applyPattern(myMessages.getString("updateSuccessMsg"));
                updateStatusMessage = formatter.format(firstAndLastName);

            } catch (SQLException ex) {
                formatter.applyPattern(myMessages.getString("updateErrorMsg"));
                updateStatusMessage = formatter.format(firstAndLastName);
            }
        } 
        else { // Insert New
            try {
                Integer scoutId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ScoutID", scoutId.toString());

                formatter.applyPattern(myMessages.getString("insertSuccessMsg"));
                updateStatusMessage = formatter.format(firstAndLastName);

            } catch (SQLException ex) {
                formatter.applyPattern(myMessages.getString("insertSuccessMsg"));
                updateStatusMessage = formatter.format(firstAndLastName);
            }
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
