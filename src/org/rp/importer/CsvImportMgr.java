package org.rp.importer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.rp.agent.GzAgent;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.importer.util.MoveFile;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CsvImportMgr {
	private static Logger log = Logger.getLogger(CsvImportMgr.class);
	
	private int fileNum;
	private int currentFile;
	private GzServices gzServices;
	
	public CsvImportMgr(GzServices gzServices)
	{
		setGzServices(gzServices);
	}
	
	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public int getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(int currentFile) {
		this.currentFile = currentFile;
	}

	private AgentRec createAgentRec(String filename) throws CsvImporterException
	{
		// ie: a1@rpco.com-2017-05-19 - 2017-05-19.csv
		
		String[] toks = filename.split("-");
		GregorianCalendar gc = new GregorianCalendar();
	    gc.clear();
		String contact = toks[0];
		gc.set(Integer.parseInt(toks[1]),Integer.parseInt(toks[2])-1,Integer.parseInt(toks[3].trim()));
		Date date = gc.getTime();
		
		AgentRec ar = new AgentRec(contact,date);
		GzAgent agent = null;
		try {
			agent = gzServices.getGzHome().getAgentByEmail(contact);
		} catch (GzPersistenceException e) {
			e.printStackTrace();
			throw new CsvImporterException("Error retrieving Agent : " + contact + " - " + e.getMessage());
		}
		if (agent == null)
			throw new CsvImporterException("Agent : " + contact + " not on system");
		
		ar.setAgent(agent);
		return ar;
	}
	
	public void importFilesForFolder(final String folder,final String loaded) throws CsvImporterException {
	    
		File dir = new File(folder);
		currentFile = 0;
		fileNum = dir.listFiles().length;
		for (final File fileEntry : dir.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	            log.info("Importing :" +  fileEntry.getAbsolutePath());
	            
	            AgentRec agentRec = createAgentRec(fileEntry.getName());
	            CsvImporter csv = new CsvImporter(fileEntry.getAbsolutePath(),gzServices);
	    		try {
	    			log.info("$$$ Importing recs from : " + fileEntry.getAbsolutePath() + "  file#: " + currentFile + " of: " + fileNum);
	    			csv.readRecs(agentRec);
	    			if (loaded=="null")
	    				MoveFile.deleteFile(fileEntry.getAbsolutePath());
	    			else
	    			if (!loaded.equals(folder))				// else leave alone
	    				MoveFile.move(fileEntry.getAbsolutePath(),loaded + "/" + fileEntry.getName());
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        }
	    }
	}
	
	public GzServices getGzServices() {
		return gzServices;
	}

	public void setGzServices(GzServices gzServices) {
		this.gzServices = gzServices;
	}

	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"rp-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
	
		String folder = "/home/pmk/w2/redpacket/import/";
		String loaded = "/home/pmk/w2/redpacket/import/";			// same for testing
	
		if (args.length>0)
			folder = args[0];
		if (args.length>1)
			loaded = args[1];
		
		try
		{
			log.info("Running with folder : " + folder + " loaded dir : " + loaded); 
			CsvImportMgr cim = new CsvImportMgr(gzServices);
			cim.importFilesForFolder(folder,loaded);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
