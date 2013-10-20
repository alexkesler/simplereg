package org.kesler.simplereg.gui.reestr.print;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.xssf.util.CellReference;
// import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.gui.reestr.column.ReestrColumn;
import org.kesler.simplereg.gui.reestr.column.ReestrColumns;

public class ReestrExporter {

	public static void exportReestr() {

		List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();

		List<Reception> receptions = ReceptionsModel.getInstance().getFilteredReceptions();


		SXSSFWorkbook wb = new SXSSFWorkbook();
		Sheet sh = wb.createSheet();
		
		// Впечатываем наименования

		Row titleRow = sh.createRow(0);
		for (int colnum = 0; colnum < reestrColumns.size(); colnum++) {
			Cell cell = titleRow.createCell(colnum);
			String value = reestrColumns.get(colnum).getName();
			cell.setCellValue(value);			
		}

		// Впечатываем значения

		for (int rownum = 0; rownum < receptions.size(); rownum++) {
			Row row = sh.createRow(rownum+1);
			for (int colnum = 0; colnum < reestrColumns.size(); colnum++) {
				Cell cell = row.createCell(colnum);
				String value = reestrColumns.get(colnum).getValue(receptions.get(rownum)).toString();
				cell.setCellValue(value);
			}
		}


		try {
			File file = new File("reestr.xlsx");
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// wb.dispose();

	}

}