package com.TaskBuddy.db;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Siddhardha
 * 
 * ContextListener class is used to set the Initialized and Destroyed ServletContextListener.
 * These methods are called when Servlets are opened and closed respectively.
 * 
 */
public class ContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ConnectionManager.getInstance().openConnection();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ConnectionManager.getInstance().closeConnection();
	}
}
