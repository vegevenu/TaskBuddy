package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DbConnectionTest {
	
	private static final Logger log = Logger.getLogger(DbConnectionTest.class);

	private static final String SERVER_NAME = "localhost";
	private static final String DB_NAME = "taskbuddy";
	private static final String USERNAME = "tb_db_user";
	private static final String PASSWORD = "tbdbuserpwd";
	
	private static final String CONN_STRING = "jdbc:mysql://" + SERVER_NAME + "/" + DB_NAME;
	
	public static void main(String[] args) throws SQLException {
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
		String sqlQuery = null;
		
		int maxId = 3;
		sqlQuery = "SELECT user_id, user_first_name FROM Users WHERE user_id <= " + maxId;
//		sqlQuery = "SELECT * FROM Users";
		
		try 
			(
					Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sqlQuery);
			)
		{
//			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			
			log.info("Connected to DB");
			
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
//			rs = stmt.executeQuery(sqlQuery);
			
			rs.last();
			log.info("Number of rows: " + rs.getRow());
			
			int id;
			String name = null;
			
			rs.beforeFirst();
			while(rs.next()){
//				id = rs.getObject("user_id", Integer.class);
//				name = rs.getObject("user_first_name", String.class);

				id = rs.getInt("user_id");
				name = rs.getString("user_first_name");

				log.info("ID: " + id + ", Name: " + name);
			}

		} catch (SQLException e) {
			log.error("Error message: " + e.getMessage());
		} 
//		finally {
//			if (rs != null){
//				rs.close();
//			}
//			if (stmt != null){
//				stmt.close();
//			}
//			if (conn != null){
//				conn.close();
//			}
//		}
	}
}
