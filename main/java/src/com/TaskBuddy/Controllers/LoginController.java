package com.TaskBuddy.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.Models.Login;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author venu
 *
 * Controller class for Logins table
 *
 */
@XmlRootElement
public class LoginController {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String selectSQL = "SELECT " +
			"login_id, user_id, " +
			"username, user_password, user_role" +
			" FROM Logins "
			+ " WHERE 1=1 ";
	
	private LoginController() {
	}
	
	/**
	 * 
	 * Method to return all Logins
	 * 
	 * @return ArrayList of all Logins
	 * @throws SQLException
	 * 
	 */
	public static ArrayList<Login> getAllLogins() throws SQLException {
		
		String sql = selectSQL;
		
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			ArrayList<Login> loginsList = new ArrayList<Login>();
			
			while (rs.next()) {
				loginsList.add(processResultSetIntoLoginRow(rs));
			}
			
			return loginsList;
		}
	}
	
	/**
	 * 
	 * Method to return the Login for the given loginId
	 * 
	 * @param loginId
	 * @return Login instance
	 * @throws SQLException
	 * 
	 */
	public static Login getLoginById(int loginId) throws SQLException {
		
		String sql = selectSQL +
				" AND login_id = ? ";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, loginId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return processResultSetIntoLoginRow(rs);
			} else {
				return null;
			}
			
		} finally {
			if (rs != null) rs.close(); 
		}
	}
	
	/**
	 * 
	 * Method to insert new Login row
	 * 
	 * @param loginRow
	 * @return boolean of whether the row is inserted successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean insertLogin(Login loginRow) throws SQLException {
		
		String sql = "INSERT INTO Logins (user_id, " +
				"username, user_password, user_role) " +
				"VALUES (?, ?, ?, ?)";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			){
			
			stmt.setInt(1, loginRow.getUserId());
			stmt.setString(2, loginRow.getUserName());
			stmt.setString(3, loginRow.getUserPassWord());
			stmt.setString(4, loginRow.getUserRole());
			
			int affected_rows = stmt.executeUpdate();
			
			if (affected_rows == 1) {
				rs = stmt.getGeneratedKeys();
				rs.next();
				
				int loginId = rs.getInt(1);
				loginRow.setLoginId(loginId);
				
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
	 * Method to update existing Login row
	 * 
	 * @param loginRow
	 * @return boolean of whether the row is updated successfully
	 * @throws SQLException
	 * 
	 */
	private static boolean updateLogin(Login loginRow) throws SQLException {
		
		String sql = "UPDATE Logins SET " +
				"user_id = ?, " +
				"username = ?, user_password = ?, user_role = ?" +
				" WHERE login_id = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setInt(1, loginRow.getUserId());
			stmt.setString(2, loginRow.getUserName());
			stmt.setString(3, loginRow.getUserPassWord());
			stmt.setString(4, loginRow.getUserRole());
			stmt.setInt(5, loginRow.getLoginId());
			
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
	 * Method to save Login row
	 * 
	 * @param loginRow
	 * @return boolean of whether or not the row is saved successfully
	 * @throws SQLException
	 * 
	 */
	public static boolean save(Login loginRow) throws SQLException {
		return loginRow.getLoginId() > 0 ? updateLogin(loginRow) : insertLogin(loginRow);
	}
	
	/**
	 * 
	 * Method to Convert ResultSet into Login instance
	 * 
	 * @param ResultSet instance rs
	 * @return Login instance
	 * @throws SQLException
	 * 
	 */
	protected static Login processResultSetIntoLoginRow(ResultSet rs) throws SQLException {
		Login loginRow = new Login();
		
		loginRow.setLoginId(rs.getInt("login_id"));
		loginRow.setUserId(rs.getInt("user_id"));
		loginRow.setUserName(rs.getString("username"));
		loginRow.setUserPassWord(rs.getString("user_password"));
		loginRow.setUserRole(rs.getString("user_role"));
		
		return loginRow;
	}
}
