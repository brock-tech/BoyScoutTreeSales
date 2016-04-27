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

import impresario.IModel;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mike
 */
public class ScoutListModel {
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    
    public ScoutListModel(IModel scout) {
        String firstName = (String)scout.getState("FirstName");
        String lastName = (String)scout.getState("LastName");
        
        name = new SimpleStringProperty(String.format("%1$s %2$s", firstName, lastName));
        id = new SimpleStringProperty((String)scout.getState("ID"));
    }
    
    public String getId() {
        return id.get();
    }
    
    public String getName() {
        return name.get();
    }
    
    @Override 
    public String toString() {
        return name.get();
    }
}
