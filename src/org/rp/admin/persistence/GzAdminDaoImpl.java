package org.rp.admin.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.admin.GzAdminProperties;
import org.rp.baseuser.persistence.GzBaseUserDaoImpl;
import org.rp.home.persistence.GzPersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class GzAdminDaoImpl extends GzBaseUserDaoImpl implements GzAdminDao {

	private static final Logger log = Logger.getLogger(GzAdminDaoImpl.class);

	@Override
	public void store(GzAdmin admin) throws GzPersistenceException
	{
		super.storeBaseUser(admin);
	}

	@Override
	public GzAdmin getAdminByEmail(String email) throws GzPersistenceException {
		GzAdmin admin = (GzAdmin) super.getBaseUserByEmail(email,GzAdmin.class);
		super.getMembersForUser(admin);
		return admin;
	}
	
	@Override
	public GzAdminProperties getAdminProperties() throws GzPersistenceException {
		try
		{
			final String sql = "SELECT * FROM admin LIMIT 1";
			GzAdminProperties ad = getJdbcTemplate().queryForObject(sql,new GzAdminPropertiesRowMapper());
			return ad;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void update(GzAdmin admin) throws GzPersistenceException {
		super.updateBaseUserProfile(admin);
	}

	@Override
	public boolean getScheduledDownTimeSet() throws GzPersistenceException {
		try
		{
			final String sql = "SELECT scheduleddowntimeset FROM admin LIMIT 1";
			return getJdbcTemplate().queryForObject(sql,Boolean.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void setScheduledDownTime(final boolean set) throws GzPersistenceException {
		try
		{
			getJdbcTemplate().update("UPDATE admin set scheduleddowntimeset=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBoolean(1,set);
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
	public void storeScheduledDownTime(final Date date) throws GzPersistenceException {
		final Timestamp t1 = new Timestamp(date.getTime());
		try
		{
			getJdbcTemplate().update("UPDATE admin set scheduleddowntime = ?,scheduleddowntimeset=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setTimestamp(1,t1);
							ps.setBoolean(2,true);
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
	public Date getScheduledDownTime() throws GzPersistenceException {
		try
		{
			final String sql = "SELECT scheduleddowntime FROM admin LIMIT 1";
			return getJdbcTemplate().queryForObject(sql,Date.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void updateVersionCode(final UUID uuid) throws GzPersistenceException {
		try
		{
			getJdbcTemplate().update("UPDATE admin set versioncode = ?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setObject(1,uuid);
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
	public UUID getVersionCode() throws GzPersistenceException {
		try
		{
			final String sql = "SELECT versioncode FROM admin LIMIT 1";
			String uuid = getJdbcTemplate().queryForObject(sql,String.class);
			return UUID.fromString(uuid);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

}
