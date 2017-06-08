package org.rp.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.rp.account.GzTransaction;
import org.springframework.jdbc.core.RowMapper;

public class GzTransactionRowMapper implements RowMapper<GzTransaction>
{
	@Override
	public GzTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		GzTransaction trans = new GzTransaction();
		
		trans.setId(rs.getLong("id"));
		trans.setAmount(rs.getDouble("amount"));
		trans.setPayee(rs.getString("payee"));
		trans.setPayer(rs.getString("payer"));
		trans.setInvoiceId(rs.getLong("invoiceid"));
		trans.setPlayGameId(UUID.fromString(rs.getString("playgameid")));
		Timestamp ts = rs.getTimestamp("timestamp");
		trans.setTimestamp(new Date(ts.getTime()));
		trans.setType(rs.getString("type").charAt(0));
		return trans;
	}

}
