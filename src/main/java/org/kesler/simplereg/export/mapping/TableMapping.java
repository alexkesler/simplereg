package org.kesler.simplereg.export.mapping;

public interface TableMapping {

    public int getColumnCount();
    public int getRowCount();
    public String getValue(int row, int column);
}
