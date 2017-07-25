package com.TaskBuddy.Models;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'GroupTasks'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class GroupTask {
	private int groupId;
	private int taskId;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
}
