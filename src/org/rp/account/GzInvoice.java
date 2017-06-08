package org.rp.account;

import java.util.Date;

public class GzInvoice extends GzDeposit {
	
	private static final long serialVersionUID = -4558471539417421128L;
	
	public static final char STATUSOPEN = 'O';
	public static final char STATUSCLOSED = 'C';
	public static final char STATUSSETTLED = 'S';
	
	public static final char WINSTAKEWIN = 'W';
	public static final char WINSTAKETIE = 'T';
	public static final char WINSTAKESTAKE = 'S';
	
	private double commission;
	private double netAmount;
	private long parentId;
	private Date dueDate;
	private long paymentId;
	private char status;
	private double stake;
	private char winstake;
	
	public GzInvoice()
	{
	}

	public GzInvoice(String payer,String payee,double amount,double commission,double netAmount,Date timestamp,Date dueDate,double stake, char winstake)
	{
		setPayer(payer);
		setPayee(payee);
		setType(GzXaction.XTYPEINVOICE);
		setAmount(amount);
		setTimestamp(timestamp);
		setDueDate(dueDate);
		setCommission(commission);
		setNetAmount(netAmount);
		setPaymentId(-1L);
		setStake(stake);
		setWinstake(winstake);
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public void update(double amount2, double commission2, double netAmount2,double stake2,char winstake) {
		amount += amount2;
		if (winstake==GzInvoice.WINSTAKEWIN)
			amount+=stake2;
		commission += commission2;
		netAmount += netAmount2;
		stake += stake2;
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public char getWinstake() {
		return winstake;
	}

	public void setWinstake(char winstake) {
		this.winstake = winstake;
	}
	
	
}
