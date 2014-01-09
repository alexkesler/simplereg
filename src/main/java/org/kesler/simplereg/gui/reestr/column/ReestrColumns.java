package org.kesler.simplereg.gui.reestr.column;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class ReestrColumns {

	private List<ReestrColumn> allColumns;

	private List<ReestrColumn> activeColumns;
	private List<ReestrColumn> inactiveColumns;

	private static ReestrColumns instance = null;

	public static synchronized ReestrColumns getInstance() {
		if (instance == null) {
			instance = new ReestrColumns();
		}

		return instance;
	}

	private ReestrColumns() {
		allColumns = new ArrayList<ReestrColumn>();
		ReestrColumn openDateReestrColumn = new OpenDateReestrColumn();
		ReestrColumn applicatorsReestrColumn = new ApplicatorsReestrColumn();
		ReestrColumn serviceReestrColumn = new ServiceReestrColumn();
		ReestrColumn statusReestrColumn = new StatusReestrColumn();
		ReestrColumn operatorReestrColumn = new OperatorReestrColumn();
		ReestrColumn toIssueDateReestrColumn = new ToIssueDateReestrColumn();
		ReestrColumn byRecordReestrColumn = new ByRecordReestrColumn();
		ReestrColumn receptionCodeReestrColumn = new ReceptionCodeReestrColumn();
		ReestrColumn rosreestrCodeReestrColumn = new RosreestrCodeReestrColumn();
        ReestrColumn realtyObjectReestrColumn = new RealtyObjectReestrColumn();
		ReestrColumn resultInMFCReestrColumn = new ResultInMFCReestrColumn();



		allColumns.add(receptionCodeReestrColumn);
		allColumns.add(openDateReestrColumn);
		allColumns.add(applicatorsReestrColumn);
		allColumns.add(serviceReestrColumn);
		allColumns.add(statusReestrColumn);
		allColumns.add(operatorReestrColumn);
		allColumns.add(toIssueDateReestrColumn);
		allColumns.add(rosreestrCodeReestrColumn);
        allColumns.add(realtyObjectReestrColumn);
		allColumns.add(resultInMFCReestrColumn);

		allColumns = Collections.unmodifiableList(allColumns); /// делаем полный список колонок неизменным во избежание

		// добавляем в активные поля 
		activeColumns = new ArrayList<ReestrColumn>();

		activeColumns.add(receptionCodeReestrColumn);		
		activeColumns.add(openDateReestrColumn);
		activeColumns.add(applicatorsReestrColumn);
		activeColumns.add(serviceReestrColumn);
		activeColumns.add(statusReestrColumn);
		activeColumns.add(resultInMFCReestrColumn);
		

		// список неактивных полей
		inactiveColumns = new ArrayList<ReestrColumn>();
		inactiveColumns.add(byRecordReestrColumn);
		inactiveColumns.add(operatorReestrColumn);
		inactiveColumns.add(toIssueDateReestrColumn);
		inactiveColumns.add(rosreestrCodeReestrColumn);
        inactiveColumns.add(realtyObjectReestrColumn);
	}

	public List<ReestrColumn> getAllColumns() {
		return allColumns;
	}

	public List<ReestrColumn> getActiveColumns() {
		return activeColumns;
	}

	public List<ReestrColumn> getInactiveColumns() {
		return inactiveColumns;
	}


	public boolean activateColumn(ReestrColumn column) {
		if (inactiveColumns.contains(column)) {
			inactiveColumns.remove(column);
			activeColumns.add(column);
			return true;
		} else {
			return false;
		}
	}

	public boolean deactivateColumn(ReestrColumn column) {
		if (activeColumns.contains(column)) {
			activeColumns.remove(column);
			inactiveColumns.add(column);
			return true;
		} else {
			return false;
		}
	}

}