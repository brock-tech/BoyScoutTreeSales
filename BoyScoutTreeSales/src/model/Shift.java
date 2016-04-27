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

//import exception.InvalidPrimaryKeyException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
//import java.util.Vector;
import userinterface.SystemLocale;

/**
 *
 * @author mike
 */
public class Shift extends EntityBase {
    private static final String myTableName = "Shift";
    
    private String updateStatusMessage = "";
    
    private final Locale myLocale;
    private final ResourceBundle myMessages;
    
    /*
    public Shift(String scoutId, String sessionId) throws InvalidPrimaryKeyException {
        super(myTableName);
        
        myLocale = SystemLocale.getInstance();
        myMessages = null;//ResourceBundle.getBundle("model.i18n.Shift", myLocale);
        
        String query = String.format(
                "SELECT * FROM %1$s WHERE (ScoutID = %2$s) AND (SessionID = %3$s)",
                myTableName, scoutId, sessionId);
        
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            
            if (size != 1) {
                throw new InvalidPrimaryKeyException("");
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
            throw new InvalidPrimaryKeyException("");
        }
    }
    */

    public Shift(Properties props) {
        super(myTableName);
        
        myLocale = SystemLocale.getInstance();
        myMessages =  ResourceBundle.getBundle("model.i18n.Shift", myLocale);
        
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
    

    public void update() {
        if (persistentState.getProperty("ID") != null) { // Update Existing
            try {
                Properties whereClause = new Properties();

                whereClause.setProperty("ID", persistentState.getProperty("ID"));

                updatePersistentState(mySchema, persistentState, whereClause);

//                updateStatusMessage = String.format(myLocale,
//                        myMessages.getString("updateSuccessMsg"),
//                        persistentState.getProperty("FirstName"),
//                        persistentState.getProperty("LastName"));

            } catch (SQLException ex) {
//                updateStatusMessage = String.format(myLocale,
//                        myMessages.getString("updateErrorMsg"),
//                        persistentState.getProperty("FirstName"),
//                        persistentState.getProperty("LastName"));
            }
        } 
        else { // Insert New
            try {
                Integer shiftId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", shiftId.toString());
                
                updateStatusMessage = myMessages.getString("insertSuccessMsg");

            } catch (SQLException ex) {
                updateStatusMessage = myMessages.getString("insertErrorMsg");
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
