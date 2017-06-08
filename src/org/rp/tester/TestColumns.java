package org.rp.tester;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.home.GzHome;
import org.rp.home.GzHomeDefaultActors;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestColumns 
{
	private static Logger log = Logger.getLogger(TestColumns.class);
	static GzHome gzHome;
	
	
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHomeDefaultActors gda = new GzHomeDefaultActors();
		gzHome = gzServices.getGzHome();
		
		List<String> cols;
		try {
			cols = gzHome.getColumns("room");
			log.info(cols);
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
