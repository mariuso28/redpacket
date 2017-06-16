package org.rp.account.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.rp.account.GzAccount;
import org.rp.account.GzBaseTransaction;
import org.rp.account.GzDeposit;
import org.rp.account.GzInvoice;
import org.rp.account.GzPayment;
import org.rp.account.GzRollup;
import org.rp.account.GzTransaction;
import org.rp.account.GzWithdrawl;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


public class GzAccountDaoImpl extends NamedParameterJdbcDaoSupport implements GzAccountDao {
	
	private static Logger log = Logger.getLogger(GzAccountDaoImpl.class);
	
	
	@Override
	public void storeTransaction(final GzTransaction trans) throws GzPersistenceException
	{
		final Timestamp t1 = new Timestamp(trans.getTimestamp().getTime());
		try
		{
			getJdbcTemplate().update("INSERT INTO transaction (payer,payee,type,amount,invoiceid,timestamp,source) "
					+ "VALUES( ?, ?, ?, ?, ?, ?, ?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psStoreTransaction) throws SQLException {
							psStoreTransaction.setString(1,trans.getPayer());
							psStoreTransaction.setString(2,trans.getPayee());
							psStoreTransaction.setString(3,String.valueOf(trans.getType()));
							psStoreTransaction.setDouble(4,trans.getAmount());
							psStoreTransaction.setLong(5,trans.getInvoiceId());
							psStoreTransaction.setTimestamp(6,t1);
							psStoreTransaction.setString(7,trans.getSource());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	public void storeAccount(final UUID baseUserId) throws GzPersistenceException {
		
		try
		{
			getJdbcTemplate().update("INSERT INTO account (baseuserid,balance,creditAsPlayer,creditAsBanker,paymentDays) "
					+ "VALUES( ?,0.0,0.0,0.0,5)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psStoreAccount) throws SQLException {
							psStoreAccount.setObject(1,baseUserId);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updateAccount(final GzAccount account) throws GzPersistenceException {
		
		// BALANCE DONE INDEPENDENTLY
		try
		{
			getJdbcTemplate().update("UPDATE account set betcommission=?,wincommission=?,creditAsPlayer=?,"
					+ "creditAsBanker=?,paymentDays=? WHERE baseuserid=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateAccount) throws SQLException {
							psUpdateAccount.setDouble(1,account.getBetCommission());
							psUpdateAccount.setDouble(2,account.getWinCommission());
							psUpdateAccount.setDouble(3,account.getCreditAsPlayer());
							psUpdateAccount.setDouble(4,account.getCreditAsBanker());
							psUpdateAccount.setInt(5,account.getPaymentDays());
							psUpdateAccount.setObject(6,account.getBaseUser().getId());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updateAccountBalance(final GzAccount account, final double amount) throws GzPersistenceException {

		try
		{
			getJdbcTemplate().update("UPDATE account SET balance=balance+ ? WHERE baseuserid=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateAccount) throws SQLException {
							psUpdateAccount.setDouble(1,amount);
							psUpdateAccount.setObject(2,account.getBaseUser().getId());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public GzAccount getAccount(final GzBaseUser baseUser) throws GzPersistenceException
	{
		try
		{
			final String sql = "SELECT * FROM account WHERE baseuserid= ?";
			List<GzAccount> accs = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				        	preparedStatement.setObject(1, baseUser.getId());
				        }
				      }, new GzAccountRowMapper());
			if (accs.isEmpty())
				return null;
			return accs.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public List<GzInvoice> getOutstandingInvoices(final GzBaseUser payer, final GzBaseUser payee) throws GzPersistenceException {
		
		String sql = "SELECT * FROM xaction WHERE ((payer = '" + payer.getEmail() + "' AND payee = '" + payee.getEmail() + "') " +
						"OR (payee = '" + payer.getEmail() + "' AND payer = '" + payee.getEmail() + "'))"
								+ "AND type='I' AND status = 'C' AND paymentid < 0 ORDER BY timestamp";
		try
		{
			List<GzInvoice> invoices = getJdbcTemplate().query(sql, new GzInvoiceRowMapper());
			return invoices;
		}
		catch (EmptyResultDataAccessException e)
		{
			return new ArrayList<GzInvoice>();
		}
	}
	
	@Override
	public Map<UUID,Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException
	{
		String sql = "SELECT email FROM baseuser WHERE parentcode = '" + user.getCode() + "'";
		Map<UUID,Double> map = new HashMap<UUID,Double>();
		try
		{
			List<String> members = getJdbcTemplate().queryForList(sql,String.class);
			for (String member : members)
			{
				sql = "SELECT sum(netamount) FROM xaction WHERE type='I' AND payee = '" + user.getEmail() + "' AND payer = '" + member + "'";
				Double payIn = getJdbcTemplate().queryForObject(sql,Double.class);
				sql = "SELECT sum(netamount) FROM xaction WHERE type='I' AND payer = '" + user.getEmail() + "' AND payee = '" + member + "'";
				Double payOut = getJdbcTemplate().queryForObject(sql,Double.class);
				sql = "SELECT id FROM baseuser WHERE email = '" + member + "'";
				UUID id = getJdbcTemplate().queryForObject(sql,UUID.class);
				
				if (payIn == null)
					payIn = 0.0;
				if (payOut == null)
					payOut = 0.0;
				
				map.put(id,payIn-payOut);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException("Could not execute : "+ sql + " - " + e.getMessage());
		}
		return map;
	}
	
	@Override
	public void updateTransaction(final GzTransaction trans) throws GzPersistenceException {
		
		try
		{
			getJdbcTemplate().update("UPDATE transaction SET invoiceid = ? WHERE ID = ?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateTransaction) throws SQLException {
							psUpdateTransaction.setLong(1,trans.getInvoiceId());
							psUpdateTransaction.setLong(2,trans.getId());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public Long getOutstandingTransactionCount()
	{
		String sql = "SELECT COUNT(*) FROM transaction WHERE invoiceid < 0";
		Long count = getJdbcTemplate().queryForObject(sql,Long.class);
		
		return count;
	}

	@Override
	public synchronized void storeInvoice(final GzInvoice invoice) throws GzPersistenceException {
		
		final Timestamp t1 = new Timestamp(invoice.getTimestamp().getTime());
		final Timestamp dueDate = new Timestamp(invoice.getDueDate().getTime());
		
		try
		{
			getJdbcTemplate().update("INSERT INTO xaction (payer,payee,type,amount,commission,netamount,timestamp,duedate,status,paymentid)"
					+ "VALUES( ?,?,?,?,?,?,?,?,?,? )"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psStoreInvoice) throws SQLException {
							psStoreInvoice.setString(1,invoice.getPayer());
							psStoreInvoice.setString(2,invoice.getPayee());
							psStoreInvoice.setString(3,String.valueOf(invoice.getType()));
							psStoreInvoice.setDouble(4,invoice.getAmount());
							psStoreInvoice.setDouble(5,invoice.getCommission());
							psStoreInvoice.setDouble(6,invoice.getNetAmount());
							psStoreInvoice.setTimestamp(7,t1);
							psStoreInvoice.setTimestamp(8,dueDate);
							psStoreInvoice.setString(9,String.valueOf('O'));
							psStoreInvoice.setLong(10,-1L);

						}
					});
			getXactionId(invoice);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public synchronized void storePayment(final GzPayment payment) throws GzPersistenceException {
		
		final Timestamp t1 = new Timestamp(payment.getTimestamp().getTime());
		
		try
		{
			getJdbcTemplate().update("INSERT INTO xaction (payer,payee,type,amount,commission,netamount,timestamp,invoiceid)"
					+ "VALUES( ?,?,?,?,?,?,?,? )"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psStorePayment) throws SQLException {
							psStorePayment.setString(1,payment.getPayer());
							psStorePayment.setString(2,payment.getPayee());
							psStorePayment.setString(3,String.valueOf(payment.getType()));
							psStorePayment.setDouble(4,payment.getAmount());
							psStorePayment.setDouble(5,payment.getCommission());
							psStorePayment.setDouble(6,payment.getNetAmount());
							psStorePayment.setTimestamp(7,t1);
							psStorePayment.setLong(8,payment.getInvoiceId());

						}
					});
			getXactionId(payment);
			updateInvoicePaymentId(payment.getId(),payment.getInvoiceId(),t1);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	private void updateInvoicePaymentId(final long paymentId,final long invoiceId, final Timestamp t1) throws GzPersistenceException
	{
		try
		{
			getJdbcTemplate().update("UPDATE xaction SET paymentid = ?,paymentdate = ? WHERE id = ?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateInvoicePaymentId) throws SQLException {
							psUpdateInvoicePaymentId.setLong(1,paymentId);
							psUpdateInvoicePaymentId.setTimestamp(2,t1);
							psUpdateInvoicePaymentId.setLong(3,invoiceId);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	private synchronized void storeBaseTransaction(final GzBaseTransaction xaction) throws GzPersistenceException {
		
		final Timestamp t1 = new Timestamp(xaction.getTimestamp().getTime());
		try
		{
			getJdbcTemplate().update("INSERT INTO xaction (payer,payee,type,amount,timestamp)"
					+ "VALUES( ?,?,?,?,?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psStoreBaseTransaction) throws SQLException {
							psStoreBaseTransaction.setString(1,xaction.getPayer());
							psStoreBaseTransaction.setString(2,xaction.getPayee());
							psStoreBaseTransaction.setString(3,String.valueOf(xaction.getType()));
							psStoreBaseTransaction.setDouble(4,xaction.getAmount());
							psStoreBaseTransaction.setTimestamp(5,t1);
						}
					});
			getXactionId(xaction);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	private synchronized void getXactionId(GzBaseTransaction xaction)
	{
		String sql = "SELECT MAX(id) FROM xaction";
		Long id = getJdbcTemplate().queryForObject(sql,Long.class);
		xaction.setId(id);
	}
	
	@Override
	public GzRollup getRollupForUser(String userId,String code,GzRole role)
	{	
		GzRollup rollup = new GzRollup(userId,code,role);
		
		String sql = "SELECT SUM(netamount) FROM xaction WHERE TYPE='I' AND payee='" + userId + "' AND paymentId>0";
		rollup.setPaidIn(getRollupValue(sql));
		
		sql = "SELECT SUM(netamount) FROM xaction WHERE TYPE='I'  AND payer='" + userId + "' AND paymentId>0";
		rollup.setPaidOut(getRollupValue(sql));
	
		sql = "SELECT SUM(netamount) FROM xaction WHERE TYPE='I' AND STATUS='C' AND payee='" + userId + "' AND paymentId<=0";
		rollup.setOwed(getRollupValue(sql));
		
		sql = "SELECT SUM(netamount) FROM xaction WHERE TYPE='I' AND STATUS='C' AND payer='" + userId + "' AND paymentId<=0";
		rollup.setOwing(getRollupValue(sql));
		
		sql = "SELECT SUM(amount) FROM xaction WHERE TYPE='W' AND payee='" + userId + "' AND paymentId<=0";
		rollup.setWithdrawl(getRollupValue(sql));
		
		sql = "SELECT SUM(amount) FROM xaction WHERE TYPE='D' AND payee='" + userId + "' AND paymentId<=0";
		rollup.setDeposit(getRollupValue(sql));
		
		rollup.calcTotal();
		return rollup;
	}
	
	private double getRollupValue(String sql)
	{
		try
		{
			log.info("getRollupValue : " + sql);
			Double result = getJdbcTemplate().queryForObject(sql,Double.class);
			if (result == null)
				return 0.0;
			return result;
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0.0;
		}
	}
	
	@Override
	public List<GzRollup> getMemberRollups(GzBaseUser currUser) {
		String sql = "SELECT email,code,role FROM baseuser WHERE parentcode = "
				+ "'" + currUser.getCode() + "' ORDER BY email";
			List<GzRollup> rList = new  ArrayList<GzRollup>();
			
			try
			{
				log.trace("getMemberRollups : " + sql);
				List<UserCodeMap> members = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(UserCodeMap.class));
				for (UserCodeMap member : members)
				{
					GzRole role = GzRole.valueOf(member.getRole());
					GzRollup rollup = getRollupForUser(member.getEmail(),member.getCode(),role);
					if (!rollup.isEmpty())
						rList.add(rollup);
				}
			}
			catch (EmptyResultDataAccessException e)
			{
				;
			}
			return rList;
	}

	@Override
	public Long getXactionCount(GzBaseUser currUser) {
		String sql = "SELECT COUNT(*) FROM xaction WHERE payer = '" + currUser.getEmail() + "' OR payee='" + currUser.getEmail() + "'";
		Long count = getJdbcTemplate().queryForObject(sql,Long.class);
		return count;
	}

	@Override
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException {
	
		if (dwType.equals("Deposit"))
		{
			GzDeposit deposit = new GzDeposit(currAccountUser.getEmail(),dwAmount,(new GregorianCalendar()).getTime());
			storeBaseTransaction(deposit);
			currAccountUser.getAccount().addBalance(dwAmount);
		}
		else
		{
			GzWithdrawl withdrawl = new GzWithdrawl(currAccountUser.getEmail(),dwAmount,(new GregorianCalendar()).getTime());
			storeBaseTransaction(withdrawl);
			currAccountUser.getAccount().addBalance(dwAmount*-1);
		}
		updateAccountBalance(currAccountUser.getAccount());
	}

	private void updateAccountBalance(final GzAccount account) throws GzPersistenceException {
		// BALANCE DONE INDEPENDENTLY
		try
		{
			getJdbcTemplate().update("UPDATE account set balance=? WHERE baseuserid=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateAccount) throws SQLException {
							psUpdateAccount.setDouble(1,account.getBalance());
							psUpdateAccount.setObject(2,account.getBaseUser().getId());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public GzInvoice getOpenInvoice(final String payer, final String payee) throws GzPersistenceException {
		
		try
		{
			List<GzInvoice> invoices = getJdbcTemplate().query("SELECT * FROM xaction WHERE payer = ? "
					+ "AND payee = ? AND TYPE='I' AND status ='O' LIMIT 1"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement psGetOpenInvoice) throws SQLException {
							psGetOpenInvoice.setString(1,payer);
							psGetOpenInvoice.setString(2,payee);
			      }
			    },new GzInvoiceRowMapper());
			if (invoices.size()==0)
				return null;
			return invoices.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void updateInvoice(final GzInvoice invoice) throws GzPersistenceException {
		
		try
		{
			getJdbcTemplate().update("UPDATE xaction SET amount=?,commission=?,netAmount=? WHERE id=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateInvoice) throws SQLException {
							psUpdateInvoice.setDouble(1,invoice.getAmount());
							psUpdateInvoice.setDouble(2,invoice.getCommission());
							psUpdateInvoice.setDouble(3,invoice.getNetAmount());
							psUpdateInvoice.setLong(5,invoice.getId());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updateInvoice(final double amount,final double commission,final double netAmount,
											final long id) throws GzPersistenceException {
		
		try
		{
			getJdbcTemplate().update("UPDATE xaction SET amount=amount + ?,commission=commission + ?,netAmount=netAmount + ? WHERE id=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateInvoice) throws SQLException {
							psUpdateInvoice.setDouble(1,amount);
							psUpdateInvoice.setDouble(2,commission);
							psUpdateInvoice.setDouble(3,netAmount);
							psUpdateInvoice.setLong(4,id);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void updateSubInvoice(final GzInvoice subInvoice, final GzInvoice invoice) throws GzPersistenceException {
		
//		log.info("subId : " + subInvoice.getId() + " id: " + invoice.getId());
		try
		{
			getJdbcTemplate().update("UPDATE xaction SET parentid=? WHERE id=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement psUpdateSubInvoice) throws SQLException {
							psUpdateSubInvoice.setLong(1,invoice.getId());
							psUpdateSubInvoice.setLong(2,subInvoice.getId());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException {
		String sql = "UPDATE xaction SET status='" + status  + "' WHERE id=" + invoice.getId();
		doUpdate(sql);
	}

	@Override
	public void closeOpenInvoices() throws GzPersistenceException {
		String sql = "UPDATE xaction SET status='C' WHERE type ='I' and status='O'";
		doUpdate(sql);
	}
	
	private void doUpdate(String sql) throws GzPersistenceException
	{
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}		
	}

	@Override
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException {

		String sql = "SELECT * FROM xaction WHERE id = " + invoiceNum;
		try
		{
			log.trace(sql);
			GzInvoice ac = getJdbcTemplate().queryForObject(sql,new GzInvoiceRowMapper());
			return ac;
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}
	}

	@Override
	public GzPayment getPaymentForId(long paymentNum) throws GzPersistenceException {

		if (paymentNum<0)
			return null;
		
		String sql = "SELECT * FROM xaction WHERE id = " + paymentNum;
		try
		{
			log.trace(sql);
			GzPayment pm = getJdbcTemplate().queryForObject(sql,new GzPaymentRowMapper());
			return pm;
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}
	}
	
	@Override
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException {
		String sql = "SELECT * FROM xaction WHERE parentid = " + invoice.getId();
		try
		{
			log.trace(sql);
			List<GzInvoice> ac = getJdbcTemplate().query(sql,new GzInvoiceRowMapper());
			return ac;
		}
		catch (EmptyResultDataAccessException e)
		{
			return new ArrayList<GzInvoice>();
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}
	}

	@Override
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException {
		String sql = "SELECT * FROM transaction WHERE invoiceid = " + invoice.getId();
		try
		{
//			log.info(sql);
			List<GzTransaction> ac = getJdbcTemplate().query(sql,new GzTransactionRowMapper());
//			log.info("got : " + ac.size() + " transactions");
			return ac;
		}
		catch (EmptyResultDataAccessException e)
		{
			return new ArrayList<GzTransaction>();
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}
	}	
	
	@Override
	public double getHigestDownstreamCommission(char type,String code) throws GzPersistenceException
	{
		String field = "betcommission";
		if (type=='W')
			field = "wincommission";
			
		String sql = "SELECT MAX(" + field + ") FROM account JOIN  baseuser AS b ON b.id=account.baseuserid"
				+ " WHERE b.parentcode = '" + code + "'";
		try
		{
			log.info(sql);
			Double max = getJdbcTemplate().queryForObject(sql,Double.class);
			if (max==null)
				return 0.0;
			return max;
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0.0;
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}
	}

	@Override
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException {
		String sql = "SELECT SUM(balance) FROM account WHERE baseuserid IN " +
				"(SELECT id FROM baseuser WHERE parentcode='" + parent.getCode() +"') AND " +
				"baseuserid <> '" + user.getId() +"'";
	try
	{
		log.info("sql = "  + sql );
		Double total = getJdbcTemplate().queryForObject(sql,Double.class);
		if (total == null)
			total = 0.0;
		return total;
	}
	catch (Exception e)
	{
		e.printStackTrace();
		log.error("getDownStreamAccount : " + e + " - " + e.getMessage());
		return 0.0;
	}
	}
	
}
