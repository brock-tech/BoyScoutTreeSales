package userinterface;

import impresario.IModel;
import java.util.Vector;

//==============================================================================
public class ViewFactory {

    public static View createView(String viewName, IModel model) {
        switch (viewName) {
            case "TreeLotCoordinatorView":
                return new TreeLotCoordinatorView(model);
                
            case "SelectAdminActionView":
                return new SelectAdminActionView(model);
                
            case "RegisterScoutTransactionView":
                return new ScoutFormView(model);
            
            case "AddTreeTransactionView":
                return new AddTreeTransactionView(model);
                
            case "EnterTreeBarcodeView":
                return new EnterTreeBarcodeView(model);
                
            case "AddTreeTypeTransactionView":
                return new AddTreeTypeFormView(model);
             
            case "EditTreeTypeTransactionView":
                return new EditTreeTypeFormView(model);   
            case "TreeTypeDataView":
                return new AddTreeTypeFormView(model);
            case "EditTreeTransactionView":
                return new EditTreeTransactionView(model);
            
            case "EditScoutTransactionView":
                return new EditScoutTransactionView(model);
            
            case "CloseShiftTransactionView":
                return new CloseSessionTransactionView(model);
                
            case "ScoutDataView":
                return new ScoutFormView(model);
                
            case "OpenSessionTransactionView":
                return new OpenSessionTransactionView(model);
                
            case "OpenShiftLookupView":
                return new OpenShiftLookupView(model);
                
            case "OpenShiftFormView":
                return new OpenShiftFormView(model);
                
            default:
                return null;
        }
    }

    public static Vector createVectorView(String viewName, IModel model) {
        switch (viewName) {
            default:
                return null;
        }
    }
}