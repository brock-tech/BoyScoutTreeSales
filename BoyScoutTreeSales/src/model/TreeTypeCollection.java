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
import java.util.Properties;
import java.util.Vector;

/**
 * 
 */
public class TreeTypeCollection extends EntityBase implements IView {
    private static final String myTableName = "Tree_Type";
    
    private Vector<TreeType> treeTypes;
    
    public TreeTypeCollection() {
        super(myTableName);
        
        treeTypes = new Vector<>();
    }

    @Override
    public Object getState(String key) {
        if (key.equals("TreeTypes")) {
            return treeTypes;
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
    
    private void addTreeType(TreeType t) {
        int index = findIndexToAdd(t);
        //System.out.println("index is " + index);
        treeTypes.insertElementAt(t, index);
    }
    
    public TreeType retrieve(String Id) {
        TreeType retValue = null;
        for (int cnt = 0; cnt < treeTypes.size(); cnt++) {
            TreeType nextType = treeTypes.elementAt(cnt);
            String nextTypeId = (String)nextType.getState("ID");
            if (nextTypeId.equals(Id)) {
                retValue = nextType;
                break;
            }
        }
        return retValue;
    }    

    
    private int findIndexToAdd(TreeType type) {
        int low = 0;
        int high = treeTypes.size()-1;
        int middle;
        
        while (low <= high) {
            middle = (low + high) / 2;
            TreeType midType = treeTypes.elementAt(middle);

            int result = TreeType.compare(type, midType);

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

    public void lookupTreeTypesByBarcode(String barcodePrefix) 
            throws InvalidPrimaryKeyException {
        
        String query = "SELECT * FROM "+myTableName
                +" WHERE (BarcodePrefix LIKE '%"+barcodePrefix+"%')";
        Vector allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            treeTypes = new Vector<>();
            System.out.println(allDataRetrieved.size() + " amount retrieved");
            for (Object nextTreeTypeData : allDataRetrieved) {
                TreeType nextTreeType = new TreeType((Properties)nextTreeTypeData);
                
                addTreeType(nextTreeType);
            }
        }
        else {
            throw new InvalidPrimaryKeyException(
                    String.format("No Tree Types found with barcode prefix like '%s',"
                            + " and Last name like '%s'",
                    barcodePrefix));
        }
    }
    
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(myTableName);
        }
    }
}
