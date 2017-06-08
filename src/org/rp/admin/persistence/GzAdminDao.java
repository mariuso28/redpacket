package org.rp.admin.persistence;

import java.util.Date;
import java.util.UUID;

import org.rp.admin.GzAdmin;
import org.rp.admin.GzAdminProperties;
import org.rp.baseuser.persistence.GzBaseUserDao;
import org.rp.home.persistence.GzPersistenceException;

public interface GzAdminDao extends GzBaseUserDao{
	
	public void store(GzAdmin admin) throws GzPersistenceException;
	public GzAdmin getAdminByEmail(String email) throws GzPersistenceException;
	public void update(GzAdmin admin) throws GzPersistenceException;
	public boolean getScheduledDownTimeSet() throws GzPersistenceException;
	public void setScheduledDownTime(boolean set) throws GzPersistenceException;
	public void storeScheduledDownTime(Date date) throws GzPersistenceException;
	public Date getScheduledDownTime() throws GzPersistenceException;
	public GzAdminProperties getAdminProperties() throws GzPersistenceException;
	public void updateVersionCode(UUID uuid) throws GzPersistenceException;
	public UUID getVersionCode() throws GzPersistenceException;
}
