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

import exception.InvalidPrimaryKeyException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author mike
 */
public class RemoveTreeTransaction extends EditTreeTransaction {
    
    public RemoveTreeTransaction() {
        super();
    }
    
    @Override
    protected void getMessagesBundle() {
        myMessages = ResourceBundle.getBundle("model.i18n.TreeSaleTransaction", myLocale);
    }
    
    @Override
    protected void processBarcode(String bc) {
        try {
            selectedTree = new Tree(bc);

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, String.format(
                    "Are you sure you want to delete '%s' ?", bc));
            confirmDialog.setWidth(500);
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedTree.delete();
            }
            updateStatusMessage = (String)selectedTree.getState("UpdateStatusMessage");
 
        } catch (InvalidPrimaryKeyException ex) {
            updateStatusMessage = myMessages.getString("treeNotFoundMsg");
        }
    }
    
}
