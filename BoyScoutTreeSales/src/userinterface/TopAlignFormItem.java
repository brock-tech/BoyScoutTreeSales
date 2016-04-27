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

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author mike
 */
public class TopAlignFormItem implements IFormItemStrategy {
    public static final double DEFAULT_WIDTH = 200;
    
    public TopAlignFormItem() { }

    @Override
    public Pane buildControl(String description, Node control) {
        VBox container = new VBox();
        container.setFillWidth(true);
        container.setAlignment(Pos.TOP_LEFT);
        container.setPrefWidth(DEFAULT_WIDTH);
        
        Label formLabel = new Label(description);
        formLabel.setTextAlignment(TextAlignment.LEFT);
        container.getChildren().add(formLabel);
        
        // Assume that the control has already been configured
        container.getChildren().add(control);
        
        return container;
    }    
}
