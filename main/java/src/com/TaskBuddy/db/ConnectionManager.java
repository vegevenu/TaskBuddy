package com.TaskBuddy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.TaskBuddy.properties.PropertiesManager;

/**
 * 
 * @author Siddhardha
 * 
 * ConnectionManager class for creating persistent database connection object 'conn'.
 * Singleton design pattern is used to create only one instance of ConnectionManager class.
 * 
 */
public class ConnectionManager {
	
	private static ConnectionManager instance = null;
	
	private static final Logger log = Logger.getLogger(ConnectionManager.class);
	
	private static final String DB_CLIENT = PropertiesManager.getProperty("DB_CLIENT");
	private static final String SERVER_NAME = PropertiesManager.getProperty("SERVER_NAME");
	private static final String DB_NAME = PropertiesManager.getProperty("DB_NAME");
	private static final String USERNAME = PropertiesManager.getProperty("USERNAME");
	private static final String PASSWORD = PropertiesManager.getProperty("PASSWORD");
	
	private static final String CONN_STRING = "jdbc:" + DB_CLIENT + "://" + SERVER_NAME + "/" + DB_NAME;
	
	private Connection conn = null;
	
	//Constructor is made private to prevent instantiation from outside the class
	private ConnectionManager() {
	}
	
	/**
	 * 
	 * ConnectionManager object is created when this method is called for the first time. 
	 * It is assigned to the private static field 'instance'.
	 * From the next time when the function is called, the already created instance of 
	 * ConnectionManager class is returned.
	 * 
	 * @return ConnectionManager instance
	 * 
	 */
	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}
	
	/**
	 * 
	 * Opens connection to MySQL database.
	 * Error message is printed if open connection fails.
	 * 
	 */
	public void openConnection()
	{
		log.info("Opening MySQL connection...");
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
				log.info("MySQL Connection opened.");
			} catch (SQLException | ClassNotFoundException e) {
				log.error("Error message: " + e.getMessage());
				log.info("MySQL Connection open failed.");
			}
			return;
		}
		log.info("MySQL Connection is already opened.");
	}
	
	/**
	 * 
	 * Returns connection to MySQL database.
	 * If connection is null, then a new connection is opened and returned.
	 * Error message is printed if open connection fails.
	 * 
	 * @return Connection object
	 * 
	 */
	public Connection getConnection()
	{
		if (conn == null) {
			openConnection();
		}
		return conn;
	}
	
	/**
	 * 
	 * Closes connection to MySQL database.
	 * Error message is printed if close connection fails.
	 * 
	 */
	public void closeConnection()
	{
		log.info("Closing MySQL connection...");
		if (conn != null) {
			try {
				conn.close();
				conn = null;
				log.info("MySQL Connection closed.");
			} catch (SQLException e) {
				log.error("Error message: " + e.getMessage());
				log.info("MySQL Connection close failed.");
			}
			return;
		}
		log.info("MySQL Connection is already closed.");
	}
}
