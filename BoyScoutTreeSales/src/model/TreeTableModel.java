package model;

import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTableModel
{
    private final SimpleStringProperty BarCode;
    private final SimpleStringProperty TreeType;
    private final SimpleStringProperty SalePrice;
    private final SimpleStringProperty Notes;
    private final SimpleStringProperty Status;
        
	//----------------------------------------------------------------------------
    public TreeTableModel(Vector<String> treeData)
    {
        BarCode =  new SimpleStringProperty(treeData.elementAt(0));
        TreeType =  new SimpleStringProperty(treeData.elementAt(1));
        SalePrice =  new SimpleStringProperty(treeData.elementAt(2));
        Notes =  new SimpleStringProperty(treeData.elementAt(3));
        Status =  new SimpleStringProperty(treeData.elementAt(4));
    }

	//----------------------------------------------------------------------------
    public String getBarCode() {
        return BarCode.get();
    }

	//----------------------------------------------------------------------------
    public void setBarCode(String barcode) {
        BarCode.set(barcode);
    }

	//----------------------------------------------------------------------------
    public String getTreeType() {
        return TreeType.get();
    }

	//----------------------------------------------------------------------------
    public void setTreeType(String name) {
        TreeType.set(name);
    }

    //----------------------------------------------------------------------------
    public String getSalePrice() {
        return SalePrice.get();
    }

    //----------------------------------------------------------------------------
    public void setSalePrice(String price) {
        SalePrice.set(price);
    }
    
    public String getNotes() {
        return Notes.get();
    }

    //----------------------------------------------------------------------------
    public void setNotes(String note) {
        Notes.set(note);
    }
    
    public String getStatus() {
        return Status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String status) {
        Status.set(status);
    }
}
