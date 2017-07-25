package com.TaskBuddy.Models;

import java.util.Date;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'Users'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class User {
	private int userId;
	private String userFirstName;
	private String userLastName;
	private String userImage;
	private Date userCreatedDate;
	private boolean isUserDeleted;
	private double totalScore;
	private double currentScore;
	private double currentPoints;
	private double weeklyPoints;
	private long fbId;

	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	
	public Date getUserCreatedDate() {
		return userCreatedDate;
	}
	public void setUserCreatedDate(Date userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}
	public boolean isUserDeleted() {
		return isUserDeleted;
	}
	public void setUserDeleted(boolean isUserDeleted) {
		this.isUserDeleted = isUserDeleted;
	}
	public double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	public double getCurrentScore() {
		return currentScore;
	}
	public void setCurrentScore(double currentScore) {
		this.currentScore = currentScore;
	}
	public double getCurrentPoints() {
		return currentPoints;
	}
	public void setCurrentPoints(double currentPoints) {
		this.currentPoints = currentPoints;
	}
	public double getWeeklyPoints() {
		return weeklyPoints;
	}
	public void setWeeklyPoints(double weeklyPoints) {
		this.weeklyPoints = weeklyPoints;
	}
	public long getFbId(){
		return fbId;
	}
	public void setFbId(long fbId){
		this.fbId = fbId;	
	}
}
