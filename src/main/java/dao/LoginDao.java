package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import model.Login;

public class LoginDao {
	/*
	 * This class handles all the database operations related to login functionality
	 */
	
	
	public Login login(String username, String password) {
		/*
		 * Return a Login object with role as "manager", "customerRepresentative" or "customer" if successful login
		 * Else, return null
		 * The role depends on the type of the user, which has to be handled in the database
		 * username, which is the email address of the user, is given as method parameter
		 * password, which is the password of the user, is given as method parameter
		 * Query to verify the username and password and fetch the role of the user, must be implemented
		 */
		
		/*data begins*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Email = ?");
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			Login login = null;
			if (!rs.next()){ 
				System.out.println("Username Does Not Exist!");
				return login;
			}
			else{ 
				if(!rs.getString("Password").equals(password)) {
					System.out.println("Incorrect Password");
					return login;
				}
				else {
					System.out.println("Successfully Logged In");
					login = new Login();
					switch(rs.getString("Role")) {
						case("Manager"):
							login.setRole("manager");
							return login;
						case("Employee"):
							login.setRole("customerRepresentative");
							return login;
						case("Customer"):
							login.setRole("customer");
							return login;
					}
				}
			}

		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		return null;
		
		/*data ends*/
		
	}
	
	public String addUser(Login login) {
		/*
		 * Query to insert a new record for user login must be implemented
		 * login, which is the "Login" Class object containing username and password for the new user, is given as method parameter
		 * The username and password from login can get accessed using getter methods in the "Login" model
		 * e.g. getUsername() method will return the username encapsulated in login object
		 * Return "success" on successful insertion of a new user
		 * Return "failure" for an unsuccessful database operation
		 */
		
		/*Sample data begins*/
		return "success";
		/*Sample data ends*/
	}

}