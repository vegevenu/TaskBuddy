package com.TaskBuddy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TaskBuddy.Controllers.UserController;
import com.TaskBuddy.Models.User;
import com.TaskBuddy.db.ConnectionManager;

/**
 * @author Saketh
 *
 * Servlet class for handling Login process
 *
 */

@SuppressWarnings("serial")
public class SignIn extends HttpServlet{
	/**
	 * 
	 * Method to handle GET Requests
	 * 
	 * @param req, resp
	 * @return void
	 * @throws ServletException, IOException
	 */
	
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	/**
	 * 
	 * Method to handle POST request and generate response
	 * 
	 * @param request, response
	 * @return void
	 * @throws ServletException, IOException
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
			
		String receivedFbId = request.getParameter("fbId");
		String receivedFirstName = request.getParameter("firstName");
		String receivedLastName = request.getParameter("lastName");

		long fbId = Long.parseLong(receivedFbId);
		int userIdFromDb = 0;
		try {
			userIdFromDb = insertIntoDB(fbId, receivedFirstName, receivedLastName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write(Integer.toString(userIdFromDb));
	}
	
	
	/**
	 * Method to insert a new record into database based on his/her facebook profile
	 * 
	 * @param fbid, firstName, lastName
	 * @return userId
	 * @throws SQLException
	 * 
	 */
	
	
	private static int insertIntoDB(long fbId, String firstName, String lastName) throws SQLException{
		String sql = "select user_id from users where fb_id = ?";
		ResultSet rs = null;
		
		int userIdFromDb = 0;
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
			){
			
			stmt.setLong(1, fbId);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				userIdFromDb = rs.getInt("user_id");
			}else{
				User newUser = new User();
				newUser.setCurrentScore(0);
				newUser.setUserImage(null);
				newUser.setTotalScore(0);
				newUser.setUserCreatedDate(new Date(System.currentTimeMillis()));
				newUser.setUserDeleted(false);
				newUser.setUserFirstName(firstName);
				newUser.setUserLastName(lastName);
				newUser.setFbId(fbId);
				
				userIdFromDb =  (UserController.save(newUser)) ? (newUser.getUserId()) : (-1);
				
			}
			return userIdFromDb;
			
			
		}finally{
			if(rs != null) {
				rs.close();
			}
		}
		
	}
}
