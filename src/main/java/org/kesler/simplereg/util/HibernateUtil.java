package org.kesler.simplereg.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import java.util.Properties;

import org.kesler.simplereg.util.OptionsUtil;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;

	static {

		String database = OptionsUtil.getOption("db.driver");
		String userName = OptionsUtil.getOption("db.user");
		String password = OptionsUtil.getOption("db.password");
		String server   = OptionsUtil.getOption("db.server");

		String driverClass = "org.h2.Driver";
		String connectionUrl = "";
		String dialect = "";

		if (database == "h2") { 					///// для базы данных H2
			driverClass = "org.h2.Driver";
			connectionUrl = "jdbc:h2:" 
			dialect = "org.hibernate.dialect.H2Dialect"
		} else if (database == "mysql") { 			///// для базы данных  MySQL
			driverClass = "com.mysql.jdbc.Driver";
			connectionUrl = "jdbc:mysql://" + server + ":3306/simplereg";
			dialect = "org.hibernate.dialect.MySQLDialect";
		}

		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.connection.driver_class","org.h2.Driver");
		hibernateProperties.setProperty("hibernate.connection.url","jdbc:h2:mem:test;INIT=create schema if not exists test");
		hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.H2Dialect");
		hibernateProperties.setProperty("hibernate.connection.username","sa");
		hibernateProperties.setProperty("hibernate.connection.password","");

		// hibernateProperties.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
		// hibernateProperties.setProperty("hibernate.connection.url","jdbc:mysql://10.10.0.170:3306/simplereg");
		// hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
		// hibernateProperties.setProperty("hibernate.connection.username","rroper");
		// hibernateProperties.setProperty("hibernate.connection.password","q1w2e3R$");

		hibernateProperties.setProperty("hibernate.c3p0.minPoolSize","5");
		hibernateProperties.setProperty("hibernate.c3p0.maxPoolSize","20");
		hibernateProperties.setProperty("hibernate.c3p0.timeout","1800");
		hibernateProperties.setProperty("hibernate.c3p0.max_statement","50");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto","create");
		hibernateProperties.setProperty("hibernate.show_sql","true");


		Configuration hibernateConfiguration = new Configuration()
						.addAnnotatedClass(org.kesler.simplereg.logic.Service.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.applicator.FL.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.applicator.UL.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.applicator.Applicator.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.applicator.ApplicatorFL.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.applicator.ApplicatorUL.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.operator.Operator.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.reception.Reception.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.reception.ReceptionStatus.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.realty.RealtyObject.class)
						.addAnnotatedClass(org.kesler.simplereg.logic.realty.RealtyType.class)
						.setProperties(hibernateProperties);

		//hibernateConfiguration.setProperties(hibernateProperties);
		/// Пытаемся сконфигурировать Hibernate
		// System.out.println("Configuring Hibernate ...");
// 		try {
// 			//creates session factory
// 			hibernateConfiguration = hibernateConfiguration.configure();
// 		} catch (HibernateException he) {
// 			System.err.println("Hibernate configurationError");
// 			he.printStackTrace();
// 		}

		System.out.println("Building Hibernate session factory ...");
		try {
			//creates session factory
			sessionFactory = hibernateConfiguration.buildSessionFactory();
		} catch (HibernateException he) {
			System.err.println("Hibernate session factory create Error");
			he.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}