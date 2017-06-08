package org.rp.web.account.pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.rp.services.GzServices;
import org.rp.web.account.GzExceptionFatal;
import org.rp.baseuser.GzBaseUser;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFStoreOpenInvoices {

	private static final Logger log = Logger.getLogger(PDFStoreOpenInvoices.class);
	private String pdfPath;
	
	
	public PDFStoreOpenInvoices(GzServices services,Map<String, Object> model, GzBaseUser from, GzBaseUser to ) throws Exception
	{
		pdfPath = services.getProperties().getProperty("pdfPath");
		if (pdfPath==null)
			throw new GzExceptionFatal("Couldn't format pdf doc - pdf path not set in config");
		
		// String invoiceType = (String) model.get("invoiceType");
		
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df1 = new SimpleDateFormat("yyMMddhhmmss");
		String dStr = df1.format(gc.getTime());
		
		pdfPath = pdfPath.trim();
		if (!pdfPath.endsWith("/"))
			pdfPath += "/";
				
		pdfPath	+= "Invoice_"  + from.getContact() + "_" + to.getContact() 
					+ "_" + dStr + ".pdf";
		Document doc=new Document();
		log.info("Writing Open Invoices to pdf file:" + pdfPath);
		PdfWriter.getInstance(doc,new FileOutputStream(pdfPath));
		doc.open(); 
		PdfOpenInvoices.buildPdfDocument(model, doc);
		doc.close();	
	}

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
}