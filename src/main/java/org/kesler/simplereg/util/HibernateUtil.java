package org.kesler.simplereg.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import java.util.Properties;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;

	static {

		Properties hibernateProperties = new Properties();
		//hibernateProperties.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");

		Configuration hibernateConfiguration = new Configuration();

		//hibernateConfiguration.setProperties(hibernateProperties);
		/// Пытаемся сконфигурировать Hibernate
		System.out.println("Configuring Hibernate ...");
		try {
			//creates session factory
			hibernateConfiguration = hibernateConfiguration.configure();
		} catch (HibernateException he) {
			System.err.println("Hibernate configurationError");
			he.printStackTrace();
		}

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