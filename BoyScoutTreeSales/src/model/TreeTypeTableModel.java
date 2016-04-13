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
        private final SimpleStringProperty ID;
	private final SimpleStringProperty BarcodePrefix;
	private final SimpleStringProperty TypeDescription;
	private final SimpleStringProperty Cost;
	//----------------------------------------------------------------------------
	public TreeTypeTableModel(Vector<String> treeTypeData)
	{
		ID =  new SimpleStringProperty(treeTypeData.elementAt(0));
		BarcodePrefix =  new SimpleStringProperty(treeTypeData.elementAt(1));
		TypeDescription =  new SimpleStringProperty(treeTypeData.elementAt(2));
		Cost =  new SimpleStringProperty(treeTypeData.elementAt(3));
	}
//------------------------Getters---------------------------------------------------
	public SimpleStringProperty getTreeTypeID() 
	{
		return ID;
	}
	public SimpleStringProperty getBarcodePrefix() 
	{
		return BarcodePrefix;
	}
	public SimpleStringProperty getDescription() 
	{
		return TypeDescription;
	}
	public SimpleStringProperty getCost() 
	{
		return Cost;
	}
	
	public void setTreeTypeID(String c) 
	{
		ID.set(c);;
	}
	public void setBarcodePrefix(String sc) 
	{
		BarcodePrefix.set(sc);;
	}
	public void setDescription(String z)
	{
		TypeDescription.set(z);
	}
	public void setCost(String e) 
	{
		Cost.set(e);
	}

}



