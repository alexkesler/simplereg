package org.kesler.simplereg.gui.util;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by alex on 21.04.16.
 */

public class CheckBoxHeaderRenderer implements TableCellRenderer {

    private final int targetColumnIndex;
    private CheckBoxIcon checkBoxIcon;
    private final CheckBoxIcon selectedCheckBoxIcon;
    private final CheckBoxIcon deselectedCheckBoxIcon;
    private final CheckBoxIcon indeterminateCheckBoxIcon;

    public CheckBoxHeaderRenderer(JTableHeader header, TableColumn tableColumn) {
        targetColumnIndex = tableColumn.getModelIndex();
//        System.out.println("Creating CheckBoxRenderer for column: " + targetColumnIndex);
        JCheckBox selectedCheckBox = new JCheckBox("",true);
        selectedCheckBox.setOpaque(false);
        selectedCheckBox.setFont(header.getFont());
        selectedCheckBoxIcon = new CheckBoxIcon(selectedCheckBox, Status.SELECTED);

        JCheckBox deselectedCheckBox = new JCheckBox("",false);
        selectedCheckBox.setOpaque(false);
        selectedCheckBox.setFont(header.getFont());
        deselectedCheckBoxIcon = new CheckBoxIcon(deselectedCheckBox, Status.DESELECTED);

        JCheckBox indeterminateCheckBox = new JCheckBox("",true);
        selectedCheckBox.setOpaque(false);
        selectedCheckBox.setFont(header.getFont());
        indeterminateCheckBox.setEnabled(false);
        indeterminateCheckBoxIcon = new CheckBoxIcon(indeterminateCheckBox, Status.INDETERMINATE);

        header.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = ((JTableHeader) e.getSource()).getTable();
                TableColumnModel columnModel = table.getColumnModel();
                int vci = columnModel.getColumnIndexAtX(e.getX());
                int mci = table.convertColumnIndexToModel(vci);
                if (mci == targetColumnIndex) {
                    TableColumn column = columnModel.getColumn(vci);
                    Object v = column.getHeaderValue();
                    boolean b = Status.DESELECTED.equals(v) ? true : false;
                    TableModel m = table.getModel();
//                    System.out.println("Mouse clicked, header value: " + column.getHeaderValue());

                    for (int i = 0; i < m.getRowCount(); i++) m.setValueAt(b, i, mci);
                    column.setHeaderValue(b ? Status.SELECTED : Status.DESELECTED);
//                    ((JTableHeader) e.getSource()).repaint();
                }
            }
        });

        final JTable table = header.getTable();
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
//                System.out.println("Table model updated, event: " + e.getType() + " column: " + e.getColumn());
                if(e.getType()==TableModelEvent.UPDATE && e.getColumn()==targetColumnIndex) {
                    int vci = table.convertColumnIndexToView(targetColumnIndex);
                    TableColumn column = table.getColumnModel().getColumn(vci);
                    if(!Status.INDETERMINATE.equals(column.getHeaderValue())) {
                        column.setHeaderValue(Status.INDETERMINATE);
                    } else {
                        boolean selected = true, deselected = true;
                        TableModel m = table.getModel();
                        for(int i=0; i < m.getRowCount(); i++) {
                            Boolean b = (Boolean)m.getValueAt(i, targetColumnIndex);
                            selected &= b; deselected &= !b;
                            if(selected==deselected) return;
                        }
                        if(selected) {
                            column.setHeaderValue(Status.SELECTED);
                        }else if(deselected) {
                            column.setHeaderValue(Status.DESELECTED);
                        }else{
                            return;
                        }
                    }
//                    System.out.println("Table model updated, header value: " + column.getHeaderValue());
//                    setCheckBoxStatus(column.getHeaderValue());
//                    JTableHeader h = table.getTableHeader();
//                    System.out.println("Repainting, icon: " + checkBoxIcon.getStatus());
//                    h.repaint(h.getHeaderRect(vci));
                }

            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isS, boolean hasF, int row, int col) {
        TableCellRenderer r = tbl.getTableHeader().getDefaultRenderer();
        JLabel l = (JLabel) r.getTableCellRendererComponent(tbl, val, isS, hasF, row, col);
        if(targetColumnIndex==tbl.convertColumnIndexToModel(col)) {
            setCheckBoxStatus(val);
        }
        l.setIcon(checkBoxIcon);
        l.setText(null);
//        System.out.println("Getting cell rendering component, icon: " + checkBoxIcon.getStatus() );

        return l;
    }

    private void setCheckBoxStatus(Object val) {

        if (val instanceof Status) {
            switch ((Status)val) {
                case SELECTED:
                    checkBoxIcon = selectedCheckBoxIcon;
                    break;
                case DESELECTED:
                    checkBoxIcon = deselectedCheckBoxIcon;
                    break;
                case INDETERMINATE:
                    checkBoxIcon = indeterminateCheckBoxIcon;
                    break;
            }
        } else {
            checkBoxIcon = indeterminateCheckBoxIcon;
        }

    }

}

class CheckBoxIcon implements Icon {

    private final JCheckBox check;
    private final Status status;

    public CheckBoxIcon(JCheckBox check, Status status) {
        this.check = check;
        this.status = status;
    }

    public Status getStatus() { return status; }

    @Override
    public int getIconWidth() {
        return check.getPreferredSize().width;
    }

    @Override
    public int getIconHeight() {
        return check.getPreferredSize().height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        SwingUtilities.paintComponent(
                g, check, (Container) c, x, y, getIconWidth(), getIconHeight());
    }
}

enum Status { SELECTED, DESELECTED, INDETERMINATE }