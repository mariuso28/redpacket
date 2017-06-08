package org.rp.account;

import java.util.Date;
import java.util.UUID;

public class GzTransaction extends GzBaseTransaction {
	
	private static final long serialVersionUID = 659183036584351555L;
	private UUID playGameId;
	private long invoiceId;
	public final static char BETTYPEPLAYERSTAKE = 'S';
	public final static char BETTYPEPLAYERWIN = 'W';
	public final static char BETTYPEPLAYERTIE = 'T';
	public final static char BETTYPEBANKERCOLLECTSTAKE = 'C';
	public final static char BETTYPEBANKERPAYWIN = 'P';
	public final static char BETTYPEBANKERPAYTIE = 'X';
	
	public GzTransaction()
	{
	}
	
	public GzTransaction(String payer,String payee,Character type,double amount,Date timestamp,UUID playGameId)
	{
		super(payer,payee,type,amount,timestamp);
		setType(type);
		setPlayGameId(playGameId);
	}
	
	public UUID getPlayGameId() {
		return playGameId;
	}
	public void setPlayGameId(UUID playGameId) {
		this.playGameId = playGameId;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	
}
