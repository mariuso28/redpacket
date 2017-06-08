package org.rp.web.account;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;
import org.rp.web.GzFormValidationException;
import org.rp.account.GzAccount;


public class GzAccountValues 
{
	private static final Logger log = Logger.getLogger(GzAccountValues.class);	
	private String creditAsPlayer;
	private String creditAsBanker;
	private String betCommission;
	private String winCommission;
	private int paymentDays;
	
	public GzAccountValues()
	{
	}
	
	public GzAccountValues(GzAccount account)
	{
		NumberFormat formatter = new DecimalFormat("#0.00");     
		creditAsPlayer = formatter.format(account.getCreditAsPlayer());
		creditAsBanker = formatter.format(account.getCreditAsBanker());
		betCommission = formatter.format(account.getBetCommission());
		winCommission = formatter.format(account.getWinCommission());
		paymentDays = account.getPaymentDays();
	}
	
	public GzAccount createAccount() throws GzFormValidationException
	{
		log.info("Creating account from : " + this.toString());
		GzAccount acc = new GzAccount();
		String invalidFields = "";
		int cnt = 0;
		try
		{
			acc.setCreditAsPlayer(Double.parseDouble(creditAsPlayer));
		}
		catch (NumberFormatException e)
		{
			invalidFields +="Credit Limit For Player,";
			cnt++;
		}
		try
		{
			acc.setCreditAsBanker(Double.parseDouble(creditAsBanker));
		}
		catch (NumberFormatException e)
		{
			invalidFields +="Credit Limit For Banker,";
			cnt++;
		}
		try
		{
			acc.setBetCommission(Double.parseDouble(betCommission));
		}
		catch (NumberFormatException e)
		{
			invalidFields +="Bet Commission,";
			cnt++;
		}
		try
		{
			acc.setWinCommission(Double.parseDouble(winCommission));
		}
		catch (NumberFormatException e)
		{
			invalidFields +="Win Commission,";
			cnt++;
		}
		
		acc.setPaymentDays(paymentDays);
		
		if (cnt>0)
		{
			invalidFields = invalidFields.substring(0,invalidFields.length()-1);
			if (cnt == 1)
				throw new GzFormValidationException("Invalid format for field: " + invalidFields);
			else
				throw new GzFormValidationException("Invalid format for fields: " + invalidFields);
		}
		
		return acc;
	}

	public String getCreditAsPlayer() {
		return creditAsPlayer;
	}

	public void setCreditAsPlayer(String creditAsPlayer) {
		this.creditAsPlayer = creditAsPlayer;
	}

	public String getCreditAsBanker() {
		return creditAsBanker;
	}

	public void setCreditAsBanker(String creditAsBanker) {
		this.creditAsBanker = creditAsBanker;
	}

	public String getBetCommission() {
		return betCommission;
	}

	public void setBetCommission(String betCommission) {
		this.betCommission = betCommission;
	}

	public String getWinCommission() {
		return winCommission;
	}

	public void setWinCommission(String winCommission) {
		this.winCommission = winCommission;
	}

	public int getPaymentDays() {
		return paymentDays;
	}

	public void setPaymentDays(int paymentDays) {
		this.paymentDays = paymentDays;
	}

	@Override
	public String toString() {
		return "GzAccountValues [creditAsPlayer=" + creditAsPlayer + ", creditAsBanker=" + creditAsBanker
				+ ", betCommission=" + betCommission + ", winCommission=" + winCommission + ", paymentDays="
				+ paymentDays + "]";
	}
	
	
 }
