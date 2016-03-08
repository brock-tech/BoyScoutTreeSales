//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************

import event.Event;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.TreeLotCoordinator;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;

/**
 *
 * @author mike
 */
public class BoyScoutTreeSales extends Application {
    private TreeLotCoordinator myCoordinator;
    
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("");
        System.out.println("");
        
        MainStageContainer.setStage(primaryStage, "Tree Sales 1.00");
        mainStage = MainStageContainer.getInstance();
        
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
        });
        
        try {
            myCoordinator = new TreeLotCoordinator();
        }
        catch (Exception exc) {
            System.err.println("TreeLotCoordinator- could not create TeeLotCoorinator");
            new Event(Event.getLeafLevelClassName(this),
            "TreeLotCoordinator.<init>",
            "Unable to create TreeLotCoordinator object",
            Event.ERROR);
            exc.printStackTrace();
        }
        
        WindowPosition.placeCenter(mainStage);
        
        mainStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
