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
public class TreeType extends EntityBase 
{
    private static final String myTableName = "Tree_Type";
    
    protected Properties dependencies;
    private String updateStatusMessage = "";
    protected TreeLotCoordinator myTLC;
    private Locale myLocale;
    private ResourceBundle myMessages;

    //--------------------------------------------------------------------------
    public TreeType(String id) throws InvalidPrimaryKeyException
    {
	super(myTableName);
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.TreeType", myLocale);
        MessageFormat formatter = new MessageFormat("", myLocale);
        
        String query = String.format("SELECT * FROM %s WHERE (ID = %s)", myTableName, id);
 
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

	// You must get one Tree Type at least
	if (allDataRetrieved != null)
	{
            int size = allDataRetrieved.size();
                if (size != 1)
		{
                    formatter.applyPattern(myMessages.getString("multipleTTFoundMsg"));
                    throw new InvalidPrimaryKeyException(formatter.format(new Object[] {id}));
		}
                else
		{
                    // copy all the retrieved data into persistent state
                    Properties retrievedTreeTypeData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();
                    
                    Enumeration allKeys = retrievedTreeTypeData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedTreeTypeData.getProperty(nextKey);

                        if (nextValue != null)
                        {
                                persistentState.setProperty(nextKey, nextValue);
                        }
                    }
                }
	}
        else
        {
            formatter.applyPattern(myMessages.getString("TTNotFound"));
            throw new InvalidPrimaryKeyException(formatter.format(new Object[] {id}));
        }
    }
    /*
    public TreeType(String barcodePrefix, int pref) throws InvalidPrimaryKeyException
    {
	super(myTableName);
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.TreeType", myLocale);
        MessageFormat formatter = new MessageFormat("", myLocale);
        
         String query = String.format(
                "SELECT * FROM %s WHERE (ID = %s)",
                myTableName,
                barcodePrefix);
 
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

	// You must get one Tree Type at least
	if (allDataRetrieved != null)
	{
            int size = allDataRetrieved.size();
                if (size != 1)
		{
                    formatter.applyPattern(myMessages.getString("multipleTTFoundMsg"));
                    throw new InvalidPrimaryKeyException(formatter.format(new Object[] {barcodePrefix}));
		}
                else
		{
                    // copy all the retrieved data into persistent state
                    Properties retrievedTreeTypeData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();
                    
                    Enumeration allKeys = retrievedTreeTypeData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedTreeTypeData.getProperty(nextKey);

                        if (nextValue != null)
                        {
                                persistentState.setProperty(nextKey, nextValue);
                        }
                    }
                }
	}
        else
        {
            formatter.applyPattern(myMessages.getString("TTNotFound"));
            throw new InvalidPrimaryKeyException(formatter.format(new Object[] {barcodePrefix}));
        }
    }
*/
    //--------------------------------------------------------------------------
    public TreeType(Properties props) 
    {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.TreeType", myLocale);
        
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);
            //System.out.println("next key " + nextKey + " nextValue " + nextValue);
            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }
    //--------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }
    //--------------------------------------------------------------------------
    
    public void update()
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        //LocalDateTime currentDate = LocalDateTime.now();
        //String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        //persistentState.setProperty("DateLastUpdate", dateLastUpdate);
        Object [] ID_barcodePrefix = new Object[] {persistentState.get("ID"), 
            persistentState.get("BarcodePrefix")};
        
        MessageFormat formatter;
        if (persistentState.getProperty("ID") != null)
        {
            try
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID",persistentState.getProperty("ID"));

                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = String.format(myLocale,myMessages.getString("updateSuccessMsg"),
               persistentState.get("ID"), persistentState.get("BarcodePrefix" ));
            }
            catch(SQLException s)
            {
               updateStatusMessage = String.format(myLocale,myMessages.getString("updateErrorMsg"),
              persistentState.get("ID"), persistentState.get("BarcodePrefix" ));
            }
        }   
        else 
        {
            try
            {
                Integer treeTypeID =
                insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + treeTypeID.toString());
   
              
               updateStatusMessage = String.format(myLocale, myMessages.getString("insertSuccessMsg"),
               treeTypeID.toString(), persistentState.get("BarcodePrefix" ));

            }
            catch(SQLException e)
            {
               updateStatusMessage = String.format(myLocale, myMessages.getString("insertErrorMsg"),
             persistentState.get("ID"), persistentState.get("BarcodePrefix" ));
            }
        }   
       System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    //--------------------------------------------------------------------------
    @Override
    public Object getState(String key) 
    {
       if (key.equals("UpdateStatusMessage") == true)
	return updateStatusMessage;
       if (key.equals("getProperties") == true)
           return persistentState;
        return persistentState.getProperty(key);
    }
    //--------------------------------------------------------------------------
   public static int compare(TreeType a, TreeType b)
    {
        String aNum = (String)a.getState("BarcodePrefix");
        String bNum = (String)b.getState("BarcodePrefix");

        return aNum.compareTo(bNum);
    }
   //--------------------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) 
    {
        myRegistry.updateSubscribers(key, this);
    }
    
    public void remove()
    {
        try{
 
            deletePersistentState(mySchema, persistentState);
            updateStatusMessage = String.format(myLocale, myMessages.getString("deleteSuccessMsg"),
             persistentState.get("ID"), persistentState.get("BarcodePrefix"));
        }
        catch(SQLException e)
        {
             updateStatusMessage = String.format(myLocale, myMessages.getString("deleteErrorMsg"),
             persistentState.get("ID"));
        }
 
    }
    //-------------------------------------------------------------------------
     public Vector<String> getTableListView() 
     {
        Vector<String> v = new Vector<>();
        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("TypeDescription"));
	v.addElement(persistentState.getProperty("Cost"));
        v.addElement(persistentState.getProperty("BarcodePrefix"));

        return v;
    }
     
    public void dataEntryView()
    {
        //AddTreeTypeTransactionView treeTypeView = new AddTreeTypeTransactionView(this);
    }
    @Override
    //--------------------------------------------------------------------------
    protected void initializeSchema(String tableName) 
    {
        if (mySchema == null)
	{
            mySchema = getSchemaInfo(tableName);
	}
    }  
    
    
}
