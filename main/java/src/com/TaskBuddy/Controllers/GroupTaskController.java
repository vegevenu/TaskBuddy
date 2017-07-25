package com.TaskBuddy.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.Models.GroupTask;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author venu
 *
 * Controller class for GroupTasks table
 *
 */
@XmlRootElement
public class GroupTaskController {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String selectSQL = "SELECT " +
			"group_id, task_id" +
			" FROM GroupTasks "
			+ " WHERE 1=1 ";
	
	private GroupTaskController() {
	}
	
	/**
	 * 
	 * Method to return all GroupTasks
	 * 
	 * @return ArrayList of all GroupTasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<GroupTask> getAllGroupTasks() throws SQLException {
		
		String sql = selectSQL;
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<GroupTask> groupTasksList = new ArrayList<GroupTask>();
			
			while (rs.next()) {
				groupTasksList.add(processResultSetIntoGroupTaskRow(rs));
			}
			
			return groupTasksList;
		}
	}
	
	/**
	 * 
	 * Method to return all the GroupTasks with Tasks (taskId) belonging to the given Group (groupId)
	 * 
	 * @param groupId
	 * @return ArrayList of GroupTasks
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<GroupTask> getAllTasksByGroupId(int groupId) throws SQLException {
		
		String sql = selectSQL +
				" AND group_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, groupId);
			rs = stmt.executeQuery();
			
			ArrayList<GroupTask> groupTasksList = new ArrayList<GroupTask>();
			
			while (rs.next()) {
				groupTasksList.add(processResultSetIntoGroupTaskRow(rs));
			}
			
			return groupTasksList;
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to return the GroupTask for the given taskId
	 * 
	 * @param taskId
	 * @return GroupTask instance
	 * @throws SQLException
	 * 
	 */
	public static GroupTask getGroupByTaskId(int taskId) throws SQLException {
		
		String sql = selectSQL +
				" AND task_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, taskId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoGroupTaskRow(rs);
			} else {
				return null;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to insert new GroupTask row
	 * 
	 * @param groupTaskRow
	 * @return boolean of whether the row is inserted successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean insertGroupTask(GroupTask groupTaskRow) throws SQLException {
		
		String sql = "INSERT INTO GroupTasks (group_id, task_id) " +
				"VALUES (?, ?)";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, groupTaskRow.getGroupId());
			stmt.setInt(2, groupTaskRow.getTaskId());
			
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
	 * Method to save GroupTask row
	 * 
	 * @param groupTaskRow
	 * @return boolean of whether or not the row is saved successfully
	 * @throws SQLException
	 * 
	 */
	public static boolean save(GroupTask groupTaskRow) throws SQLException {
		return insertGroupTask(groupTaskRow);
	}
	
	/**
	 * 
	 * Method to Convert ResultSet into GroupTask instance
	 * 
	 * @param ResultSet instance rs
	 * @return GroupTask instance
	 * @throws SQLException
	 * 
	 */
	protected static GroupTask processResultSetIntoGroupTaskRow(ResultSet rs) throws SQLException {
		GroupTask groupTaskRow = new GroupTask();
		
		groupTaskRow.setGroupId(rs.getInt("group_id"));
		groupTaskRow.setTaskId(rs.getInt("task_id"));
		
		return groupTaskRow;
	}
}
