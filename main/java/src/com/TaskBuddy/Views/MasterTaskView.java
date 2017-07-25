package com.TaskBuddy.Views;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.TaskBuddy.Controllers.TaskController;
import com.TaskBuddy.Models.Task;
import com.TaskBuddy.Models.UserTask;
import com.TaskBuddy.ViewObjects.TaskViewObject;
import com.TaskBuddy.properties.PropertiesManager;

/**
 * @author Siddhardha
 *
 * View class for Tasks table
 *
 */
@Path("/tasks/master")
public class MasterTaskView {

	private static final Logger log = Logger.getLogger(TaskView.class);
	private static final double PERCENTAGE = Double.parseDouble(PropertiesManager.getProperty("PERCENTAGE"));

	public MasterTaskView() {
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

	/**
	 * 
	 * This method is invoked on GET
	 * @return ArrayList of all TaskViewObjects in JSON format
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public static ArrayList<TaskViewObject> getAllMasterTaskViews() {
		try {

			ArrayList<TaskViewObject> taskViewList = new ArrayList<TaskViewObject>();

			ArrayList<Task> tasksList = TaskController.getAllMasterTasks();

			for (Task taskRow : tasksList) {
				UserTask userTaskRow = new UserTask();
				taskViewList.add(createTaskViewObject(taskRow, userTaskRow));
			}

			return taskViewList;

		} catch (Exception e) {

			log.error("Error message: " + e.getMessage());

			return null;

		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public static boolean insertMasterTaskView(TaskViewObject taskViewRow) {
		try {
			Task taskRow = getTaskFromTaskViewObject(taskViewRow);

			boolean taskSaved = TaskController.save(taskRow);

			if(taskSaved) {
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
	public static boolean updateMasterTaskView(TaskViewObject taskViewRow) {
		try {
			Task taskRow = getTaskFromTaskViewObject(taskViewRow);
			boolean taskSaved  = false;

			List<Task> masterTasksList = TaskController.getAllMasterTasks();

			int countOfIncompleteMasterTasks = 0;
			for(Task masterTask : masterTasksList){
				if(!masterTask.isTaskCompleted()){
					countOfIncompleteMasterTasks++;
				}
			}

			if(countOfIncompleteMasterTasks > 1){
				double originalPoints = taskRow.getTaskOriginalPointValue();
				double percentValue = (originalPoints * PERCENTAGE) / 100;
				double updatedPoints = originalPoints - percentValue;
				taskRow.setTaskOriginalPointValue(updatedPoints);

				taskSaved = TaskController.save(taskRow);

				double total = 0;
				for(Task masterTask : masterTasksList){
					if(masterTask.getTaskId() != taskRow.getTaskId() && !masterTask.isTaskCompleted()){ //Ignoring the completed tasks and the currently selected tasks
						total += masterTask.getTaskOriginalPointValue();
					}
				}
				for(Task masterTask : masterTasksList){
					if(masterTask.getTaskId() != taskRow.getTaskId() && !masterTask.isTaskCompleted()){ //Ignoring the completed tasks and the currently selected tasks
						double originalPointsRemTasks = masterTask.getTaskOriginalPointValue();
						double percentValueRemTasks = (originalPointsRemTasks * percentValue) / total;
						double updatedPointsRemTasks = originalPointsRemTasks + percentValueRemTasks;
						masterTask.setTaskOriginalPointValue(updatedPointsRemTasks);
						TaskController.save(masterTask);
					}
				}
			} else{
				taskSaved = TaskController.save(taskRow);
			}

			if(taskSaved) {
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
