package org.rp.web.admin;

import java.io.Serializable;
import java.util.Date;

public class GzAdminDownTimeForm implements Serializable
{
	private static final long serialVersionUID = -2794190034279852396L;
	private GzAdminDownTimeCommand command;
	private String errMsg;
	private String infoMsg;
	private Date scheduledDownTime;
	private boolean downTimeSet;
	private String versionCode;
	
	public GzAdminDownTimeForm()
	{
	}

	public GzAdminDownTimeCommand getCommand() {
		return command;
	}

	public void setCommand(GzAdminDownTimeCommand command) {
		this.command = command;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public Date getScheduledDownTime() {
		return scheduledDownTime;
	}

	public void setScheduledDownTime(Date scheduledDownTime) {
		this.scheduledDownTime = scheduledDownTime;
	}

	public boolean isDownTimeSet() {
		return downTimeSet;
	}

	public void setDownTimeSet(boolean downTimeSet) {
		this.downTimeSet = downTimeSet;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

}
