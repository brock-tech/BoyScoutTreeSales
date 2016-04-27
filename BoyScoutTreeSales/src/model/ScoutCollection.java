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
import impresario.IView;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * 
 */
public class ScoutCollection extends EntityBase implements IView {
    private static final String myTableName = "Scout";
    
    private Vector<Scout> scouts;
    
    public ScoutCollection() {
        super(myTableName);
        
        scouts = new Vector<>();
    }

    @Override
    public Object getState(String key) {
        if (key.equals("Scouts")) {
            return scouts;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    
    private int findIndexToAdd(Scout s) {
        int low = 0;
        int high = scouts.size()-1;
        int middle;
        
        while (low <= high) {
            middle = (low + high) / 2;
            Scout midScout = scouts.elementAt(middle);

            int result = Scout.compare(s, midScout);

            if (result == 0) {
                return middle;
            } else if (result < 0) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return low;
    }
    
    public void addScout(Scout s) {
        int index = findIndexToAdd(s);
        scouts.insertElementAt(s, index);
    }
    
    public void removeScout(String id) {
        Iterator<Scout> i = scouts.iterator();
        while (i.hasNext()) {
            if (i.next().getState("ID").equals(id)) {
                i.remove();
            }
        }
    }
    
    public Scout retrieve(String id) {
        Scout retValue = null;
        for (Scout s : scouts) {
            String nextScoutId = (String)s.getState("ID");
            if (id.equals(nextScoutId)) {
                retValue = s;
                break;
            }
        }
        return retValue;
    }
    
    public void lookupScoutsByName(String firstName, String lastName) 
            throws InvalidPrimaryKeyException {
        
        String query = "SELECT * FROM "+myTableName
                +" WHERE (FirstName LIKE '%"+firstName
                +"%') AND (LastName LIKE '%"+lastName+"%')"
                +"AND (Status != 'Inactive')";
        
        Vector allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            scouts = new Vector<>();
            
            for (Object nextScoutData : allDataRetrieved) {
                Scout nextScout = new Scout((Properties)nextScoutData);
                
                addScout(nextScout);
            }
        }
        else {
            throw new InvalidPrimaryKeyException(
                    String.format("No Scouts found with First name like '%s',"
                            + " and Last name like '%s'",
                    firstName,
                    lastName));
        }
    }
    
    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(myTableName);
        }
    }
}
