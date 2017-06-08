package org.rp.admin;

import java.util.Date;
import java.util.UUID;

public class GzAdminProperties{

	private boolean creditAsBankerOn;
	private boolean creditAsPlayerOn;
	private boolean betCommissionOn;
	private boolean winCommissionOn;
	private Date scheduledDownTime;
	private boolean scheduledDownTimeSet;
	private UUID versionCode;
	
	public GzAdminProperties()
	{
	}

	public boolean isCreditAsBankerOn() {
		return creditAsBankerOn;
	}

	public void setCreditAsBankerOn(boolean creditAsBankerOn) {
		this.creditAsBankerOn = creditAsBankerOn;
	}

	public boolean isCreditAsPlayerOn() {
		return creditAsPlayerOn;
	}

	public void setCreditAsPlayerOn(boolean creditAsPlayerOn) {
		this.creditAsPlayerOn = creditAsPlayerOn;
	}

	public boolean isBetCommissionOn() {
		return betCommissionOn;
	}

	public void setBetCommissionOn(boolean betCommissionOn) {
		this.betCommissionOn = betCommissionOn;
	}

	public boolean isWinCommissionOn() {
		return winCommissionOn;
	}

	public void setWinCommissionOn(boolean winCommissionOn) {
		this.winCommissionOn = winCommissionOn;
	}

	public Date getScheduledDownTime() {
		return scheduledDownTime;
	}

	public void setScheduledDownTime(Date scheduledDownTime) {
		this.scheduledDownTime = scheduledDownTime;
	}

	public boolean isScheduledDownTimeSet() {
		return scheduledDownTimeSet;
	}

	public void setScheduledDownTimeSet(boolean scheduledDownTimeSet) {
		this.scheduledDownTimeSet = scheduledDownTimeSet;
	}

	public UUID getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(UUID versionCode) {
		this.versionCode = versionCode;
	}
	
	
}
