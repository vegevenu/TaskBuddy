package com.TaskBuddy.Views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.TaskBuddy.Controllers.TaskController;
import com.TaskBuddy.Controllers.UserTaskController;
import com.TaskBuddy.Models.Task;
import com.TaskBuddy.Models.UserTask;
import com.TaskBuddy.ViewObjects.TaskViewObject;

/**
 * @author Siddhardha
 *
 * View class for Tasks table
 *
 */
@Path("/tasks")
public class TaskView {
	
	private static final Logger log = Logger.getLogger(TaskView.class);

	public TaskView() {
	}
	
	private static TaskViewObject createTaskViewObject(Task taskRow, UserTask userTaskRow) {
		TaskViewObject taskViewRow = new TaskViewObject();
		
		taskViewRow.setTaskId(taskRow.getTaskId());
		taskViewRow.setTaskTitle(taskRow.getTaskTitle());
		taskViewRow.setTaskDescription(taskRow.getTaskDescription());
		taskViewRow.setTaskOriginalPointValue(taskRow.getTaskOriginalPointValue());
		taskViewRow.setTaskUpdatedPointValue(taskRow.getTaskUpdatedPointValue());
		taskViewRow.setTaskCreatedBy(taskRow.getTaskCreatedBy());
		taskViewRow.setTaskCreatedDate(taskRow.getTaskCreatedDate());
		taskViewRow.setTaskDueDate(taskRow.getTaskDueDate());
		taskViewRow.setTaskCompleted(taskRow.isTaskCompleted());
		taskViewRow.setTaskDeleted(taskRow.isTaskDeleted());
		taskViewRow.setTaskRepetition(taskRow.getTaskRepetition());
		taskViewRow.setTaskMaster(taskRow.isTaskMaster());
		taskViewRow.setTaskDueDuration(taskRow.getTaskDueDuration());
		taskViewRow.setTaskOverdue(taskRow.isTaskOverdue());
		taskViewRow.setUserId(userTaskRow.getUserId());
		taskViewRow.setTaskAssignedDate(userTaskRow.getTaskAssignedDate());
		taskViewRow.setTaskAssigned(userTaskRow.isTaskAssigned());
		
		return taskViewRow;
	}
	
	private static Task getTaskFromTaskViewObject(TaskViewObject taskViewRow) {
		Task taskRow = new Task();
		
		taskRow.setTaskId(taskViewRow.getTaskId());
		taskRow.setTaskTitle(taskViewRow.getTaskTitle());
		taskRow.setTaskDescription(taskViewRow.getTaskDescription());
		taskRow.setTaskOriginalPointValue(taskViewRow.getTaskOriginalPointValue());
		taskRow.setTaskUpdatedPointValue(taskViewRow.getTaskUpdatedPointValue());
		taskRow.setTaskCreatedBy(taskViewRow.getTaskCreatedBy());
		taskRow.setTaskCreatedDate(taskViewRow.getTaskCreatedDate());
		taskRow.setTaskDueDate(taskViewRow.getTaskDueDate());
		taskRow.setTaskCompleted(taskViewRow.isTaskCompleted());
		taskRow.setTaskDeleted(taskViewRow.isTaskDeleted());
		taskRow.setTaskRepetition(taskViewRow.getTaskRepetition());
		taskRow.setTaskMaster(taskViewRow.isTaskMaster());
		taskRow.setTaskDueDuration(taskViewRow.getTaskDueDuration());
		taskRow.setTaskOverdue(taskViewRow.isTaskOverdue());
		
		return taskRow;
	}
	
	private static UserTask getUserTaskFromTaskViewObject(TaskViewObject taskViewRow) {
		UserTask userTaskRow = new UserTask();
		
		userTaskRow.setUserId(taskViewRow.getUserId());
		userTaskRow.setTaskId(taskViewRow.getTaskId());
		userTaskRow.setTaskAssignedDate(taskViewRow.getTaskAssignedDate());
		userTaskRow.setTaskAssigned(taskViewRow.isTaskAssigned());
		
		return userTaskRow;
	}
	
	/**
	 * 
	 * This method is invoked on GET
	 * @return ArrayList of all TaskViewObjects in JSON format
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public static ArrayList<TaskViewObject> getAllTaskViews() {
		try {
			
			ArrayList<TaskViewObject> taskViewList = new ArrayList<TaskViewObject>();
			
			ArrayList<Task> tasksList = TaskController.getAllTasks();
			
			for (Task taskRow : tasksList) {
				
				ArrayList<UserTask> userTasksList = UserTaskController.getAllUsersByTaskId(taskRow.getTaskId());
				
				for (UserTask  userTaskRow : userTasksList) {
					taskViewList.add(createTaskViewObject(taskRow, userTaskRow));
				}
				
				if(userTasksList.isEmpty())
				{
					UserTask userTaskRow = new UserTask();
					taskViewList.add(createTaskViewObject(taskRow, userTaskRow));
				}
			}
			
			return taskViewList;
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@GET @Path("user/{userId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public static ArrayList<TaskViewObject> getAllTaskViewsByUserId(@PathParam("userId") int userId) {
		try {
			
			ArrayList<UserTask> userTasksList = UserTaskController.getAllTasksByUserId(userId);
			
			ArrayList<TaskViewObject> taskViewList = new ArrayList<TaskViewObject>();
			
			for (UserTask userTaskRow : userTasksList) {
				
				Task taskRow = TaskController.getTaskById(userTaskRow.getTaskId());
				
				taskViewList.add(createTaskViewObject(taskRow, userTaskRow));
			}
			
			return taskViewList;
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@GET @Path("{taskId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public static TaskViewObject getTaskViewByTaskId(@PathParam("taskId") int taskId) {
		try {
			
			Task taskRow = TaskController.getTaskById(taskId);
			
			ArrayList<UserTask> userTasksList = UserTaskController.getAllUsersByTaskId(taskId);
			
			UserTask userTaskRow = new UserTask();
			
			for (UserTask userTaskRowInternal : userTasksList) {
				userTaskRow = userTaskRowInternal;
			}
			
			return createTaskViewObject(taskRow, userTaskRow);
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return null;
			
		}
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean insertTaskView(TaskViewObject taskViewRow) {
		try {
			Task taskRow = getTaskFromTaskViewObject(taskViewRow);
			boolean taskSaved = TaskController.save(taskRow);
			
			boolean userTaskSaved = true;
			
			if(!taskRow.isTaskMaster())
			{
				UserTask userTaskRow = getUserTaskFromTaskViewObject(taskViewRow);			
				userTaskRow.setTaskId(taskRow.getTaskId());
				userTaskSaved = UserTaskController.save(userTaskRow);
			}
			
			if(taskSaved && userTaskSaved) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
	
	@PUT @Path("{taskId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean updateTaskView(TaskViewObject taskViewRow) {
		try {
			Task taskRow = getTaskFromTaskViewObject(taskViewRow);
			UserTask userTaskRow = getUserTaskFromTaskViewObject(taskViewRow);
			
			Task taskRowFromDB = TaskController.getTaskById(taskRow.getTaskId());
			ArrayList<UserTask> userTasksList = UserTaskController.getAllUsersByTaskId(taskRow.getTaskId());
			
			for (UserTask userTaskRowObj : userTasksList) {
				if (userTaskRowObj.getUserId() != userTaskRow.getUserId()) {
					userTaskRowObj.setTaskAssigned(false);
					UserTaskController.save(userTaskRowObj);
				}
			}
			
			if (taskRow.isTaskDeleted() == true) {
				userTaskRow.setTaskAssigned(false);
			}
			
			if(taskRow.isTaskCompleted() && !taskRowFromDB.isTaskCompleted() && !taskRow.getTaskRepetition().equals("NoRepeat")) { //When a Repetition task is marked complete
				
				//Code block: Create new task with same values
				Task taskRowNew = getTaskFromTaskViewObject(taskViewRow);
				UserTask userTaskRowNew = getUserTaskFromTaskViewObject(taskViewRow);
				
				taskRowNew.setTaskId(-1);
				taskRowNew.setTaskCreatedDate(new Date(System.currentTimeMillis()));
				
				if (taskRowNew.getTaskRepetition().equals("Monthly")) { //Add 1 month to due date until due date > current date
					Calendar cal = Calendar.getInstance();
					cal.setTime(taskRowNew.getTaskDueDate());
					
					Date currentDate = new Date(System.currentTimeMillis());
					
					if(cal.getTime().before(currentDate)) {
						while (!cal.getTime().after(currentDate)) {
							cal.add(Calendar.MONTH, 1);
						}
					} else {
						cal.add(Calendar.MONTH, 1);
					}
					
					taskRowNew.setTaskDueDate(cal.getTime());
				}
				else if (taskRowNew.getTaskRepetition().equals("Weekly")) { //Add 7 days to due date until due date > current date
					Calendar cal = Calendar.getInstance();
					cal.setTime(taskRowNew.getTaskDueDate());
					
					Date currentDate = new Date(System.currentTimeMillis());
					
					if(cal.getTime().before(currentDate)) {
						while (!cal.getTime().after(currentDate)) {
							cal.add(Calendar.DATE, 7);
						}
					} else {
						cal.add(Calendar.DATE, 7);
					}
					
					taskRowNew.setTaskDueDate(cal.getTime());
				}
				else if (taskRowNew.getTaskRepetition().equals("Daily")) { //Add 1 day to due date until due date > current date
					Calendar cal = Calendar.getInstance();
					cal.setTime(taskRowNew.getTaskDueDate());
					
					Date currentDate = new Date(System.currentTimeMillis());
					
					if(cal.getTime().before(currentDate)) {
						while (!cal.getTime().after(currentDate)) {
							cal.add(Calendar.DATE, 1);
						}
					} else {
						cal.add(Calendar.DATE, 1);
					}
					
					taskRowNew.setTaskDueDate(cal.getTime());
				}
					
				taskRowNew.setTaskCompleted(false);
				
				boolean taskNewSaved = TaskController.save(taskRowNew);
				
				userTaskRowNew.setTaskId(taskRowNew.getTaskId());
				userTaskRowNew.setTaskAssignedDate(new Date(System.currentTimeMillis()));
				
				boolean userTaskNewSaved = UserTaskController.save(userTaskRowNew);
				
				//End of Code block: Create new task
				
				taskRow.setTaskRepetition("NoRepeat"); //Marking the completed task as Non repeatable
				
				if(taskNewSaved && userTaskNewSaved) {
					//do nothing
				} else {
					return false;
				}
				
			} else if(!taskRow.isTaskCompleted() && taskRowFromDB.isTaskCompleted() && taskRow.getTaskRepetition() != "NoRepeat") { //When a Repetition task is marked not complete
				
			}
			
			boolean taskSaved = TaskController.save(taskRow);
			boolean userTaskSaved = UserTaskController.save(userTaskRow);
			
			if(taskSaved && userTaskSaved) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			
			log.error("Error message: " + e.getMessage());
			
			return false;
		}
	}
}
