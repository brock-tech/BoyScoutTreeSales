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

import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mike
 */
public class ScoutTableModel {
    private final SimpleStringProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty memberId;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty email;
    private final SimpleStringProperty status;
    private final SimpleStringProperty dateStatusUpdated;
    
    public ScoutTableModel(Vector<String> scoutData) {
        id = new SimpleStringProperty(scoutData.elementAt(0));
        firstName = new SimpleStringProperty(scoutData.elementAt(1));
        middleName = new SimpleStringProperty(scoutData.elementAt(2));
        lastName = new SimpleStringProperty(scoutData.elementAt(3));
        memberId = new SimpleStringProperty(scoutData.elementAt(4));
        dateOfBirth = new SimpleStringProperty(scoutData.elementAt(5));
        phoneNumber = new SimpleStringProperty(scoutData.elementAt(6));
        email = new SimpleStringProperty(scoutData.elementAt(7));
        status = new SimpleStringProperty(scoutData.elementAt(8));
        dateStatusUpdated = new SimpleStringProperty(scoutData.elementAt(9));
    }

    public String getId() {
        return id.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getMemberId() {
        return memberId.get();
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getDateStatusUpdated() {
        return dateStatusUpdated.get();
    }
    
    
}
