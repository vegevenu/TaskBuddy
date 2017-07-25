package com.TaskBuddy.Controllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;

import com.TaskBuddy.db.ConnectionManager;


/**
 * @author venu
 *
 * Controller class to call reset_week stored procedure
 *
 */

@XmlRootElement
public class ResetWeekController {
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private static String SQL = "{CALL reset_week()}";
	
	private ResetWeekController() {
	}
	
	public static boolean resetWeek() throws SQLException {
		
		String sql = SQL;
		
		try (
				CallableStatement cstmt = conn.prepareCall (sql);
			){
			
			cstmt.execute();
			
			return true;
		}
	}

}
