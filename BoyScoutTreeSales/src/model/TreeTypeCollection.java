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
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author mike
 */
public class TreeTypeCollection extends EntityBase {
    private static String myTableName = "TreeType";
    
    private Vector<TreeType> treeTypes;
    
    public TreeTypeCollection() {
        super(myTableName);
        treeTypes = new Vector<>();
    }
    
    public void findTypesWithBarcodePrefix(String barcodePrefix)
        throws InvalidPrimaryKeyException {
        String query = String.format(
                "SELECT * FROM %s WHERE barcodePrefix = %s",
                myTableName, 
                barcodePrefix
        );
        
        Vector allDataRetrieved = getSelectQueryResult(query);
        
        if (allDataRetrieved != null) {
            treeTypes = new Vector<>();
            
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextTypeData = (Properties)allDataRetrieved.elementAt(cnt);
                
                TreeType type = new TreeType(nextTypeData);
                if (type != null) {
                    addTreeType(type);
                }
            }
        }
    }
    
    public TreeType retrieve(String typeId) {
        TreeType retValue = null;
        for (int cnt = 0; cnt < treeTypes.size(); cnt++) {
            TreeType nextType = treeTypes.elementAt(cnt);
            String nextTypeId = (String)nextType.getState("typeId");
            if (nextTypeId.equals(typeId)) {
                retValue = nextType;
                break;
            }
        }
        return retValue;
    }
    
    private void addTreeType(TreeType t) {
        treeTypes.add(t);
    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        
    }
    
    @Override
    protected void initializeSchema(String tableName) {
        
    }
    
}
