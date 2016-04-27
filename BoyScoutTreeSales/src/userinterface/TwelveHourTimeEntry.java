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
    protected static final String TIME_FORMAT = "^(0?[1-9]|1[0-2]):[0-6][0-9]";
    
    protected TextField timeField;
    protected ComboBox amPmSelectBox;

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
    public LocalTime getTime() {
        String timeInput = timeField.getText().trim();
        if (timeInput.matches(TIME_FORMAT)) {
            timeInput += " " + (String) amPmSelectBox.getValue();
            LocalTime parsedTime = LocalTime.parse(
                    timeInput, DateTimeFormatter.ofPattern("h:mm a"));
            return parsedTime;
        }
        return null;
    }

    @Override
    public void setTime(LocalTime value) {
        String time = value.format(DateTimeFormatter.ofPattern("h:mm"));
        String amPm = value.format(DateTimeFormatter.ofPattern("a"));
        
        timeField.setText(time);
        amPmSelectBox.setValue(amPm);
    }
    
    @Override
    public void requestFocus() {
        timeField.requestFocus();
    }
}
