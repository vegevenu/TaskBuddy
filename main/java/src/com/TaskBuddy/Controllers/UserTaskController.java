package com.TaskBuddy.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.Models.UserTask;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author venu
 *
 * Controller class for UserTasks table
 *
 */
@XmlRootElement
public class UserTaskController {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String selectSQL = "SELECT " +
			"user_id, task_id, " +
			"task_assigned_date, is_task_assigned" +
			" FROM UserTasks "
			+ " WHERE is_task_assigned = true ";
	
	private UserTaskController() {
	}
	
	/**
	 * 
	 * Method to return all UserTasks
	 * 
	 * @return ArrayList of all UserTasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<UserTask> getAllUserTasks() throws SQLException {
		
		String sql = selectSQL;
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<UserTask> userTasksList = new ArrayList<UserTask>();
			
			while (rs.next()) {
				userTasksList.add(processResultSetIntoUserTaskRow(rs));
			}
			
			return userTasksList;
		}
	}
	
	/**
	 * 
	 * Method to return all UserTasks with Users (userId) who are assigned the given Task (taskId)
	 * 
	 * @param taskId
	 * @return ArrayList of UserTasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<UserTask> getAllUsersByTaskId(int taskId) throws SQLException {
		
		String sql = selectSQL +
				" AND task_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, taskId);
			rs = stmt.executeQuery();
			
			ArrayList<UserTask> userTasksList = new ArrayList<UserTask>();
			
			while (rs.next()) {
				userTasksList.add(processResultSetIntoUserTaskRow(rs));
			}
			
			return userTasksList;
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to return all UserTasks with Tasks (taskId) assigned to the given User (userId)
	 * 
	 * @param userId
	 * @return ArrayList of UserTasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<UserTask> getAllTasksByUserId(int userId) throws SQLException {
		
		String sql = selectSQL +
				" AND user_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			
			ArrayList<UserTask> userTasksList = new ArrayList<UserTask>();
			
			while (rs.next()) {
				userTasksList.add(processResultSetIntoUserTaskRow(rs));
			}
			
			return userTasksList;
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to return UserTask for the given userId and taskId
	 * 
	 * @param userId, taskId
	 * @return UserTask instance
	 * @throws SQLException
	 * 
	 */
	public static UserTask getUserTaskByUserIdAndTaskId(int userId, int taskId) throws SQLException {
		
		String sql = selectSQL +
				" AND user_id = ? AND task_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userId);
			stmt.setInt(2, taskId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoUserTaskRow(rs);
			} else {
				return null;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to insert new UserTask row
	 * 
	 * @param userTaskRow
	 * @return boolean of whether the row is inserted successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean insertUserTask(UserTask userTaskRow) throws SQLException {
		
		String sql = "INSERT INTO UserTasks (user_id, task_id, " +
				"task_assigned_date, is_task_assigned) " +
				"VALUES (?, ?, ?, ?)";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userTaskRow.getUserId());
			stmt.setInt(2, userTaskRow.getTaskId());
			stmt.setTimestamp(3, new Timestamp(userTaskRow.getTaskAssignedDate().getTime()));
			stmt.setBoolean(4, userTaskRow.isTaskAssigned());
			
			int affected_rows = stmt.executeUpdate();
			
			if (affected_rows == 1) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 
	 * Method to update existing UserTask row
	 * 
	 * @param userTaskRow
	 * @return boolean of whether the row is updated successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean updateUserTask(UserTask userTaskRow) throws SQLException {
		
		String sql = "UPDATE UserTasks SET " +
				"task_assigned_date = ?, is_task_assigned = ?" +
				" WHERE user_id = ? AND task_id = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setTimestamp(1, new Timestamp(userTaskRow.getTaskAssignedDate().getTime()));
			stmt.setBoolean(2, userTaskRow.isTaskAssigned());
			stmt.setInt(3, userTaskRow.getUserId());
			stmt.setInt(4, userTaskRow.getTaskId());
			
			int affected_rows = stmt.executeUpdate();
			
			if (affected_rows == 1) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 
	 * Method to save UserTask row
	 * 
	 * @param userTaskRow
	 * @return boolean of whether or not the row is saved successfully
	 * @throws SQLException
	 * 
	 */
	public static boolean save(UserTask userTaskRow) throws SQLException {
		return checkUserTaskExists(userTaskRow.getUserId(), userTaskRow.getTaskId()) != null ? updateUserTask(userTaskRow) : insertUserTask(userTaskRow);
	}
	
	/**
	 * 
	 * Method to check if userTaskRow exists
	 * 
	 * Code is duplicated from getUserTaskByUserIdAndTaskId(int, int) 
	 * to include records having is_task_assigned both true and false 
	 * 
	 * @param userId, taskId
	 * @return UserTask instance
	 * @throws SQLException
	 * 
	 */
	public static UserTask checkUserTaskExists(int userId, int taskId) throws SQLException {
		
		String sql = "SELECT " +
				"user_id, task_id, " +
				"task_assigned_date, is_task_assigned" +
				" FROM UserTasks "
				+ " WHERE user_id = ? AND task_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userId);
			stmt.setInt(2, taskId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoUserTaskRow(rs);
			} else {
				return null;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to Convert ResultSet into UserTask instance
	 * 
	 * @param ResultSet instance rs
	 * @return UserTask instance
	 * @throws SQLException
	 * 
	 */
	protected static UserTask processResultSetIntoUserTaskRow(ResultSet rs) throws SQLException {
		UserTask userTaskRow = new UserTask();
		
		userTaskRow.setUserId(rs.getInt("user_id"));
		userTaskRow.setTaskId(rs.getInt("task_id"));
		userTaskRow.setTaskAssignedDate(rs.getTimestamp("task_assigned_date"));
		userTaskRow.setTaskAssigned(rs.getBoolean("is_task_assigned"));
		
		return userTaskRow;
	}
}
