/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import impresario.IModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author mike
 */
public class CloseSessionTransactionView extends BaseView {    
    protected Button confirmButton;
    protected Button denyButton;

    public CloseSessionTransactionView(IModel model) {
        super(model, "CloseSessionTransactionView");
    }
    
    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        IModel session = (IModel) myModel.getState("SessionToClose");
        
        
        Text promptText = new Text(myResources.getProperty("promptText"));
        promptText.setTextAlignment(TextAlignment.CENTER);
        
        HBox promptContainer = new HBox();
        promptContainer.setAlignment(Pos.CENTER);
        promptContainer.getChildren().add(promptText);
        
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50d);
        col1.setHalignment(HPos.RIGHT);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50d);
        col2.setHalignment(HPos.LEFT);
        
        grid.getColumnConstraints().addAll(col1, col2);
        
        LocalDate startDate = LocalDate.parse(
                (String)session.getState("StartDate"),
                DateTimeFormatter.ISO_LOCAL_DATE);
        String dateFormat = myResources.getProperty("dateFormat");
        Label dateLabel = new Label(myResources.getProperty("startDate"));
        Label dateValueLabel = new Label(startDate.format(
                DateTimeFormatter.ofPattern(dateFormat)));
        grid.add(dateLabel, 0, 0);
        grid.add(dateValueLabel, 1, 0);
        
        LocalTime startTime = LocalTime.parse(
                (String)session.getState("StartTime"),
                DateTimeFormatter.ISO_LOCAL_TIME);
        String timeFormat = myResources.getProperty("timeFormat");
        Label startTimeLabel = new Label(myResources.getProperty("startTime"));
        Label startTimeValueLabel = new Label(
                startTime.format(DateTimeFormatter.ofPattern(timeFormat)));
        grid.add(startTimeLabel, 0, 1);
        grid.add(startTimeValueLabel, 1, 1);
        
        LocalTime endTime = LocalTime.parse(
                (String)session.getState("EndTime"),
                DateTimeFormatter.ISO_LOCAL_TIME);
        Label endTimeLabel = new Label(myResources.getProperty("endTime"));
        Label endTimeValueLabel = new Label(
                endTime.format(DateTimeFormatter.ofPattern(timeFormat)));
        grid.add(endTimeLabel, 0, 2);
        grid.add(endTimeValueLabel, 1, 2);
        
        String cashFormat = myResources.getProperty("cashFormat");
        Label startCashLabel = new Label(myResources.getProperty("startCash"));
        Label startCashValueLabel = new Label(String.format(cashFormat,
                (String)session.getState("StartingCash")));
        grid.add(startCashLabel, 0, 3);
        grid.add(startCashValueLabel, 1, 3);
        
        Label endCashLabel = new Label(myResources.getProperty("startCash"));
        Label endCashValueLabel = new Label(String.format(cashFormat,
                (String)session.getState("EndingCash")));
        grid.add(endCashLabel, 0, 4);
        grid.add(endCashValueLabel, 1, 4);
        
        Label checkTotalLabel = new Label(myResources.getProperty("checkTotal"));
        Label checkTotalValueLabel = new Label(String.format(cashFormat,
                (String)session.getState("TotalCheckTransactionsAmount")));
        grid.add(checkTotalLabel, 0, 5);
        grid.add(checkTotalValueLabel, 1, 5);
        
        confirmButton = new Button(myResources.getProperty("confirm"));
        confirmButton.setOnAction(submitHandler);
        confirmButton.setPrefWidth(100);
        
        denyButton = new Button(myResources.getProperty("deny"));
        denyButton.setOnAction(submitHandler);
        denyButton.setPrefWidth(100);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(confirmButton, denyButton);
        
        VBox content = new VBox(10);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(promptContainer, grid, buttonContainer);
        
        return content;
    }
    
    private void processAction(ActionEvent event) {
        Object source = event.getSource();
        if (source == confirmButton) {
            myModel.stateChangeRequest("Confirm", null);
        }
        else if (source == denyButton) {
            myModel.stateChangeRequest("Cancel", null);
        }
    }
    
    @Override
    public void updateState(String key, Object value) {
        
    }
}
