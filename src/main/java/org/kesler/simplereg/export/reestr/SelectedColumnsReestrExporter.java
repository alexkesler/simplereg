package org.kesler.simplereg.export.reestr;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.kesler.simplereg.gui.reestr.column.ReestrColumn;
import org.kesler.simplereg.gui.reestr.column.ReestrColumns;

import java.util.List;

/**
 * Формирование ведомости для выбранных колонок
 */
public class SelectedColumnsReestrExporter extends ReestrExporter {

    private ReestrExportEnum exportEnum = ReestrExportEnum.SELECTED_COLUMNS;

    @Override
    public ReestrExportEnum getEnum() {
        return exportEnum;
    }

    @Override
    protected void prepare() {
        Sheet sh = wb.createSheet();

        // Впечатываем наименования

        List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);


        Row titleRow = sh.createRow(0);
        for (int colnum = 0; colnum < reestrColumns.size(); colnum++) {
            int width = reestrColumns.get(colnum).getWidth();
            sh.setColumnWidth(colnum, 256*width);
            Cell cell = titleRow.createCell(colnum);
            String value = reestrColumns.get(colnum).getName();
            cell.setCellValue(value);

            cell.setCellStyle(cellStyle);
        }

        // Впечатываем значения

        for (int rownum = 0; rownum < receptions.size(); rownum++) {
            Row row = sh.createRow(rownum+1);
            for (int colnum = 0; colnum < reestrColumns.size(); colnum++) {
                Cell cell = row.createCell(colnum);
                String value = reestrColumns.get(colnum).getValue(receptions.get(rownum)).toString();
                cell.setCellValue(value);

                cell.setCellStyle(cellStyle);
            }
        }

    }
}
