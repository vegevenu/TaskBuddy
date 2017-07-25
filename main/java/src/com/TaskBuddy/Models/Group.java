package com.TaskBuddy.Models;

import java.util.Date;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'Groups'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class Group {
	private int groupId;
	private String groupName;
	private int groupAdminUserId;
	private String groupImage;
	private Date groupCreatedDate;
	private boolean isGroupDeleted;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGroupAdminUserId() {
		return groupAdminUserId;
	}
	public void setGroupAdminUserId(int groupAdminUserId) {
		this.groupAdminUserId = groupAdminUserId;
	}
	public String getGroupImage() {
		return groupImage;
	}
	public void setGroupImage(String groupImage) {
		this.groupImage = groupImage;
	}
	public Date getGroupCreatedDate() {
		return groupCreatedDate;
	}
	public void setGroupCreatedDate(Date groupCreatedDate) {
		this.groupCreatedDate = groupCreatedDate;
	}
	public boolean isGroupDeleted() {
		return isGroupDeleted;
	}
	public void setGroupDeleted(boolean isGroupDeleted) {
		this.isGroupDeleted = isGroupDeleted;
	}
}
