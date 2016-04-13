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
                
            case "AddTreeTypeTransactionView":
                return new AddTreeTypeFormView(model);
             
            case "EditTreeTypeTransactionView":
                return new EditTreeTypeFormView(model);
            
            case "TreeTypeDataView":
                    return new AddTreeTypeFormView(model);
          
            case "EditTreeActionView":
                return new EditTreeActionView(model);
                
            case "EditScoutTransactionView":
                return new EditScoutTransactionView(model);
                
            case "ScoutDataView":
                return new ScoutFormView(model);

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
