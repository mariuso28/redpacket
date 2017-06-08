package org.rp.web.admin;

import java.io.Serializable;
import java.util.Date;

public class GzAdminDownTimeCommand implements Serializable
{
	private static final long serialVersionUID = 490462843065441967L;
	private Date scheduledDownTime;
	private String versionCode;

	public GzAdminDownTimeCommand()
	{
	}

	public Date getScheduledDownTime() {
		return scheduledDownTime;
	}

	public void setScheduledDownTime(Date scheduledDownTime) {
		this.scheduledDownTime = scheduledDownTime;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

}
