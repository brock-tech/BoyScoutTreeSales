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
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 *
 * @author mike
 */
public class SelectAdminActionView extends BaseView {
    
    Button openShiftButton;
    Button closeShiftButton;

    public SelectAdminActionView(IModel model) {
        super(model, "SelectShiftActionView");
    }

    @Override
    protected Node createContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
