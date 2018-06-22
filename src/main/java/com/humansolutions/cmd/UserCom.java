package com.humansolutions.cmd;

import java.util.Date;

public class UserCom {
	
	private String username;
	
	private String password;
	
	private int sessionNumber;
	
	private int enabled;
	
	private double earnings;
	
	private String authority;
	
	private String sessionStatus;
	
	private String treatmentMsgType;
	
	private String treatmentMsg;
	
	private int imageId;
	
	private long diffInDays;
	
	private int isFirstLogin;
	
	private Date startDate;
	
	private int id;
	
	private int endTask;

	public int getEndTask() {
		return endTask;
	}

	public void setEndTask(int endTask) {
		this.endTask = endTask;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public int getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(int isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public long getDiffInDays() {
		return diffInDays;
	}

	public void setDiffInDays(long diffInDays) {
		this.diffInDays = diffInDays;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getTreatmentMsg() {
		return treatmentMsg;
	}

	public void setTreatmentMsg(String treatmentMsg) {
		this.treatmentMsg = treatmentMsg;
	}

	public String getTreatmentMsgType() {
		return treatmentMsgType;
	}

	public void setTreatmentMsgType(String treatmentMsgType) {
		this.treatmentMsgType = treatmentMsgType;
	}

	public String getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSessionNumber() {
		return sessionNumber;
	}

	public void setSessionNumber(int sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public double getEarnings() {
		return earnings;
	}

	public void setEarnings(double earnings) {
		this.earnings = earnings;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
