package org.kesler.simplereg.gui;

import java.util.List;

public interface GenericListDialogController<T> {
	
	// public List<T> getAllItems();	
	public boolean openAddItemDialog();
	public boolean openEditItemDialog(int index);
	public boolean removeItem(int index);
	
	public void readItems();
}	

