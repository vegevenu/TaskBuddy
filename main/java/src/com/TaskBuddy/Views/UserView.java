package com.TaskBuddy.Views;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.TaskBuddy.Controllers.UserController;
import com.TaskBuddy.Models.User;

/**
 * @author Siddhardha
 *
 * View class for Users table
 *
 */
@Path("/users")
public class UserView {
	
	private static final Logger log = Logger.getLogger(UserView.class);

	public UserView() {
	}
	
	/**
	 * 
	 * This method is invoked on GET
	 * @return ArrayList of all Users in JSON format
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public static ArrayList<User> getAllUsers() {
		try {
			
			return UserController.getAllUsers();
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@GET @Path("{userId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public static User getUserById(@PathParam("userId") int userId) {
		try {
			
			return UserController.getUserById(userId);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean insertUser(User userRow) {
		try {
			
			return UserController.save(userRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
	
	@PUT @Path("{userId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean updateUser(User userRow) {
		try {
			
			return UserController.save(userRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
}
