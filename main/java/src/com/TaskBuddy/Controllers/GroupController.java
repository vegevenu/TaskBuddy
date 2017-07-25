package com.TaskBuddy.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.Models.Group;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author Siddhardha
 *
 * Controller class for Groups table
 *
 */
@XmlRootElement
public class GroupController {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String selectSQL = "SELECT " +
			"group_id, group_name, group_admin_user_id, group_image, " +
			"group_created_date, is_group_deleted" +
			" FROM Groups "
			+ " WHERE is_group_deleted = false ";
	
	private GroupController() {
	}
	
	/**
	 * 
	 * Method to return all Groups
	 * 
	 * @return ArrayList of all Groups
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<Group> getAllGroups() throws SQLException {
		
		String sql = selectSQL;
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<Group> groupsList = new ArrayList<Group>();
			
			while (rs.next()) {
				groupsList.add(processResultSetIntoGroupRow(rs));
			}
			
			return groupsList;
		}
	}
	
	/**
	 * 
	 * Method to return the Group for the given groupId
	 * 
	 * @param groupId
	 * @return Group instance
	 * @throws SQLException
	 * 
	 */
	public static Group getGroupById(int groupId) throws SQLException {
		
		String sql = selectSQL +
				" AND group_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, groupId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoGroupRow(rs);
			} else {
				return null;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to insert new Group row
	 * 
	 * @param groupRow
	 * @return boolean of whether the row is inserted successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean insertGroup(Group groupRow) throws SQLException {
		
		String sql = "INSERT INTO Groups (group_name, group_admin_user_id, group_image, " +
				"group_created_date, is_group_deleted) " +
				"VALUES (?, ?, ?, ?, ?)";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			){
			
			stmt.setString(1, groupRow.getGroupName());
			stmt.setInt(2, groupRow.getGroupAdminUserId());
			stmt.setString(3, groupRow.getGroupImage());
			stmt.setTimestamp(4, new Timestamp(groupRow.getGroupCreatedDate().getTime()));
			stmt.setBoolean(5, groupRow.isGroupDeleted());
			
			int affected_rows = stmt.executeUpdate();
			
			if (affected_rows == 1) {
				rs = stmt.getGeneratedKeys();
				rs.next();
				
				int groupId = rs.getInt(1);
				groupRow.setGroupId(groupId);
				
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
	 * Method to update existing Group row
	 * 
	 * @param groupRow
	 * @return boolean of whether the row is updated successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean updateGroup(Group groupRow) throws SQLException {
		
		String sql = "UPDATE Groups SET " +
				"group_name = ?, group_admin_user_id = ?, group_image = ?, " +
				"group_created_date = ?, is_group_deleted = ?" +
				" WHERE group_id = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setString(1, groupRow.getGroupName());
			stmt.setInt(2, groupRow.getGroupAdminUserId());
			stmt.setString(3, groupRow.getGroupImage());
			stmt.setTimestamp(4, new Timestamp(groupRow.getGroupCreatedDate().getTime()));
			stmt.setBoolean(5, groupRow.isGroupDeleted());
			stmt.setInt(6, groupRow.getGroupId());
			
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
	 * Method to save Group row
	 * 
	 * @param groupRow
	 * @return boolean of whether or not the row is saved successfully
	 * @throws SQLException
	 * 
	 */
	public static boolean save(Group groupRow) throws SQLException {
		return groupRow.getGroupId() > 0 ? updateGroup(groupRow) : insertGroup(groupRow);
	}
	
	/**
	 * 
	 * Method to Convert ResultSet into Group instance
	 * 
	 * @param ResultSet instance rs
	 * @return Group instance
	 * @throws SQLException
	 * 
	 */
	protected static Group processResultSetIntoGroupRow(ResultSet rs) throws SQLException {
		Group groupRow = new Group();
		
		groupRow.setGroupId(rs.getInt("group_id"));
		groupRow.setGroupName(rs.getString("group_name"));
		groupRow.setGroupAdminUserId(rs.getInt("group_admin_user_id"));
		groupRow.setGroupImage(rs.getString("group_image"));
		groupRow.setGroupCreatedDate(rs.getTimestamp("group_created_date"));
		groupRow.setGroupDeleted(rs.getBoolean("is_group_deleted"));
		
		return groupRow;
	}
}
