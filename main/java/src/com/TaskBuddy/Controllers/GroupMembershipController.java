package com.TaskBuddy.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.Models.GroupMembership;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author venu
 *
 * Controller class for GroupMemberships table
 *
 */
@XmlRootElement
public class GroupMembershipController {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String selectSQL = "SELECT " +
			"group_id, user_id, " +
			"user_joined_date, has_user_unjoined" +
			" FROM GroupMemberships "
			+ " WHERE has_user_unjoined = false ";
	
	private GroupMembershipController() {
	}
	
	/**
	 * 
	 * Method to return all GroupMemberships
	 * 
	 * @return ArrayList of all GroupMemberships
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<GroupMembership> getAllGroupMemberships() throws SQLException {
		
		String sql = selectSQL;
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<GroupMembership> groupMembershipsList = new ArrayList<GroupMembership>();
			
			while (rs.next()) {
				groupMembershipsList.add(processResultSetIntoGroupMembershipRow(rs));
			}
			
			return groupMembershipsList;
		}
	}
	
	/**
	 * 
	 * Method to return all GroupMemberships with Users (userId) who are members of the given Group (groupId)
	 * 
	 * @param groupId
	 * @return ArrayList of GroupMemberships
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<GroupMembership> getAllUsersByGroupId(int groupId) throws SQLException {
		
		String sql = selectSQL +
				" AND group_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, groupId);
			rs = stmt.executeQuery();
			
			ArrayList<GroupMembership> groupMembershipsList = new ArrayList<GroupMembership>();
			
			while (rs.next()) {
				groupMembershipsList.add(processResultSetIntoGroupMembershipRow(rs));
			} 
			
			return groupMembershipsList;
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to return all GroupMemberships with Groups (groupId) joined by the given User (userId)
	 * 
	 * @param userId
	 * @return ArrayList of GroupMemberships
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<GroupMembership> getAllGroupsByUserId(int userId) throws SQLException {
		
		String sql = selectSQL +
				" AND user_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			
			ArrayList<GroupMembership> groupMembershipsList = new ArrayList<GroupMembership>();
			
			while (rs.next()) {
				groupMembershipsList.add(processResultSetIntoGroupMembershipRow(rs));
			} 
			
			return groupMembershipsList;
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to return GroupMembership for the given userId and groupId
	 * 
	 * @param userId, groupId
	 * @return GroupMembership instance
	 * @throws SQLException
	 * 
	 */
	public static GroupMembership getGroupMembershipByUserIdAndGroupId(int userId, int groupId) throws SQLException {
		
		String sql = selectSQL +
				" AND user_id = ? AND group_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userId);
			stmt.setInt(2, groupId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoGroupMembershipRow(rs);
			} else {
				return null;
			}
			
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to insert new GroupMembership row
	 * 
	 * @param groupMembershipRow
	 * @return boolean of whether the row is inserted successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean insertGroupMembership(GroupMembership groupMembershipRow) throws SQLException {
		
		String sql = "INSERT INTO GroupMemberships (group_id, user_id, " +
				"user_joined_date, has_user_unjoined) " +
				"VALUES (?, ?, ?, ?)";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, groupMembershipRow.getGroupId());
			stmt.setInt(2, groupMembershipRow.getUserId());
			stmt.setTimestamp(3, new Timestamp(groupMembershipRow.getUserJoinedDate().getTime()));
			stmt.setBoolean(4, groupMembershipRow.hasUserUnjoined());
			
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
	 * Method to update existing GroupMembership row
	 * 
	 * @param groupMembershipRow
	 * @return boolean of whether the row is updated successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean updateGroupMembership(GroupMembership groupMembershipRow) throws SQLException {
		
		String sql = "UPDATE GroupMemberships SET " +
				"user_joined_date = ?, has_user_unjoined = ?" +
				" WHERE group_id = ? AND user_id = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setTimestamp(1, new Timestamp(groupMembershipRow.getUserJoinedDate().getTime()));
			stmt.setBoolean(2, groupMembershipRow.hasUserUnjoined());
			stmt.setInt(3, groupMembershipRow.getGroupId());
			stmt.setInt(4, groupMembershipRow.getUserId());
			
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
	 * Method to save GroupMembership row
	 * 
	 * @param groupMembershipRow
	 * @return boolean of whether or not the row is saved successfully
	 * @throws SQLException
	 * 
	 */
	public static boolean save(GroupMembership groupMembershipRow) throws SQLException {
		return checkGroupMembershipExists(groupMembershipRow.getUserId(), groupMembershipRow.getGroupId()) != null ? updateGroupMembership(groupMembershipRow) : insertGroupMembership(groupMembershipRow); 
	}
	
	/**
	 * 
	 * Method to check if groupMembershipRow exists
	 * 
	 * Code is duplicated from getGroupMembershipByUserIdAndGroupId(int, int) 
	 * to include records having has_user_unjoined both true and false 
	 * 
	 * @param userId, groupId
	 * @return GroupMembership instance
	 * @throws SQLException
	 * 
	 */
	public static GroupMembership checkGroupMembershipExists(int userId, int groupId) throws SQLException {
		
		String sql = "SELECT " +
				"group_id, user_id, " +
				"user_joined_date, has_user_unjoined" +
				" FROM GroupMemberships "
				+ " WHERE user_id = ? AND group_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, userId);
			stmt.setInt(2, groupId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoGroupMembershipRow(rs);
			} else {
				return null;
			}
			
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to Convert ResultSet into GroupMembership instance
	 * 
	 * @param ResultSet instance rs
	 * @return GroupMembership instance
	 * @throws SQLException
	 * 
	 */
	protected static GroupMembership processResultSetIntoGroupMembershipRow(ResultSet rs) throws SQLException {
		GroupMembership groupMembershipRow = new GroupMembership();
		
		groupMembershipRow.setGroupId(rs.getInt("group_id"));
		groupMembershipRow.setUserId(rs.getInt("user_id"));
		groupMembershipRow.setUserJoinedDate(rs.getTimestamp("user_joined_date"));
		groupMembershipRow.setHasUserUnjoined(rs.getBoolean("has_user_unjoined"));
		
		return groupMembershipRow;
	}
}
