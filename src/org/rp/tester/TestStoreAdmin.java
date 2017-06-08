package org.rp.tester;

import org.apache.log4j.Logger;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.admin.GzAdmin;
import org.rp.baseuser.GzBaseUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestStoreAdmin 
{
	private static Logger log = Logger.getLogger(TestStoreAdmin.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"rp-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome gzHome = gzServices.getGzHome();
		
		try {
				GzAdmin admin = new GzAdmin("gzAdmin@test.com");
				admin.setPassword("88888888");
				gzHome.storeAdmin(admin);
				
				GzBaseUser bu = gzHome.getBaseUserByCode(admin.getCode());
				log.info("DONE : " + bu.getCode() + " " + bu.getClass());
			} catch (GzPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
