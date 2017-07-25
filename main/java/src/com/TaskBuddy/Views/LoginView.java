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

import com.TaskBuddy.Controllers.LoginController;
import com.TaskBuddy.Models.Login;

/**
 * @author Siddhardha
 *
 * View class for Logins table
 *
 */
@Path("/logins")
public class LoginView {
	
	private static final Logger log = Logger.getLogger(LoginView.class);

	public LoginView() {
	}
	
	/**
	 * 
	 * This method is invoked on GET
	 * @return ArrayList of all Logins in JSON format
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public static ArrayList<Login> getAllLogins() {
		try {
			
			return LoginController.getAllLogins();
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@GET @Path("{loginId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public static Login getLoginById(@PathParam("loginId") int loginId) {
		try {
			
			return LoginController.getLoginById(loginId);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean insertLogin(Login loginRow) {
		try {
			
			return LoginController.save(loginRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
	
	@PUT @Path("{loginId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean updateLogin(Login loginRow) {
		try {
			
			return LoginController.save(loginRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
}
