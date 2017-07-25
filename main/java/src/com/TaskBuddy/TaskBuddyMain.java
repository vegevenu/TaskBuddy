package com.TaskBuddy;

import com.TaskBuddy.db.ConnectionManager;

/**
 * @author Siddhardha
 *
 * Main class for the TaskBuddyMain web application
 *
 */
public class TaskBuddyMain {

	/**
	 * 
	 * Main method. Starting point of execution.
	 * 
	 */
	public static void main(String[] args) {

		//opening connection to MySQL database
		ConnectionManager.getInstance().openConnection();

		
		
		//closing connection to MySQL database
		ConnectionManager.getInstance().closeConnection();
	}
}
