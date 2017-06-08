package org.rp.web.account;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.NumberUtil;
import org.rp.util.StackDump;
import org.rp.web.GzFormValidationException;
import org.rp.account.GzAccount;
import org.rp.account.GzInvoice;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;

public class AccountDetailsForm{
	
	private static final Logger log = Logger.getLogger(AccountDetailsForm.class);
	
	private static int MAXPAYMENTDAYS = 7;
	private double availableCreditPlayer;
	private double availableCreditBanker;
	private int maxPaymentDays;
	private List<Integer> paymentDays;
	private AccountCommand command;
	private GzAccount account;
	private String message;
	private String info;
	private boolean modify;
	private List<GzInvoice> outstandingInvoices;
	private List<String> payFlags;
	private List<Boolean> overDueFlags;
	private double outstandingInvoicesTotal;
	private GzBaseUser payee;
	private GzBaseUser payer;
	private GzAccountValues prevAccount;
	
	public AccountDetailsForm()
	{
		setModify(false);
	}
	
	private AccountDetailsForm(GzBaseUser payer,GzBaseUser payee)
	{
		this();
		setPayer(payer);
		setPayee(payee);
		setAccount(payer.getAccount());
		setPrevAccount(new GzAccountValues(payer.getAccount()));
	}
	
	public AccountDetailsForm(GzHome gzHome,GzBaseUser payer,GzBaseUser payee)
	{
		this(payer,payee);
		availableCreditPlayer = gzHome.getDownStreamCreditAsPlayer(payer,payee); 
		availableCreditBanker = gzHome.getDownStreamCreditAsBanker(payer,payee); 
		setMaxPaymentDays(AccountDetailsForm.MAXPAYMENTDAYS);
		paymentDays = new ArrayList<Integer>();
		for (int i=0; i<=AccountDetailsForm.MAXPAYMENTDAYS; i++)
			paymentDays.add(i);
		try {
			setOutstandingInvoices(gzHome.getOutstandingInvoices(payer,payee));
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		payFlags = new ArrayList<String>();
		overDueFlags = new ArrayList<Boolean>();
		GregorianCalendar gc = new GregorianCalendar();
		for (GzInvoice invoice : outstandingInvoices)
		{
			payFlags.add("off");
			overDueFlags.add(invoice.getDueDate().before(gc.getTime()));
		}
	}
	
	public void setPayFlagsOn(Boolean currChecked) {
		payFlags = new ArrayList<String>();
		for (@SuppressWarnings("unused") GzInvoice invoice : outstandingInvoices)
		{
			if (currChecked)
				payFlags.add("on");
			else
				payFlags.add("off");
		}
	}
	
	public AccountDetailsForm(GzHome gzHome,GzBaseUser user,GzBaseUser parent,AccountDetailsForm lastForm)
	{
		this(gzHome,user,parent);
		setPrevAccount(lastForm.getCommand().getNewAccount());
	}

	public void setCommand(AccountCommand command) {
		this.command = command;
	}

	public AccountCommand getCommand() {
		return command;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMaxPaymentDays(int maxPaymentDays) {
		this.maxPaymentDays = maxPaymentDays;
	}

	public int getMaxPaymentDays() {
		return maxPaymentDays;
	}
	
	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public boolean isModify() {
		return modify;
	}

	public void validate(GzServices gzServices,GzBaseUser user,GzBaseUser parent) throws GzFormValidationException
	{
		GzHome gzHome = gzServices.getGzHome();
		
		
		log.info( " validate" + account);
		GzAccount account = getCommand().getNewAccount().createAccount();
		
		
		if (parent.getRole().equals(GzRole.ROLE_ADMIN) || user==parent)   // no relevant parent details (if user == parent - company setting own values)
		{
			// still got warn out negative commissions downstream
			if (user.getRole().equals(GzRole.ROLE_COMP))
				checkDownstreamCommissions(gzHome,user,account);
			return;
		}
		
		if (account.getPaymentDays() > MAXPAYMENTDAYS)
		{
			account.setPaymentDays(0);
			throw new GzFormValidationException("Max Payment Days exceeded - should be less/equal to :" + MAXPAYMENTDAYS);
		}
		
		if (gzServices.getGzAdminProperties().isCreditAsPlayerOn()==false)
		{
			if (account.getCreditAsPlayer() != 0.0)
				throw new GzFormValidationException("Credit for Player not enabled");
		}
		else
		{
			double available = gzHome.getDownStreamCreditAsPlayer(user,parent);
			if (account.getCreditAsPlayer() > available)
			{
				account.setCreditAsPlayer(0.0);
				throw new GzFormValidationException("Max Credit for Player exceeded - should be less/equal to: " 
						+ String.format("%1$,.2f",available));
			}
		}
		
		if (gzServices.getGzAdminProperties().isCreditAsBankerOn()==false)
		{
			if (account.getCreditAsBanker() != 0.0)
				throw new GzFormValidationException("Credit for Banker not enabled");
		}
		else
		{
			double available = gzHome.getDownStreamCreditAsBanker(user,parent);
			if (account.getCreditAsBanker() > available)
			{
				account.setCreditAsBanker(0.0);
				throw new GzFormValidationException("Max Credit for Banker exceeded - should be less/equal to: " 
						+ String.format("%1$,.2f",available));
			}
		}
		
		if (gzServices.getGzAdminProperties().isBetCommissionOn()==false)
		{
			if (account.getBetCommission() != 0.0)
				throw new GzFormValidationException("Bet Commission not enabled");
		}
		else
		{
			if (account.getBetCommission()>parent.getAccount().getBetCommission())
				throw new GzFormValidationException("Maximum Bet Commission cannot be exceeded");
		}
		
		if (gzServices.getGzAdminProperties().isWinCommissionOn()==false)
		{
			if (account.getWinCommission() != 0.0)
				throw new GzFormValidationException("Win Commission not enabled");
		}
		else
		{
			if (account.getWinCommission()>parent.getAccount().getWinCommission())
				throw new GzFormValidationException("Maximum Win Commission cannot be exceeded");
		}
		
		checkDownstreamCommissions(gzHome,user,account);
	}

	private void checkDownstreamCommissions(GzHome gzHome,GzBaseUser user, GzAccount account) throws GzFormValidationException{
		try {
			double dwc = gzHome.getHigestDownstreamCommission('W',user.getCode());
			double dbc = gzHome.getHigestDownstreamCommission('B',user.getCode());
			String msg = "";
			if (dwc > account.getWinCommission())
				msg += "Win commission cannot be set smaller than downstream member's ";
			if (dbc > account.getBetCommission())
				msg += "Bet commission cannot be set smaller than downstream member's";
			if (!msg.isEmpty())
				throw new GzFormValidationException(msg);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
		}
	}

	public void validateWithDep(GzHome gzHome,GzBaseUser user,GzBaseUser parent) throws GzFormValidationException, GzPersistenceException{
		
		Double dwAmount;
		try
		{
			dwAmount = Double.parseDouble(getCommand().getDwAmount());
		}
		catch (NumberFormatException e)
		{
			if (getCommand().getDwType().equals("Withdrawl"))
				throw new GzFormValidationException("Withdrawal amount has invalid format.");
			else
				throw new GzFormValidationException("Deposit amount has invalid format.");
		}
		dwAmount = NumberUtil.trimDecimals2(dwAmount);
		
		if (dwAmount==0.0)
		{
			return;
		}
		
		GzAccount acc = gzHome.getAccount(user);
		user.setAccount(acc);
		if (getCommand().getDwType().equals("Withdrawl"))
		{
			double diff = user.getAccount().getBalance() - dwAmount;
			if (diff<0.0)
			{
				log.info("Amt: " +  dwAmount + " Bal : " + user.getAccount().getBalance());
				throw new GzFormValidationException("Withdrawal amount cannot be more than account's balance.");
			}
			return;
		}
		
		if (user.getAccount().getBalance()>0 || (user.getAccount().getBalance()*-1.0 < dwAmount))
		{
			throw new GzFormValidationException("Can only deposit into a negative balance up to : " + user.getAccount().getBalance());
		}
			
		/* Deposit
		double available = gzHome.getDownStreamAccountBalance(user,parent);
		if (user.getAccount().getBalance() +  getCommand().getDwAmount() > available)
		{
			throw new GzFormValidationException("Max Balance amount exceeded - should be less/equal to: " 
					+ String.format("%1$,.2f",available));
		}
		*/
	}

	public void setOutstandingInvoices(List<GzInvoice> outstandingInvoices) {
		this.outstandingInvoices = outstandingInvoices;
		outstandingInvoicesTotal = 0.0;
		for (GzInvoice invoice : outstandingInvoices)
			if (invoice.getPayee().equals(payee.getEmail()))
				outstandingInvoicesTotal-=invoice.getNetAmount();
			else
				outstandingInvoicesTotal+=invoice.getNetAmount();
	}

	public List<GzInvoice> getOutstandingInvoices() {
		return outstandingInvoices;
	}

	public void setOutstandingInvoicesTotal(double outstandingInvoicesTotal) {
		this.outstandingInvoicesTotal = outstandingInvoicesTotal;
	}

	public double getOutstandingInvoicesTotal() {
		return outstandingInvoicesTotal;
	}

	public List<String> getPayFlags() {
		return payFlags;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setOverDueFlags(List<Boolean> overDueFlags) {
		this.overDueFlags = overDueFlags;
	}

	public List<Boolean> getOverDueFlags() {
		return overDueFlags;
	}

	public void setPaymentDays(List<Integer> paymentDays) {
		this.paymentDays = paymentDays;
	}

	public List<Integer> getPaymentDays() {
		return paymentDays;
	}

	public GzBaseUser getPayee() {
		return payee;
	}

	public void setPayee(GzBaseUser payee) {
		this.payee = payee;
	}

	public GzBaseUser getPayer() {
		return payer;
	}

	public void setPayer(GzBaseUser payer) {
		this.payer = payer;
	}

	public double getAvailableCreditPlayer() {
		return availableCreditPlayer;
	}

	public void setAvailableCreditPlayer(double availableCreditPlayer) {
		this.availableCreditPlayer = availableCreditPlayer;
	}

	public double getAvailableCreditBanker() {
		return availableCreditBanker;
	}

	public void setAvailableCreditBanker(double availableCreditBanker) {
		this.availableCreditBanker = availableCreditBanker;
	}

	public GzAccount getAccount() {
		return account;
	}

	public void setAccount(GzAccount account) {
		this.account = account;
	}

	public void setPayFlags(List<String> payFlags) {
		this.payFlags = payFlags;
	}

	public GzAccountValues getPrevAccount() {
		return prevAccount;
	}

	public void setPrevAccount(GzAccountValues prevAccount) {
		this.prevAccount = prevAccount;
	}
	
	
	
}
