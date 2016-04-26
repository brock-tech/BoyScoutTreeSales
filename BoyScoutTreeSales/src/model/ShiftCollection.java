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

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author mike
 */
public class ShiftCollection extends EntityBase {
    private static final String myTableName = "Shift";
    
    private Vector<Shift> shifts;

    public ShiftCollection() {
        super(myTableName);
        
        shifts = new Vector<>();
    }
    
    public void addShift(Shift s) {
        shifts.add(s);
    }
    
    public Shift retrieveByScoutId(String scoutId) {
        for (Shift s : shifts) {
            String shiftScoutId = (String)s.getState("ScoutId");
            if (shiftScoutId.equals(scoutId)) {
                return s;
            }
        }
        return null;
    }
    
    public void deleteByScoutId(String scoutId) {
        Iterator<Shift> i = shifts.iterator();
        while (i.hasNext()) {
            Shift nextShift = i.next();
            if (nextShift.getState("ScoutID").equals(scoutId)) {
                i.remove();
            }
        }
    }

    @Override
    public Object getState(String key) {
        if ("Shifts".equals(key)) {
            return shifts;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(myTableName);
        }
    }
    
}
