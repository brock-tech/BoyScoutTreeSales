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

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author mike
 */
public interface IFormItemStrategy {
    public Pane buildControl(String description, Node control);
}
