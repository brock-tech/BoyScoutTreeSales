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

/**
 *
 * @author mike
 */
public class FormItemFactory {
    public static IFormItemStrategy getFormItem(String id) {
        if (id.equals("DefaultFormItem")) {
            return new TopAlignFormItem();
        }
        if (id.equals("TopAlignFormItem")) {
            return new TopAlignFormItem();
        }
        return null;
    }
}
