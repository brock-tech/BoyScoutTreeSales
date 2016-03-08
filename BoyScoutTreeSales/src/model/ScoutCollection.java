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

import impresario.IView;
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
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        
    }

    @Override
    protected void initializeSchema(String tableName) {
        
    }

    @Override
    public void updateState(String key, Object value) {
        
    }
}
