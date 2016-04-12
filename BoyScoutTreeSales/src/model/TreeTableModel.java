package model;

import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTableModel
{
    private final SimpleStringProperty BarCode;
    private final SimpleStringProperty TreeType;
    private final SimpleStringProperty SalePrice;
    private final SimpleStringProperty CName;
    private final SimpleStringProperty CPhoneNum;
    private final SimpleStringProperty Cemail;
    private final SimpleStringProperty DateStatusUpdated;
    private final SimpleStringProperty TimeStatusUpdated;
        
	//----------------------------------------------------------------------------
    public TreeTableModel(Vector<String> treeData)
    {
        BarCode =  new SimpleStringProperty(treeData.elementAt(0));
        TreeType =  new SimpleStringProperty(treeData.elementAt(1));
        SalePrice =  new SimpleStringProperty(treeData.elementAt(2));
        CName =  new SimpleStringProperty(treeData.elementAt(3));
        CPhoneNum =  new SimpleStringProperty(treeData.elementAt(4));
        Cemail =  new SimpleStringProperty(treeData.elementAt(5));
        DateStatusUpdated =  new SimpleStringProperty(treeData.elementAt(6));
        TimeStatusUpdated =  new SimpleStringProperty(treeData.elementAt(7));
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

    //----------------------------------------------------------------------------
    public String getCName() {
        return CName.get();
    }

		//----------------------------------------------------------------------------
    public void setCName(String name) {
        CName.set(name);
    }

		//----------------------------------------------------------------------------
    public String getCPhoneNum() {
        return CPhoneNum.get();
    }

    //----------------------------------------------------------------------------
    public void setCPhoneNum(String phone) {
        CPhoneNum.set(phone);
    }

    public String getCemail() {
        return CPhoneNum.get();
    }

    //----------------------------------------------------------------------------
    public void setCemail(String mail) {
        CPhoneNum.set(mail);
    }
    
    public String getDateStatusUpdated() {
        return DateStatusUpdated.get();
    }

    //----------------------------------------------------------------------------
    public void setDateStatusUpdated(String date) {
        DateStatusUpdated.set(date);
    }
    
    public String getTimeStatusUpdated() {
        return TimeStatusUpdated.get();
    }

    //----------------------------------------------------------------------------
    public void setTimeStatusUpdated(String time) {
        TimeStatusUpdated.set(time);
    }
}
