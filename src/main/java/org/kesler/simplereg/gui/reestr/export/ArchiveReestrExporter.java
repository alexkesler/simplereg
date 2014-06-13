package org.kesler.simplereg.gui.reestr.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.kesler.simplereg.logic.Reception;

import java.util.ArrayList;
import java.util.List;

/**
 * Формирование реестра передачи в архив
 */
public class ArchiveReestrExporter extends ReestrExporter {

    private ReestrExportEnum exportEnum = ReestrExportEnum.FOR_ARCHIVE;


    @Override
    public ReestrExportEnum getEnum() {
        return exportEnum;
    }

    @Override
    protected void prepare() {
        Sheet sh = wb.createSheet();

        sh.setColumnWidth(0, 256*5);
        sh.setColumnWidth(1, 256*22);
        sh.setColumnWidth(2, 256*40);

        // Создаем заголовок
        Row titleRow = sh.createRow(0);

        // Стиль

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);


        Cell cell = titleRow.createCell(0);
        cell.setCellValue("№ п/п");
        cell.setCellStyle(cellStyle);

        cell = titleRow.createCell(1);
        cell.setCellValue("Код Росреестра");
        cell.setCellStyle(cellStyle);

        cell = titleRow.createCell(2);
        cell.setCellValue("Объект недвижимости");
        cell.setCellStyle(cellStyle);


        // формируем список основных дел

        List<Reception> mainReceptions = new ArrayList<Reception>();
        for (Reception reception: receptions)
            if(reception.getParentReception()==null) mainReceptions.add(reception);


        // Заполняем таблицу

        for (int rownum = 0; rownum < mainReceptions.size(); rownum++) {
            Reception reception = mainReceptions.get(rownum);
            Row row = sh.createRow(rownum+1);
            for (int colnum = 0; colnum < 3; colnum++) {
                cell = row.createCell(colnum);
                String value = "";
                switch (colnum) {
                    case 0:
                        value += rownum+1;
                        break;
                    case 1:
                        value = getRosreestrCodes(reception);
                        break;
                    case 2:
                        value = reception.getRealtyObject().toString();
                        break;
                }

                cell.setCellValue(value);

                cellStyle = wb.createCellStyle();
                cellStyle.setWrapText(true);
                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

                cell.setCellStyle(cellStyle);
            }
        }


    }

    private String getRosreestrCodes(Reception reception) {
        String codes = reception.getRosreestrCode();
        for (Reception subReception:reception.getSubReceptions())
            codes +="\n"+subReception.getRosreestrCode();
        return codes;
    }
}
