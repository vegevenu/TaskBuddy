package com.TaskBuddy.Models;

import java.util.Date;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'UserTasks'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class UserTask {
	private int userId;
	private int taskId;
	private Date taskAssignedDate;
	private boolean isTaskAssigned;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public Date getTaskAssignedDate() {
		return taskAssignedDate;
	}
	public void setTaskAssignedDate(Date taskAssignedDate) {
		this.taskAssignedDate = taskAssignedDate;
	}
	public boolean isTaskAssigned() {
		return isTaskAssigned;
	}
	public void setTaskAssigned(boolean isTaskAssigned) {
		this.isTaskAssigned = isTaskAssigned;
	}
}
