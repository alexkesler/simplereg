package org.kesler.simplereg.gui;


public interface GenericListDialogController<T> {
	
	boolean openAddItemDialog();
	boolean openEditItemDialog(T item);
	boolean removeItem(T item);

	void filterItems(String filter);
	
	void updateItems();
}	

