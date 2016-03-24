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

import java.util.Locale;

/**
 *
 * @author mike
 */
public class SystemLocale {
    private static Locale myInstance = null;
    
    private SystemLocale() { }
    
    public static void setLocale(String lang, String country) {
        myInstance = new Locale(lang, country);
    }
    
    public static Locale getInstance() {
        return myInstance;
    }
}
