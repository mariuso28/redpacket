package org.rp.admin.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.rp.admin.GzAdminProperties;
import org.springframework.jdbc.core.RowMapper;

public class GzAdminPropertiesRowMapper implements RowMapper<GzAdminProperties>{
	
	public GzAdminPropertiesRowMapper()
	{
	}
	
	public GzAdminProperties mapRow(ResultSet rs,int rowNum) {
	
		try
		{
			GzAdminProperties admin = new GzAdminProperties();
			setValues(rs,admin);
			return admin;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void setValues(ResultSet rs,GzAdminProperties admin) throws SQLException {
		
		admin.setBetCommissionOn(rs.getBoolean("betcommissionon"));
		admin.setWinCommissionOn(rs.getBoolean("wincommissionon"));
		admin.setCreditAsBankerOn(rs.getBoolean("creditasbankeron"));
		admin.setCreditAsPlayerOn(rs.getBoolean("creditasplayeron"));
		admin.setScheduledDownTimeSet(rs.getBoolean("scheduleddowntimeset"));
		Timestamp ts = rs.getTimestamp("scheduleddowntime");
		admin.setScheduledDownTime(new Date(ts.getTime()));	
		admin.setVersionCode(UUID.fromString(rs.getString("versioncode")));
	}

}
