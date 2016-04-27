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
                throw new InvalidPrimaryKeyException(String.format(
                    myMessages.getString("multipleSessionFoundMsg"), id)); // TODO: Add message
            }
            else {
                // Copy all retrived data into persistent state.
                Properties retrievedSessionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedSessionData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedSessionData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } 
        else {
            throw new InvalidPrimaryKeyException(String.format(
                    myMessages.getString("noSessionFoundMsg"), id));
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
    
    private Session() {
        super(myTableName);
        
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages = null;// ResourceBundle.getBundle("model.i18n.Session", myLocale);
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

                updateStatusMessage = myMessages.getString("updateSuccessMsg");

            } catch (SQLException ex) {
                updateStatusMessage = myMessages.getString("updateErrorMsg"); 
            }
        } 
        else { // Insert New
            try {
                Integer id = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", id.toString());
                
                updateStatusMessage = myMessages.getString("insertSuccessMsg");

            } catch (SQLException ex) {
                updateStatusMessage = myMessages.getString("insertErrorMsg"); // @todo: Add message
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
    
    public static Session findOpenSession() throws InvalidPrimaryKeyException {
        String query = "SELECT * FROM "+myTableName+" WHERE (EndTime = '<empty>')";
        
        Session session = new Session();
        
        Vector<Properties> allDataRetrieved = session.getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be exactly one session. Any more will be an error.
            if (size != 1) {
                throw new InvalidPrimaryKeyException(
                        session.myMessages.getString("noOpenSessionFound"));
            }
            else {
                // Copy all retrived data into persistent state.
                Properties retrievedSessionData = allDataRetrieved.elementAt(0);
                session.persistentState = new Properties();

                Enumeration allKeys = retrievedSessionData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedSessionData.getProperty(nextKey);

                    if (nextValue != null) {
                        session.persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } 
        else {
            throw new InvalidPrimaryKeyException(
                    session.myMessages.getString("multipleOpenSessionsFound"));
        }
        
        return session;
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    
}
