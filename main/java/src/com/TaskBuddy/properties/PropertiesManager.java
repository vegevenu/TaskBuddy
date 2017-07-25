package com.TaskBuddy.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Siddhardha
 * 
 * PropertiesManager class to retrieve the properties stored in config.properties file
 * 
 */
public class PropertiesManager {
	
	private static final String CONFIG_FILE_NAME = "config.properties";
	
	private static final Logger log = Logger.getLogger(PropertiesManager.class);
	
	private static Properties properties = null;
	private static InputStream propertiesFile = null;
	
	private PropertiesManager() {
	}
	
	/**
	 * 
	 * Method to get a property VALUE by passing the KEY as input
	 * 
	 * @param key
	 * @return value
	 * 
	 */
	public static String getProperty(String key) 
	{
		properties = new Properties();
		String value = null;
		
		try {
			propertiesFile = PropertiesManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
			
			properties.load(propertiesFile);
			
			value = properties.getProperty(key);
			
		} catch (IOException e) {
			log.error("Error message: " + e.getMessage());
		} finally {
			if (propertiesFile != null) {
				try {
					propertiesFile.close();
				} catch (IOException e) {
					log.error("Error message: " + e.getMessage());
				}
			}
		}
		return value;
	}
}
