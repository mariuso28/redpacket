package org.rp.web.admin;

import java.io.Serializable;
import java.util.Date;

public class GzAdminForm implements Serializable
{
	private static final long serialVersionUID = -3663041507565608823L;
	private GzAdminCommand command;
	private Date scheduledDownTime;
	private boolean downTimeSet;
	private String errMsg;
	private String infoMsg;
	
	public GzAdminForm()
	{
	}

	public GzAdminCommand getCommand() {
		return command;
	}

	public void setCommand(GzAdminCommand command) {
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
	
}
