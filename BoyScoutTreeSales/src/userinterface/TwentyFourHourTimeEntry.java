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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author mike
 */
public class TwentyFourHourTimeEntry extends TimeEntry {
    
    TextField timeField;
    
    public TwentyFourHourTimeEntry() {
        super();
    }

    @Override
    protected void createContent() {
        timeField = new TextField();
        timeField.setPrefWidth(100);
        timeField.setPromptText("12:00");
        
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPrefWidth(200);
        container.getChildren().addAll(timeField);
        
        this.getChildren().add(container);
    }

    @Override
    public String getTime() {
        String timeInput = timeField.getText().trim();
        try {
            LocalTime convTime = LocalTime.parse(timeInput, standardFormat);
            
            return convTime.format(standardFormat);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setTime(LocalTime value) {
        timeField.setText(value.format(standardFormat));
    }
    
    

    @Override
    public void setTime(String time) {
        timeField.setText(time);
    }
    
    @Override
    public void requestFocus() {
        timeField.requestFocus();
    }
}
