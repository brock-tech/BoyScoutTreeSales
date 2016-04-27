//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
package userinterface;

import java.time.LocalTime;

/**
 *
 * @author mike
 */
public abstract class TimeEntry extends javafx.scene.Group {
    
    protected TimeEntry() {
        super();
        createContent();
    }
    
    protected abstract void createContent();
    public abstract LocalTime getTime();
    public abstract void setTime(LocalTime value);
}
