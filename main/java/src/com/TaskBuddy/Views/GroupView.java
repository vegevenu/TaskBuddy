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

import com.TaskBuddy.Controllers.GroupController;
import com.TaskBuddy.Models.Group;

/**
 * @author Siddhardha
 *
 * View class for Groups table
 *
 */
@Path("/groups")
public class GroupView {
	
	private static final Logger log = Logger.getLogger(GroupView.class);

	public GroupView() {
	}
	
	/**
	 * 
	 * This method is invoked on GET
	 * @return ArrayList of all Groups in JSON format
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public static ArrayList<Group> getAllGroups() {
		try {
			
			return GroupController.getAllGroups();
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	
	@GET @Path("{groupId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public static Group getGroupById(@PathParam("groupId") int groupId) {
		try {
			
			return GroupController.getGroupById(groupId);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean insertGroup(Group groupRow) {
		try {
			
			return GroupController.save(groupRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
	
	@PUT @Path("{groupId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean updateGroup(Group groupRow) {
		try {
			
			return GroupController.save(groupRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
}
