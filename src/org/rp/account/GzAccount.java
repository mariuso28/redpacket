package org.rp.account;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;

public class GzAccount 
{
	private static final Logger log = Logger.getLogger(GzAccount.class);	
	private GzBaseUser baseUser;
	private double balance;
	private double creditAsPlayer;
	private double creditAsBanker;
	private double betCommission;
	private double winCommission;
	private int paymentDays;
	
	public GzAccount()
	{
	}
	
	public GzAccount(GzBaseUser baseUser)
	{
		setBaseUser(baseUser);
	}
	
	public void updateBalance(double stake) {
		balance += stake;
	}
	
	public void addBalance(double amount)
	{
		balance += amount;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getCreditAsPlayer() {
		return creditAsPlayer;
	}

	public void setCreditAsPlayer(double creditAsPlayer) {
		this.creditAsPlayer = creditAsPlayer;
	}

	public double getCreditAsBanker() {
		return creditAsBanker;
	}

	public void setCreditAsBanker(double creditAsBanker) {
		this.creditAsBanker = creditAsBanker;
	}

	public GzBaseUser getBaseUser() {
		return baseUser;
	}

	public void setBaseUser(GzBaseUser baseUser) {
		this.baseUser = baseUser;
	}

	public int getPaymentDays() {
		return paymentDays;
	}

	public void setPaymentDays(int paymentDays) {
		this.paymentDays = paymentDays;
	}

	public double getBetCommission() {
		return betCommission;
	}

	public void setBetCommission(double betCommission) {
		this.betCommission = betCommission;
	}

	public double getWinCommission() {
		return winCommission;
	}

	public void setWinCommission(double winCommission) {
		this.winCommission = winCommission;
	}

	public double getAvailableBalanceAsPlayer(double inhouseBalance) {
		
		return inhouseBalance + creditAsPlayer;
	}

	@Override
	public String toString() {
		return "GzAccount [balance=" + balance + ", creditAsPlayer=" + creditAsPlayer + ", creditAsBanker="
				+ creditAsBanker + ", betCommission=" + betCommission + ", winCommission=" + winCommission
				+ ", paymentDays=" + paymentDays + "]";
	}

 }
