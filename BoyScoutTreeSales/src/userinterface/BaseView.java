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
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 *
 */
public abstract class BaseView extends View {
    public static final double DEFAULT_WIDTH = 500.0;
    
    protected Locale myLocale;
    protected Properties myResources;
    
    protected MessageView statusLog;
    
    /**
     * 
     * @param model
     * @param classname
     */
    //----------------------------------------------------------
    protected BaseView(IModel model, String classname) {
        super(model, classname);
        
        myLocale = SystemLocale.getInstance();
        myResources = new Properties();
        loadResources("userinterface.i18n.BaseView");
        
        try {
            loadResources("userinterface.i18n." + classname);
        }
        catch (Exception exc) {
            /* Resource file not found or no additional resources needed */ 
        }
        
        VBox container = new VBox(10);
        container.setPrefWidth(DEFAULT_WIDTH);
        container.setBackground(Background.EMPTY);
        
        container.getChildren().add(createHeader());
        container.getChildren().add(createContent());
        container.getChildren().add(createFooter());
        
        getChildren().add(container);
        
        myModel.subscribe("TransactionError", this);
    }
    
    /**
     * Loads resources from the given file using the current Locale
     * 
     * @param resourceFile
     */
    //----------------------------------------------------------
    protected void loadResources(String resourceFile) {
        ResourceBundle resources = ResourceBundle.getBundle(resourceFile, myLocale);

        if (resources == null) {
            return;
        }

        Enumeration<String> keys = resources.getKeys();
        while (keys.hasMoreElements()) {
            String nextKey = keys.nextElement();
            myResources.setProperty(nextKey, resources.getString(nextKey));
        }
    }
    
    /**
     * Creates the BaseView header
     */
    //----------------------------------------------------------
    protected Node createHeader() {
        HBox titleContainer = new HBox(10);
        titleContainer.setPrefSize(DEFAULT_WIDTH, 40.0);
        titleContainer.setAlignment(Pos.CENTER);
        
        Text titleText = new Text(myResources.getProperty("titleText"));
        titleText.setFont(Font.font("SansSerif", FontWeight.BOLD, 20.0));
        titleText.setFill(Color.web("GREEN"));
        titleText.setTextAlignment(TextAlignment.CENTER);
        
        titleContainer.getChildren().add(titleText);
        return titleContainer;
    }
    
    /**
     * Creates the form content
     * @return Screen content
     */
    //----------------------------------------------------------
    protected abstract Node createContent();
    
    /**
     * 
     */
    //----------------------------------------------------------
    protected Node createFooter() {
        VBox footer = new VBox(10);
        footer.setAlignment(Pos.BASELINE_LEFT);
        footer.setPrefWidth(DEFAULT_WIDTH);
        
        statusLog = new MessageView("");
        footer.getChildren().add(statusLog);
        
        Text copyrightText = new Text(myResources.getProperty("copyrightText"));
        copyrightText.setFont(Font.font("SansSerif", FontWeight.NORMAL, 12.0));
        copyrightText.setFill(Color.web("GRAY"));
        copyrightText.setTextAlignment(TextAlignment.LEFT);
        footer.getChildren().add(copyrightText);
                
        return footer;
    }
    
    /**
     * 
     * @param msg
     */
    //----------------------------------------------------------
    public void displayMessage(String msg) {
        statusLog.displayMessage(msg);
    }
    
    /**
     * 
     * @param msg
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String msg) {
        statusLog.displayErrorMessage(msg);
    }
    
    /**
     * 
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }
}
