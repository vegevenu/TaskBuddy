package com.TaskBuddy.Views.Testing;

import static com.jayway.restassured.RestAssured.expect;

import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;



public class UserViewTest {
	@Test
	public void getUserByIdShouldReturnCorrectUser(){
		expect().
				body("userId", equalTo("1")).
				body("userFirstName", equalTo("shivtej")).
				body("userLastName", equalTo("saripudi")).
				body("userImage", equalTo("")).
				body("userCreatedDate", equalTo("2014-10-07 01:02:49.0")).
				body("isUserDeleted", equalTo("false")).
				body("totalScore", equalTo("6")).
				body("currentScore", equalTo("0")).
		when().
				get("/users/1");
	}
}
