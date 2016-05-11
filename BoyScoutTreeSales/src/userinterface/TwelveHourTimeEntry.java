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
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author mike
 */
public class TwelveHourTimeEntry extends TimeEntry {
    protected final DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("h:mm a");
    
    protected TextField timeField;
    protected ComboBox amPmSelectBox;
    
    public TwelveHourTimeEntry() {
        super();
    }

    @Override
    protected void createContent() {
        timeField = new TextField();
        timeField.setPrefWidth(100);
        timeField.setPromptText("12:00");
        
        ObservableList<String> options = FXCollections.observableArrayList(
                "AM",
                "PM"
        );
        amPmSelectBox = new ComboBox(options);
        amPmSelectBox.setValue("AM");
        amPmSelectBox.setPrefWidth(100);
        
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPrefWidth(300);
        container.setPadding(new Insets(5, 5, 5, 5));
        container.getChildren().addAll(timeField, amPmSelectBox);
        
        this.getChildren().add(container);
    }
    
    @Override
    public String getTime() {
        String timeInput = String.format("%1$s %2$s",
                timeField.getText().trim(),
                (String)amPmSelectBox.getValue());
        try {
            LocalTime convertedTime = LocalTime.parse(timeInput, displayFormat);
            
            return convertedTime.format(standardFormat);
            
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setTime(LocalTime time) {
        String[] values = time.format(displayFormat).split(" ");
        
        timeField.setText(values[0]);
        amPmSelectBox.setValue(values[1]);
    }
    
    public void setTime(String time) {
        String convTime = LocalTime.parse(time, standardFormat).format(displayFormat);
        String[] values = convTime.split(" ");
        
        timeField.setText(values[0]);
        amPmSelectBox.setValue(values[1]);
    }
    
    @Override
    public void requestFocus() {
        timeField.requestFocus();
    }
}
