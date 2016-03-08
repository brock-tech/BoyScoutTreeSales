package userinterface;

import impresario.IModel;
import java.util.Vector;

//==============================================================================
public class ViewFactory {

    public static View createView(String viewName, IModel model) {
        switch (viewName) {
            case "TreeLotCoordinatorView":
                return new TreeLotCoordinatorView(model);

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
