package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.kesler.simplereg.dao.DAOFactory;


public class ReceptionsModel {
	private List<Reception> receptions;

	public ReceptionsModel() {
		receptions = new ArrayList<Reception>();
	}

	public ReceptionsModel(List<Reception> receptions) {
		this.receptions = receptions;
	}

	public List<Reception> getReceptions() {
		return receptions;
	}

	public void addReception(Reception reception) {
		receptions.add(reception);
		try {
			DAOFactory.getInstance().getReseptionDAO().addReception(reception);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null,sqle.getMesage(),"Ошибка записи в базу данных", JOptionPane.OK_OPTION);
		}
	}

}
