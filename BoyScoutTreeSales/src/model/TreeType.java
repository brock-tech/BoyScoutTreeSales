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
import java.util.Enumeration;



/**
 *
 */
public class TreeType extends EntityBase 
{
    private static final String myTableName = "TreeType";
    
    protected Properties dependencies;
    private String updateStatusMessage = "";
    protected TreeLotCoordinator myTLC;
    //--------------------------------------------------------------------------
    public TreeType(TreeLotCoordinator tlc)
    {
       //to navigate back from Add/Modify TreeType GUI
        super(myTableName);
        myTLC = tlc;
    }
    //--------------------------------------------------------------------------
    public TreeType(String typeId, TreeLotCoordinator tlc)
            throws InvalidPrimaryKeyException
    {
        this(typeId);
	myTLC = tlc;
    }
    //--------------------------------------------------------------------------
    public TreeType(String typeId) throws InvalidPrimaryKeyException
    {
	super(myTableName);
        setDependencies();

        String query = "SELECT * FROM " + myTableName + 
        " WHERE (typeId = " + typeId + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

	// You must get one Patron at least
	if (allDataRetrieved != null)
	{
            int size = allDataRetrieved.size();
                if (size != 1)
		{
                    throw new InvalidPrimaryKeyException("Multiple TreeTypes matching id : "
                    + typeId + " found.");
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
            throw new InvalidPrimaryKeyException("No TreeType matching id : "
            + typeId + " found.");
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
        try
        {
            if (persistentState.getProperty("typeId") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("typeId",
                persistentState.getProperty("typeId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "TreeType data for TreeType with ID : " +
                persistentState.getProperty("typeId") + " updated successfully in database";
            }
            else
            {
                Integer patronID =
                insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("typeId", "" + patronID.intValue());
                updateStatusMessage = "TreeType data for new TreeType : " +  persistentState.getProperty("typeId")
                + "installed successfully in database!";
            }
        }   
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing TreeType data in database!";
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
        String aNum = (String)a.getState("typeId");
        String bNum = (String)b.getState("typeId");

        return aNum.compareTo(bNum);
    }
   //--------------------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) 
    {
        myRegistry.updateSubscribers(key, this);
    }
    //--------------------------------------------------------------------------
     public Vector<String> getTableListView() 
     {
        Vector<String> v = new Vector<>();
        v.addElement(persistentState.getProperty("typeId"));
	v.addElement(persistentState.getProperty("cost"));
	v.addElement(persistentState.getProperty("description"));
        
        return v;
    }
     
    public void dataEntryView()
    {
        AddTreeTypeTransactionView treeTypeView = new AddTreeTypeTransactionView(this);
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
