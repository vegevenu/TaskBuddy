package com.TaskBuddy.Views.Testing;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class TaskViewTest {
	@Test
	public void getUserByIdShouldReturnCorrectUser(){
		expect().
				body("taskId", equalTo("1")).
				body("taskTitle", equalTo("Spray and wipe down counters & table")).
				body("taskDescription", equalTo("")).
				body("taskPointValue", equalTo("10")).
				body("taskCreatedBy", equalTo("Siddhardha m")).
				body("taskCreatedDate", equalTo("07-Oct-2014 09:00")).
				body("taskDueDate", equalTo("15-Oct-2014 11:35")).
				body("isTaskCompleted", equalTo("false")).
				body("isTaskDeleted", equalTo("false")).
		when().
				get("/tasks/1");
	}
	
}
