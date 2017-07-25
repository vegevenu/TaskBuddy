package com.TaskBuddy.Models;

import java.util.Date;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'Tasks'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class Task {
	private int taskId;
	private String taskTitle;
	private String taskDescription;
	private double taskOriginalPointValue;
	private double taskUpdatedPointValue;
	private int taskCreatedBy;
	private Date taskCreatedDate;
	private Date taskDueDate;
	private boolean isTaskCompleted;
	private boolean isTaskDeleted;
	private String taskRepetition;
	private boolean isTaskMaster;
	private int taskDueDuration;
	private boolean isTaskOverdue;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public double getTaskOriginalPointValue() {
		return taskOriginalPointValue;
	}
	public void setTaskOriginalPointValue(double taskOriginalPointValue) {
		this.taskOriginalPointValue = taskOriginalPointValue;
	}
	public double getTaskUpdatedPointValue() {
		return taskUpdatedPointValue;
	}
	public void setTaskUpdatedPointValue(double taskUpdatedPointValue) {
		this.taskUpdatedPointValue = taskUpdatedPointValue;
	}
	public int getTaskCreatedBy() {
		return taskCreatedBy;
	}
	public void setTaskCreatedBy(int taskCreatedBy) {
		this.taskCreatedBy = taskCreatedBy;
	}
	public Date getTaskCreatedDate() {
		return taskCreatedDate;
	}
	public void setTaskCreatedDate(Date taskCreatedDate) {
		this.taskCreatedDate = taskCreatedDate;
	}
	public Date getTaskDueDate() {
		return taskDueDate;
	}
	public void setTaskDueDate(Date taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
	public boolean isTaskCompleted() {
		return isTaskCompleted;
	}
	public void setTaskCompleted(boolean isTaskCompleted) {
		this.isTaskCompleted = isTaskCompleted;
	}
	public boolean isTaskDeleted() {
		return isTaskDeleted;
	}
	public void setTaskDeleted(boolean isTaskDeleted) {
		this.isTaskDeleted = isTaskDeleted;
	}
	public String getTaskRepetition() {
		return taskRepetition;
	}
	public void setTaskRepetition(String taskRepetition) {
		this.taskRepetition = taskRepetition;
	}
	public boolean isTaskMaster() {
		return isTaskMaster;
	}
	public void setTaskMaster(boolean isTaskMaster) {
		this.isTaskMaster = isTaskMaster;
	}
	public int getTaskDueDuration() {
		return taskDueDuration;
	}
	public void setTaskDueDuration(int taskDueDuration) {
		this.taskDueDuration = taskDueDuration;
	}
	public boolean isTaskOverdue() {
		return isTaskOverdue;
	}
	public void setTaskOverdue(boolean isTaskOverdue) {
		this.isTaskOverdue = isTaskOverdue;
	}
}
