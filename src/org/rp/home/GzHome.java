package org.rp.home;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.rp.account.GzAccount;
import org.rp.account.GzInvoice;
import org.rp.account.GzPayment;
import org.rp.account.GzRollup;
import org.rp.account.GzTransaction;
import org.rp.account.persistence.GzTransactionRowMapperPaginated;
import org.rp.account.persistence.GzXactionRowMapperPaginated;
import org.rp.admin.GzAdmin;
import org.rp.admin.GzAdminProperties;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

public interface GzHome 
{	
	public JdbcTemplate getJdbcTemplate();
	
	// general + account stuff
	public void storeBaseUser(GzBaseUser baseUser) throws GzPersistenceException;
	public GzBaseUser getBaseUserByEmail(String email) throws GzPersistenceException;
	public void updateBaseUserProfile(GzBaseUser baseUser) throws GzPersistenceException;
	public void getMembersForBaseUser(GzBaseUser baseUser) throws GzPersistenceException;
	public GzAgent getAgentByCode(String code) throws GzPersistenceException;
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException;
	public GzAdmin getAdminByEmail(String email) throws GzPersistenceException;
	public void storeTransaction(GzTransaction transaction) throws GzPersistenceException;
	public void storeAdmin(GzAdmin admin) throws GzPersistenceException;
	public void updateAdmin(GzAdmin currUser) throws GzPersistenceException;
	public void updateAccount(GzAccount account) throws GzPersistenceException;
	public void storeAgent(GzAgent agent) throws GzPersistenceException;
	public double getDownStreamCreditAsPlayer(GzBaseUser user, GzBaseUser parent);
	public double getDownStreamCreditAsBanker(GzBaseUser user, GzBaseUser parent);
	public void updateTransaction(GzTransaction trans) throws GzPersistenceException;
	public GzTransactionRowMapperPaginated getGzTransactionRowMapperPaginated(int count) throws GzPersistenceException;
	public GzRollup getRollupForUser(String userId,String code,GzRole role);
	public GzBaseUser getParentForUser(GzBaseUser currUser) throws GzPersistenceException;
	public List<GzRollup> getMemberRollups(GzBaseUser currUser);
	public List<GzInvoice> getOutstandingInvoices(GzBaseUser payer, GzBaseUser payee) throws GzPersistenceException;
	public GzXactionRowMapperPaginated getGzInvoiceRowMapperPaginated(GzBaseUser user, int count);
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException;
	public GzBaseUser getBaseUserByCode(String code) throws GzPersistenceException;
	public void updateEnabled(GzBaseUser currAccountUser) throws GzPersistenceException;
	public void getDownstreamForParent(GzBaseUser currUser);
	public GzInvoice getOpenInvoice(String payer, String payee, char winstake) throws GzPersistenceException;
	public void updateInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void updateInvoice(double amount, double commission, double netAmount, double stake, long id) throws GzPersistenceException;
	public void updateSubInvoice(GzInvoice subInvoice, GzInvoice invoice) throws GzPersistenceException;
	public void storeInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void storePayment(GzPayment payment) throws GzPersistenceException;
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException;
	public void closeOpenInvoices() throws GzPersistenceException;
	public String getCloseInvoiceAfterMins();
	public void setCloseInvoiceAfterMins(String closeInvoiceAfterMins);
	public int getCloseInvoiceAfter();
	public void setCloseInvoiceAfter(int closeInvoiceAfter);
	public String getCloseInvoiceStartAt();
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException;
	public GzPayment getPaymentForId(long paymentNum) throws GzPersistenceException;
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public double getHigestDownstreamCommission(char type, String code) throws GzPersistenceException;
	public String getEmailForId(UUID id) throws GzPersistenceException;
	public void storeImage(String email, MultipartFile data, String contentType) throws GzPersistenceException;
	public byte[] getImage(final String email) throws GzPersistenceException;
	public void updateAccountBalance(GzAccount account,double amount) throws GzPersistenceException;
	public GzAccount getAccount(GzBaseUser baseUser) throws GzPersistenceException;
	public void setDefaultPasswordForAll(String encoded);
	public boolean getScheduledDownTimeSet() throws GzPersistenceException;
	public void setScheduledDownTime(boolean set) throws GzPersistenceException;
	public void storeScheduledDownTime(Date date) throws GzPersistenceException;
	public Date getScheduledDownTime() throws GzPersistenceException;
	public Map<UUID, Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException;
	public GzAdminProperties getAdminProperties() throws GzPersistenceException;
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException;
	public void updateLeafInstance(GzBaseUser bu);
	public void updateVersionCode(UUID uuid) throws GzPersistenceException;
	public UUID getVersionCode() throws GzPersistenceException;
	public void overrideDataSourceUrl(String url);

	public List<String> getColumns(String table) throws GzPersistenceException;

	
	
}
