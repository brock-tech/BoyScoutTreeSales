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

/**
 *
 * @author mike
 */
public class CurrentSession {
    private static Session _myInstance = null;
    
    private void instantiateSession() {
        
    }
    
    public Session getInstance() {
        if (_myInstance == null) {
            instantiateSession();
        }
        return _myInstance;
    }
}
