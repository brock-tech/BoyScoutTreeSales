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
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import userinterface.SystemLocale;

/**
 *
 * @author mike
 */
public class Session extends EntityBase {
    public static final String myTableName = "Session";
    
    protected Properties dependencies;
    private String updateStatusMessage = "";
    private final Locale myLocale;
    private final ResourceBundle myMessages;
    
    public Session(String id) throws InvalidPrimaryKeyException {
        super(myTableName);
        
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.Session", myLocale);
        
        String query = String.format("SELECT * FROM %s WHERE (ID = '%s')", myTableName, id);
        
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be exactly one session. Any more will be an error.
            if (size != 1) {
                throw new InvalidPrimaryKeyException(""); // TODO: Add message
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
            throw new InvalidPrimaryKeyException(""); // TODO: Add message
        }
    } 
    
    public Session(Properties props) {
        super(myTableName);
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages =  ResourceBundle.getBundle("model.i18n.Session", myLocale);
        
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
        if (persistentState.getProperty("ID") != null) { // Update Existing
            try {
                Properties whereClause = new Properties();

                whereClause.setProperty("ID", persistentState.getProperty("ID"));

                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = ""; // @todo: Add message

            } catch (SQLException ex) {
                updateStatusMessage = ""; // @todo: Add message
            }
        } 
        else { // Insert New
            try {
                Integer scoutId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", scoutId.toString());
                
                updateStatusMessage = ""; // @todo: Add message

            } catch (SQLException ex) {
                updateStatusMessage = ""; // @todo: Add message
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
        if (persistentState.containsKey(key))
            persistentState.setProperty(key, (String)value);
        
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    
}
