/*
 * 
 * COPYRIGHT 2016
 *   College at Brockport, State University of New York.
 *   ALL RIGHTS RESERVED
 * 
 * This file is the product of The College at Brockport and cannot
 * be reproduced, copied, or used in any shape or form without
 * the express written consent of The College at Brockport.
 */
package userinterface;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 * @author mike
 */
public class TopAlignStandardCurrencyItem implements IFormItemStrategy {
    String currencyPrefix;
    
    @Override
    public Pane buildControl(String description, Node control) {
        if (currencyPrefix == null) {
            Locale sysLocale = SystemLocale.getInstance();
            ResourceBundle rb = ResourceBundle.getBundle("userinterface.i18n.BaseView", sysLocale);
            currencyPrefix = rb.getString("currencyPrefix");
        }
        
        HBox layout = new HBox(5);
        layout.setAlignment(Pos.CENTER_LEFT);
        
        Text currencyPrefixText = new Text(currencyPrefix);
        layout.getChildren().addAll(currencyPrefixText, control);
        
        if (description != null) {
            TopAlignFormItem tafi = new TopAlignFormItem();
            return tafi.buildControl(description, layout);
        }
        return layout;
    }
}
