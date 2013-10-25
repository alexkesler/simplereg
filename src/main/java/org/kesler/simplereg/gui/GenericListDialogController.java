package org.kesler.simplereg.gui;

import java.util.List;

public interface GenericListDialogController<T> {
	public List<T> getAllItems();	

	public void openAddItemDialog();
	public void openEditItemDialog(int index);
	public void removeItem(int index);
}	

