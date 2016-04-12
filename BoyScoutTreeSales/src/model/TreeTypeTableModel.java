//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
package model;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTypeTableModel
{
        private final SimpleStringProperty treeTypeID;
	private final SimpleStringProperty barcodePrefix;
	private final SimpleStringProperty description;
	private final SimpleStringProperty cost;
	//----------------------------------------------------------------------------
	public TreeTypeTableModel(Vector<String> treeTypeData)
	{
		treeTypeID =  new SimpleStringProperty(treeTypeData.elementAt(0));
		barcodePrefix =  new SimpleStringProperty(treeTypeData.elementAt(1));
		description =  new SimpleStringProperty(treeTypeData.elementAt(2));
		cost =  new SimpleStringProperty(treeTypeData.elementAt(3));
	}
//------------------------Getters---------------------------------------------------
	public SimpleStringProperty getTreeTypeID() 
	{
		return treeTypeID;
	}
	public SimpleStringProperty getBarcodePrefix() 
	{
		return barcodePrefix;
	}
	public SimpleStringProperty getDescription() 
	{
		return description;
	}
	public SimpleStringProperty getCost() 
	{
		return cost;
	}
	
	public void setTreeTypeID(String c) 
	{
		treeTypeID.set(c);;
	}
	public void setBarcodePrefix(String sc) 
	{
		barcodePrefix.set(sc);;
	}
	public void setDescription(String z)
	{
		description.set(z);
	}
	public void setCost(String e) 
	{
		cost.set(e);
	}

}



