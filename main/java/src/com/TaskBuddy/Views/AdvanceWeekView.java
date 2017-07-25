package com.TaskBuddy.Views;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.TaskBuddy.Controllers.AdvanceWeekController;

/**
 * @author Siddhardha
 *
 * View class to call advance_week stored procedure
 *
 */
@Path("/advance")
public class AdvanceWeekView {
	
	private static final Logger log = Logger.getLogger(AdvanceWeekView.class);
	
	public AdvanceWeekView() {
	}
	
	/**
	 * 
	 * This method is invoked on POST
	 * @return boolean of success or failure
	 * 
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean advanceWeek() {
		try {
			
			return AdvanceWeekController.advanceWeek();
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
}
