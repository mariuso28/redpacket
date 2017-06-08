package org.rp.web.admin;

import java.io.Serializable;
import java.util.List;

import org.rp.baseuser.GzProfile;

public class GzAdminCommand implements Serializable
{
	private static final long serialVersionUID = -804903870498969809L;
	private String verifyPassword;
	private GzProfile profile;
	private List<String> durations;
	private String gameId;
	private int durationIndex;
	

	public GzAdminCommand()
	{
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public GzProfile getProfile() {
		return profile;
	}

	public void setProfile(GzProfile profile) {
		this.profile = profile;
	}

	public List<String> getDurations() {
		return durations;
	}

	public void setDurations(List<String> durations) {
		this.durations = durations;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getDurationIndex() {
		return durationIndex;
	}

	public void setDurationIndex(int durationIndex) {
		this.durationIndex = durationIndex;
	}
	
}
