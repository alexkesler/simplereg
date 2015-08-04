package org.kesler.simplereg.export.reestr;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.util.OptionsUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Формирование реестра передачи
 */
public class TransmitReestrExporter extends ReestrExporter {


    public TransmitReestrExporter() {
        type = Type.FOR_TRANSMIT;
    }

    @Override
    protected void prepare() {

        String filialName = OptionsUtil.getOption("reg.filial.name");
        String filialAddress = OptionsUtil.getOption("reg.filial.address");


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Sheet sh = wb.createSheet();

        sh.setColumnWidth(0, 256*5);
        sh.setColumnWidth(1, 256*30);
        sh.setColumnWidth(2, 256*25);
        sh.setColumnWidth(3, 256 * 15);
        sh.setColumnWidth(4, 256 * 22);
        sh.setColumnWidth(5, 256 * 15);
        sh.setColumnWidth(6, 256 * 15);


        CellStyle centeredCellStyle = wb.createCellStyle();
        centeredCellStyle.setWrapText(true);
        centeredCellStyle.setAlignment(CellStyle.ALIGN_CENTER);


        CellStyle underlineCenteredCellStyle = wb.createCellStyle();
        underlineCenteredCellStyle.setWrapText(true);
        underlineCenteredCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        underlineCenteredCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        CellStyle underlineLeftCellStyle = wb.createCellStyle();
        underlineLeftCellStyle.setWrapText(true);
        underlineLeftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        underlineLeftCellStyle.setBorderBottom(CellStyle.BORDER_THIN);


        Row titleRow = sh.createRow(0);
        Cell cell = titleRow.createCell(0);
        titleRow.createCell(1);
        cell.setCellValue("Ведомость приема-передачи документов");
        cell.setCellStyle(centeredCellStyle);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        CellRangeAddress range = new CellRangeAddress(0,0,0,6);
        sh.addMergedRegion(range);

        titleRow = sh.createRow(1);
        cell = titleRow.createCell(0);
        titleRow.createCell(1);
        cell.setCellValue("от ГБУ СО «Многофункциональный центр» (" + filialName + ")");
        cell.setCellStyle(centeredCellStyle);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        range = new CellRangeAddress(1,1,0,6);
        sh.addMergedRegion(range);

        titleRow = sh.createRow(2);
        cell = titleRow.createCell(0);
        titleRow.createCell(1);
        cell.setCellValue(filialAddress);
        cell.setCellStyle(centeredCellStyle);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        range = new CellRangeAddress(2,2,0,6);
        sh.addMergedRegion(range);

        titleRow = sh.createRow(3);
        cell = titleRow.createCell(0);
        titleRow.createCell(1);
        cell.setCellValue("");
        cell.setCellStyle(centeredCellStyle);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        range = new CellRangeAddress(3,3,0,6);
        sh.addMergedRegion(range);


        titleRow = sh.createRow(4);
        cell = titleRow.createCell(0);
        titleRow.createCell(1);
        cell.setCellValue("на " + dateFormat.format(new Date()));
        cell.setCellStyle(centeredCellStyle);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        range = new CellRangeAddress(4,4,0,6);
        sh.addMergedRegion(range);

        titleRow = sh.createRow(5);
        cell = titleRow.createCell(0);
        titleRow.createCell(1);
        cell.setCellValue("в ______________________________________________________________________________");
        cell.setCellStyle(centeredCellStyle);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        range = new CellRangeAddress(5,5,0,6);
        sh.addMergedRegion(range);

        titleRow = sh.createRow(6);
        titleRow.setHeightInPoints(27);
        cell = titleRow.createCell(0);
        cell.setCellValue("В соответствии с соглашением о взаимодействии при оказании услуг ГБУ СО «Многофункциональный центр» направляет для " +
                "обработки нижеперечисленные комплекты документов заявителей");
        CellStyle titleCellStyle1 = wb.createCellStyle();
        titleCellStyle1.cloneStyleFrom(centeredCellStyle);
        titleCellStyle1.setAlignment(CellStyle.ALIGN_JUSTIFY);
        cell.setCellStyle(titleCellStyle1);
        titleRow.createCell(1);
        titleRow.createCell(2);
        titleRow.createCell(3);
        titleRow.createCell(4);
        titleRow.createCell(5);
        titleRow.createCell(6);
        range = new CellRangeAddress(6,6,0,6);
        sh.addMergedRegion(range);



        int tableRowPos = 8;


        // Создаем заголовок
        Row headerRow = sh.createRow(tableRowPos);

        // Стиль

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setWrapText(true);
        headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerCellStyle.setFont(font);

        cell = headerRow.createCell(0);
        cell.setCellValue("№ п/п");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Наименование услуги");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(2);
        cell.setCellValue("Фамилия, инициалы (либо наименование) заявителя");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(3);
        cell.setCellValue("Дата приема");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(4);
        cell.setCellValue("Регистрационный номер запроса");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(5);
        cell.setCellValue("Количество листов в запросе");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(6);
        cell.setCellValue("Примечание");
        cell.setCellStyle(headerCellStyle);

       // Заполняем таблицу

        CellStyle tableCellStyle = wb.createCellStyle();
        tableCellStyle.setWrapText(true);
        tableCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        tableCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        tableCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        tableCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        CellStyle tableCenteredCellStyle = wb.createCellStyle();
        tableCenteredCellStyle.cloneStyleFrom(tableCellStyle);
        tableCenteredCellStyle.setAlignment(CellStyle.ALIGN_CENTER);


        for (int rownum = 0; rownum < receptions.size(); rownum++) {
            Reception reception = receptions.get(rownum);
            Row row = sh.createRow(rownum+tableRowPos+1);
            for (int colnum = 0; colnum < 7; colnum++) {
                cell = row.createCell(colnum);
                switch (colnum) {
                    case 0:
                        cell.setCellValue(rownum+1);
                        cell.setCellStyle(tableCenteredCellStyle);
                        break;
                    case 1:
                        cell.setCellValue(reception.getServiceName());
                        cell.setCellStyle(tableCellStyle);
                        break;
                    case 2:
                        cell.setCellValue(reception.getApplicatorsNames());
                        cell.setCellStyle(tableCellStyle);
                        break;
                    case 3:
                        cell.setCellValue(dateFormat.format(reception.getOpenDate()));
                        cell.setCellStyle(tableCenteredCellStyle);
                        break;
                    case 4:
                        cell.setCellValue(reception.getReceptionCode());
                        cell.setCellStyle(tableCellStyle);
                        break;
                    case 5:
                        cell.setCellValue(reception.getPagesNum()==null?"":reception.getPagesNum()+"");
                        cell.setCellStyle(tableCenteredCellStyle);
                        break;
                    default:
                        cell.setCellStyle(tableCellStyle);
                        break;
                }


            }
        }

        ///// футер

        int footerRowPos = tableRowPos + receptions.size() + 4;


        ///// специалист
        Row footerRow = sh.createRow(footerRowPos);

        cell = footerRow.createCell(0);
        cell.setCellValue("Специалист ГБУ СО «Многофункциональный центр», подготовивший ведомость");
        footerRow.createCell(1);
        footerRow.createCell(2);
        footerRow.createCell(3);
        footerRow.createCell(4);
        footerRow.createCell(5);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos,footerRowPos,0,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 1);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("__________________");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+1,footerRowPos+1,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellValue(CurrentOperator.getInstance().getOperator().getFIO());
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(3);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos+1,footerRowPos+1,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellValue(dateFormat.format(new Date()));
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(6);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos+1,footerRowPos+1,5,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 2);

        cell = footerRow.createCell(0);
        cell.setCellValue("(подпись)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+2,footerRowPos+2,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellValue("(фамилия, инициалы)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(3);
        range = new CellRangeAddress(footerRowPos + 2,footerRowPos+2,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellValue("(дата)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+2,footerRowPos+2, 5, 6);
        sh.addMergedRegion(range);

        /// курьер

        footerRow = sh.createRow(footerRowPos + 3);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("Курьер ГБУ СО «Многофункциональный центр»");
        footerRow.createCell(1);
        footerRow.createCell(2);
        footerRow.createCell(3);
        footerRow.createCell(4);
        footerRow.createCell(5);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+3,footerRowPos+3, 0, 6);
        sh.addMergedRegion(range);


        footerRow = sh.createRow(footerRowPos + 4);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("__________________");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+4,footerRowPos+4, 0, 1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellStyle(underlineLeftCellStyle);
        cell = footerRow.createCell(3);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos + 4, footerRowPos+4, 2, 3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(6);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos+4,footerRowPos+4,5,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 5);

        cell = footerRow.createCell(0);
        cell.setCellValue("(подпись)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+5,footerRowPos+5,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellValue("(фамилия, инициалы)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(3);
        range = new CellRangeAddress(footerRowPos + 5,footerRowPos+5,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellValue("(дата)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+5,footerRowPos+5,5,6);
        sh.addMergedRegion(range);

        /// Документы получил Специалист МФЦ

        footerRow = sh.createRow(footerRowPos + 6);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("Документы получил:");
        footerRow.createCell(1);
        footerRow.createCell(2);
        footerRow.createCell(3);
        footerRow.createCell(4);
        footerRow.createCell(5);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+6,footerRowPos+6,0,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 7);

        cell = footerRow.createCell(0);
        cell.setCellValue("Специалист ГБУ СО «Многофункциональный центр»");
        footerRow.createCell(1);
        footerRow.createCell(2);
        footerRow.createCell(3);
        footerRow.createCell(4);
        footerRow.createCell(5);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+7,footerRowPos+7,0,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 8);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("__________________");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+8,footerRowPos+8,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(3);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos + 8,footerRowPos+8,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(6);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos+8,footerRowPos+8,5,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 9);

        cell = footerRow.createCell(0);
        cell.setCellValue("(подпись)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+9,footerRowPos+9,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellValue("(фамилия, инициалы)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(3);
        range = new CellRangeAddress(footerRowPos + 9,footerRowPos+9,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellValue("(дата)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+9,footerRowPos+9,5,6);
        sh.addMergedRegion(range);


        /// Документы получил Уполномоченное лицо Органа-получателя

        footerRow = sh.createRow(footerRowPos + 10);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("Документы получил:");
        footerRow.createCell(1);
        footerRow.createCell(2);
        footerRow.createCell(3);
        footerRow.createCell(4);
        footerRow.createCell(5);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+10,footerRowPos+10,0,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 11);

        cell = footerRow.createCell(0);
        cell.setCellValue("Уполномоченное лицо Органа-получателя");
        footerRow.createCell(1);
        footerRow.createCell(2);
        footerRow.createCell(3);
        footerRow.createCell(4);
        footerRow.createCell(5);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+11,footerRowPos+11,0,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 12);
        footerRow.setHeightInPoints(27);

        cell = footerRow.createCell(0);
        cell.setCellValue("__________________");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+12,footerRowPos+12,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(3);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos + 12,footerRowPos+12,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellStyle(underlineCenteredCellStyle);
        cell = footerRow.createCell(6);
        cell.setCellStyle(underlineCenteredCellStyle);
        range = new CellRangeAddress(footerRowPos+12,footerRowPos+12,5,6);
        sh.addMergedRegion(range);

        footerRow = sh.createRow(footerRowPos + 13);

        cell = footerRow.createCell(0);
        cell.setCellValue("(подпись)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(1);
        range = new CellRangeAddress(footerRowPos+13,footerRowPos+13,0,1);
        sh.addMergedRegion(range);
        cell = footerRow.createCell(2);
        cell.setCellValue("(фамилия, инициалы)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(3);
        range = new CellRangeAddress(footerRowPos + 13,footerRowPos+13,2,3);
        sh.addMergedRegion(range);
        footerRow.createCell(4);
        cell = footerRow.createCell(5);
        cell.setCellValue("(дата)");
        cell.setCellStyle(centeredCellStyle);
        footerRow.createCell(6);
        range = new CellRangeAddress(footerRowPos+13,footerRowPos+13,5,6);
        sh.addMergedRegion(range);


    }

}
