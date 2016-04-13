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
            
            case "TreeTransactionView":
                return new TreeTransactionView(model);
                
            case "AddTreeTypeTransactionView":
                return new AddTreeTypeFormView(model);
             
            case "EditTreeTypeTransactionView":
<<<<<<< HEAD
                return new EditTreeTypeFormView(model);
            
            case "TreeTypeDataView":
                    return new AddTreeTypeFormView(model);
          
=======
                return new EditTreeTypeFormView(model);        
            
>>>>>>> e3c97e31a95a480907a247dd7d57e2d0fe95502e
            case "EditTreeActionView":
                return new EditTreeActionView(model);
            
            case "EditScoutTransactionView":
                return new EditScoutTransactionView(model);
            
            case "ScoutDataView":
                return new ScoutFormView(model);
            
            case "TreeDataView":
                return new TreeTransactionView(model);
                
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
