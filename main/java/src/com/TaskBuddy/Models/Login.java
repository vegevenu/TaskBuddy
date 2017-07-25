package com.TaskBuddy.Models;

/**
 * @author Siddhardha
 * 
 * Model class for the table 'Logins'.
 * Each private member represents a column of the table.
 * The private members must be accessed using the public getter and setter methods. 
 *
 */
public class Login {
	private int loginId;
	private int userId;
	private String userName;
	private String userPassWord;
	private String userRole;
	
	public int getLoginId() {
		return loginId;
	}
	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassWord() {
		return userPassWord;
	}
	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
}
