package org.kesler.simplereg;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.Service;

import org.kesler.simplereg.dao.DAOFactory;

public class SimpleReg {

	public static void main(String[] args) {
		Service s1 = new Service();
		Service s2 = new Service();
		Service s21 = new Service();

		s1.setName("Service # 1");
		s2.setName("Service # 2");
		s21.setName("Service # 21");
		s21.setParentService(s2);

		try {
			DAOFactory.getInstance().getServiceDAO().addService(s1);
			DAOFactory.getInstance().getServiceDAO().addService(s2);
			DAOFactory.getInstance().getServiceDAO().addService(s21);


			List<Service> services = DAOFactory.getInstance().getServiceDAO().getAllServices();			
		} catch (SQLException e) {
			System.out.println("DB Error" + e.getMessage());
		}

		System.out.println("================Все услуги================");
		for (Service s : services) {
			System.out.println("ID: "+ s.getId() + " услуга: "+ s.getName());
		}

	}

}