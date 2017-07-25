package com.TaskBuddy.Controllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.db.ConnectionManager;

/**
 * 
 *
 * Controller class to call advance_week stored procedure
 *
 */
@XmlRootElement
public class AdvanceWeekController {
	
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String SQL = "{CALL advance_week()}";
	
	private AdvanceWeekController() {
	}
	
	public static boolean advanceWeek() throws SQLException {
		
		String sql = SQL;
		
		try (
				CallableStatement cstmt = conn.prepareCall (sql);
			){
			
			cstmt.execute();
			
			return true;
		}
	}
}
