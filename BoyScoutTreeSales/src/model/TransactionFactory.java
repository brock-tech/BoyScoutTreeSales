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
 */
public class TransactionFactory {
    public static Transaction createTransaction(String transName) {
        switch (transName) {
            case "AddScout":
                return new AddScoutTransaction();
            case "AddTreeType":
                  return new AddTreeTypeTransaction();
            
            default: return null;
        }
    }
}
