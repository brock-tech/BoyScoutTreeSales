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

/**
 * 
 */
public class Tree extends EntityBase {
    private static final String myTableName = "Tree";

    public Tree(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);
        
        
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
    
    public boolean isAvailable() {
        String status = persistentState.getProperty("treeStatus");
        return status.equals("Available");
    }
    
}
