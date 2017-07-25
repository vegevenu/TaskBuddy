package com.TaskBuddy.Models;

import java.util.Date;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'GroupMemberships'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class GroupMembership {
	private int groupId;
	private int userId;
	private Date userJoinedDate;
	private boolean hasUserUnjoined;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getUserJoinedDate() {
		return userJoinedDate;
	}
	public void setUserJoinedDate(Date userJoinedDate) {
		this.userJoinedDate = userJoinedDate;
	}
	public boolean hasUserUnjoined() {
		return hasUserUnjoined;
	}
	public void setHasUserUnjoined(boolean hasUserUnjoined) {
		this.hasUserUnjoined = hasUserUnjoined;
	}
}
