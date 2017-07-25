package com.TaskBuddy.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.Models.Task;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author venu
 *
 * Controller class for Tasks table
 *
 */
@XmlRootElement
public class TaskController {

	
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String selectSQL = "SELECT "
			+ "task_id, task_title, task_description, task_original_point_value, task_updated_point_value, "
			+ "task_created_by, task_created_date, task_due_date, is_task_completed, is_task_deleted, "
			+ "task_repetition, is_task_master, task_due_duration, is_task_overdue "
			+ " FROM Tasks "
			+ " WHERE is_task_deleted = false ";
	
	private TaskController() {
	}
	
	/**
	 * 
	 * Method to return all Non-Master Tasks
	 * 
	 * @return ArrayList of all Non-Master Tasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<Task> getAllTasks() throws SQLException {
		
		String sql = selectSQL +
				" AND is_task_master = false ";
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<Task> tasksList = new ArrayList<Task>();
			
			while (rs.next()) {
				tasksList.add(processResultSetIntoTaskRow(rs));
			}
			
			return tasksList;
		}
	}

	/**
	 * 
	 * Method to return all Master Tasks
	 * 
	 * @return ArrayList of all Master Tasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<Task> getAllMasterTasks() throws SQLException {
		
		String sql = selectSQL +
				" AND is_task_master = true ";
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<Task> tasksList = new ArrayList<Task>();
			
			while (rs.next()) {
				tasksList.add(processResultSetIntoTaskRow(rs));
			}
			
			return tasksList;
		}
	}
	
	/**
	 * 
	 * Method to return the Non Master Task for the given taskId
	 * 
	 * @param taskId
	 * @return Task instance
	 * @throws SQLException
	 * 
	 */
	public static Task getTaskById(int taskId) throws SQLException {
		
		String sql = selectSQL +
				" AND task_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, taskId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoTaskRow(rs);
			} else {
				return null;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to insert new Task row
	 * 
	 * @param taskRow
	 * @return boolean of whether the row is inserted successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean insertTask(Task taskRow) throws SQLException {
		
		String sql = "INSERT INTO Tasks (task_title, task_description, task_original_point_value, " + 
				"task_updated_point_value, task_created_by, task_created_date, task_due_date, is_task_completed, " +
				"is_task_deleted, task_repetition, is_task_master, task_due_duration, is_task_overdue) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			){
			
			stmt.setString(1, taskRow.getTaskTitle());
			stmt.setString(2, taskRow.getTaskDescription());
			stmt.setDouble(3, taskRow.getTaskOriginalPointValue());
			stmt.setDouble(4, taskRow.getTaskUpdatedPointValue());
			stmt.setInt(5, taskRow.getTaskCreatedBy());
			stmt.setTimestamp(6, new  Timestamp(taskRow.getTaskCreatedDate().getTime()));
			stmt.setTimestamp(7, new Timestamp(taskRow.getTaskDueDate().getTime()));
			stmt.setBoolean(8, taskRow.isTaskCompleted());
			stmt.setBoolean(9, taskRow.isTaskDeleted());
			stmt.setString(10, taskRow.getTaskRepetition());
			stmt.setBoolean(11, taskRow.isTaskMaster());
			stmt.setInt(12, taskRow.getTaskDueDuration());
			stmt.setBoolean(13, taskRow.isTaskOverdue());
			
			int affected_rows = stmt.executeUpdate();
			
			if (affected_rows == 1) {
				rs = stmt.getGeneratedKeys();
				rs.next();
				
				int taskId = rs.getInt(1);
				taskRow.setTaskId(taskId);
				
				return true;
			} else {
				return false;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to update existing Task row
	 * 
	 * @param taskRow
	 * @return boolean of whether the row is updated successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean updateTask(Task taskRow) throws SQLException {
		
		String sql = "UPDATE Tasks SET " +
				"task_title = ?, task_description = ?, task_original_point_value = ?, " +
				"task_updated_point_value = ?, task_created_by = ?, task_created_date = ?, " +
				"task_due_date = ?, is_task_completed = ?, is_task_deleted = ?, task_repetition = ?, " +
				"is_task_master = ?, task_due_duration = ?, is_task_overdue = ?" +
				" WHERE task_id = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setString(1, taskRow.getTaskTitle());
			stmt.setString(2, taskRow.getTaskDescription());
			stmt.setDouble(3, taskRow.getTaskOriginalPointValue());
			stmt.setDouble(4, taskRow.getTaskUpdatedPointValue());
			stmt.setInt(5, taskRow.getTaskCreatedBy());
			stmt.setTimestamp(6, new Timestamp(taskRow.getTaskCreatedDate().getTime()));
			stmt.setTimestamp(7, new Timestamp(taskRow.getTaskDueDate().getTime()));
			stmt.setBoolean(8, taskRow.isTaskCompleted());
			stmt.setBoolean(9, taskRow.isTaskDeleted());
			stmt.setString(10, taskRow.getTaskRepetition());
			stmt.setBoolean(11, taskRow.isTaskMaster());
			stmt.setInt(12, taskRow.getTaskDueDuration());
			stmt.setBoolean(13, taskRow.isTaskOverdue());
			stmt.setInt(14, taskRow.getTaskId());
			
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
	 * Method to save Task row
	 * 
	 * @param taskRow
	 * @return boolean of whether or not the row is saved successfully
	 * @throws SQLException
	 * 
	 */
	public static boolean save(Task taskRow) throws SQLException {
		return taskRow.getTaskId() > 0 ? updateTask(taskRow) : insertTask(taskRow);
	}
	
	/**
	 * 
	 * Method to Convert ResultSet into Task instance
	 * 
	 * @param ResultSet instance rs
	 * @return Task instance
	 * @throws SQLException
	 * 
	 */
	protected static Task processResultSetIntoTaskRow(ResultSet rs) throws SQLException {
		Task taskRow = new Task();
		
		taskRow.setTaskId(rs.getInt("task_id"));
		taskRow.setTaskTitle(rs.getString("task_title"));
		taskRow.setTaskDescription(rs.getString("task_description"));
		taskRow.setTaskOriginalPointValue(rs.getDouble("task_original_point_value"));
		taskRow.setTaskUpdatedPointValue(rs.getDouble("task_updated_point_value"));
		taskRow.setTaskCreatedBy(rs.getInt("task_created_by"));
		taskRow.setTaskCreatedDate(rs.getTimestamp("task_created_date"));
		taskRow.setTaskDueDate(rs.getTimestamp("task_due_date"));
		taskRow.setTaskCompleted(rs.getBoolean("is_task_completed"));
		taskRow.setTaskDeleted(rs.getBoolean("is_task_deleted"));
		taskRow.setTaskRepetition(rs.getString("task_repetition"));
		taskRow.setTaskMaster(rs.getBoolean("is_task_master"));
		taskRow.setTaskDueDuration(rs.getInt("task_due_duration"));
		taskRow.setTaskOverdue(rs.getBoolean("is_task_overdue"));
		
		return taskRow;
	}
}
