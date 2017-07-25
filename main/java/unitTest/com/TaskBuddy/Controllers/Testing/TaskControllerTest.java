package com.TaskBuddy.Controllers.Testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.TaskBuddy.Controllers.TaskController;
import com.TaskBuddy.db.ConnectionManager;



public class TaskControllerTest {

	@Test
	public void getAllTasksShouldReturnArrayList() throws SQLException{
		assertNotNull("getAllTasks() is returning null", TaskController.getAllTasks());
		
		assertNotNull("Database Connection is Lost", ConnectionManager.getInstance().getConnection());
		
		assertEquals("", 2, 2);
		
	}

	
	
}
