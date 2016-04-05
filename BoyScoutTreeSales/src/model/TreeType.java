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
    private static final String myTableName = "TreeType";
    
    protected Properties dependencies;
    private String updateStatusMessage = "";
    protected TreeLotCoordinator myTLC;
    private Locale myLocale;
    private ResourceBundle myMessages;

    //--------------------------------------------------------------------------
    public TreeType(TreeLotCoordinator tlc)
    {
       //to navigate back from Add/Modify TreeType GUI
        super(myTableName);
        myTLC = tlc;
    }
    //--------------------------------------------------------------------------
    public TreeType(String barcodePrefix, TreeLotCoordinator tlc)
            throws InvalidPrimaryKeyException
    {
        this(barcodePrefix);
	myTLC = tlc;
    }
    //--------------------------------------------------------------------------
    public TreeType(String barcodePrefix) throws InvalidPrimaryKeyException
    {
	super(myTableName);
        setDependencies();
        
        myLocale = SystemLocale.getInstance();
        myMessages = ResourceBundle.getBundle("model.i18n.TreeType", myLocale);
        MessageFormat formatter = new MessageFormat("", myLocale);
        
         String query = String.format(
                "SELECT * FROM %s WHERE (barcodePrefix = %s)",
                myTableName,
                barcodePrefix);
 
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

	// You must get one Patron at least
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
    //--------------------------------------------------------------------------
    TreeType(Properties props) 
    {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);
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
        LocalDateTime currentDate = LocalDateTime.now();
        String dateLastUpdate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        persistentState.setProperty("DateLastUpdate", dateLastUpdate);
        Object [] ID_barcodePrefix = new Object[] {persistentState.get("treeTypeId"),
                persistentState.get("barcodePrefix")};
        
        MessageFormat formatter = new MessageFormat("", myLocale);
        if (persistentState.getProperty("treeTypeID") != null)
        {
            try
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("treeTypeID",
                persistentState.getProperty("treeTypeID"));
                updatePersistentState(mySchema, persistentState, whereClause);
               formatter.applyPattern(myMessages.getString("insertSuccessMsg"));
               updateStatusMessage = formatter.format(ID_barcodePrefix);
            }
            catch(SQLException s)
            {
               formatter.applyPattern(myMessages.getString("insertErrorMsg"));
               updateStatusMessage = formatter.format(ID_barcodePrefix);
            }
        }   
        else
        {
            try
            {
                Integer treeTypeID =
                insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("treeTypeID", "" + treeTypeID.intValue());
               formatter.applyPattern(myMessages.getString("updateSuccessMsg"));
               updateStatusMessage = formatter.format(ID_barcodePrefix);
            }
            catch(SQLException e)
            {
               formatter.applyPattern(myMessages.getString("updateErrorMsg"));
               updateStatusMessage = formatter.format(ID_barcodePrefix);
            }
        }   
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    //--------------------------------------------------------------------------
    @Override
    public Object getState(String key) 
    {
       if (key.equals("UpdateStatusMessage") == true)
	return updateStatusMessage;

        return persistentState.getProperty(key);
    }
    //--------------------------------------------------------------------------
   public static int compare(TreeType a, TreeType b)
    {
        String aNum = (String)a.getState("barcodePrefix");
        String bNum = (String)b.getState("barcodePrefix");

        return aNum.compareTo(bNum);
    }
   //--------------------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) 
    {
        myRegistry.updateSubscribers(key, this);
    }
    //-------------------------------------------------------------------------
     public Vector<String> getTableListView() 
     {
        Vector<String> v = new Vector<>();
        v.addElement(persistentState.getProperty("treeTypeId"));
        v.addElement(persistentState.getProperty("barcodePrefix"));
        v.addElement(persistentState.getProperty("description"));
	v.addElement(persistentState.getProperty("cost"));
	
        
        return v;
    }
     
    public void dataEntryView()
    {
       // AddTreeTypeTransactionView treeTypeView = new AddTreeTypeTransactionView(this);
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
