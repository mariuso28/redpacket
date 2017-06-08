package org.rp.web.account;

import java.util.ArrayList;
import java.util.List;

public class AccountCommand{

	private GzAccountValues newAccount;
	private String dwAmount;
	private String dwType;
	private String comment;
	private List<Long> invoiceIds;
	private List<String> payFlags;

	public AccountCommand()
	{
		invoiceIds = new ArrayList<Long>();
		payFlags = new ArrayList<String>();
		setDwAmount("0.0");
		setComment("");
	}
	
	public GzAccountValues getNewAccount() {
		return newAccount;
	}

	public void setNewAccount(GzAccountValues newAccount) {
		this.newAccount = newAccount;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}
	
	public void setDwType(String dwType) {
		this.dwType = dwType;
	}

	public String getDwType() {
		return dwType;
	}

	public String getDwAmount() {
		return dwAmount;
	}

	public void setDwAmount(String dwAmount) {
		this.dwAmount = dwAmount;
	}

	@Override
	public String toString() {
		return "AccountCommand [newAccount=" + newAccount + ", dwAmount="
				+ dwAmount + ", dwType=" + dwType + ", comment=" + comment
				+ "]";
	}

	public void setPayFlags(List<String> payFlags) {
		this.payFlags = payFlags;
	}

	public List<String> getPayFlags() {
		return payFlags;
	}

	public void setInvoiceIds(List<Long> invoiceIds) {
		this.invoiceIds = invoiceIds;
	}

	public List<Long> getInvoiceIds() {
		return invoiceIds;
	}

	
}