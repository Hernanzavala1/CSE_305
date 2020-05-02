package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;  

import model.Customer;
import model.Employee;

public class EmployeeDao {
	/*
	 * This class handles all the database operations related to the employee table
	 */
	
	public String addEmployee(Employee employee) {

		/*
		 * All the values of the add employee form are encapsulated in the employee object.
		 * These can be accessed by getter methods (see Employee class in model package).
		 * e.g. firstName can be accessed by employee.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database insertion of the employee details and return "success" or "failure" based on result of the database insertion.
		 */
		
		/*data begins*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("INSERT INTO Person(FirstName, LastName, Address, City, State, ZipCode, Password, Email, Role) VALUES(?,?,?,?,?,?,?,?,?);");
			statement.setString(1, employee.getFirstName());
			statement.setString(2, employee.getLastName());
			statement.setString(3, employee.getAddress());
			statement.setString(4, employee.getCity());
			statement.setString(5, employee.getState());
			statement.setInt(6, employee.getZipCode());
			statement.setString(7, employee.getPassword());
			statement.setString(8, employee.getEmail());
			if(employee.getIsManager()) {
				statement.setString(9, "Manager");
			}
			else {
				statement.setString(9, "Employee");
			}
			statement.executeUpdate();
			
			statement = con.prepareStatement("SELECT * FROM Person WHERE Email = ?");
			statement.setString(1, employee.getEmail());
			ResultSet rs = statement.executeQuery();
			rs.next();
			int id = rs.getInt("Id");
			
			statement = con.prepareStatement("INSERT INTO Employee(Id, SSN, IsManager, StartDate, HourlyRate) VALUES(?,?,?,?,?);");
			statement.setInt(1, id);
			statement.setString(2, employee.getSSN());
			if(employee.getIsManager()) {
				statement.setInt(3, 1);
			}
			else {
				statement.setInt(3, 0);
			}
			Date conv = Date.valueOf(employee.getStartDate());  
			statement.setDate(4, conv);
			statement.setDouble(5, employee.getHourlyRate());
			statement.executeUpdate();
			
			return "success";
		}
		catch(Exception e) {
			System.out.println(e);
			return "failure";
		}
		/*data ends*/

	}

	public String editEmployee(Employee employee) {
		/*
		 * All the values of the edit employee form are encapsulated in the employee object.
		 * These can be accessed by getter methods (see Employee class in model package).
		 * e.g. firstName can be accessed by employee.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database update and return "success" or "failure" based on result of the database update.
		 */
		
		/*Sample data begins*/
		return "success";
		/*Sample data ends*/

	}

	public String deleteEmployee(String SSN) {
		/*
		 * SSN, which is the Employee's SSN which has to be deleted, is given as method parameter
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */
		
		/*Sample data begins*/
		return "success";
		/*Sample data ends*/

	}

	
	public List<Employee> getEmployees() {

		/*
		 * The students code to fetch data from the database will be written here
		 * Query to return details about all the employees must be implemented
		 * Each record is required to be encapsulated as a "Employee" class object and added to the "employees" List
		 */

		List<Employee> employees = new ArrayList<Employee>();
		
		/*Sample data begins*/
		for (int i = 0; i < 10; i++) {
			Employee employee = new Employee();
			employee.setEmail("shiyong@cs.sunysb.edu");
			employee.setFirstName("Shiyong");
			employee.setLastName("Lu");
			employee.setAddress("123 Success Street");
			employee.setCity("Stony Brook");
			employee.setStartDate("2006-10-17");
			employee.setState("NY");
			employee.setZipCode(11790);
			employee.setSSN("6314135555");
			employee.setHourlyRate(100);
			employee.setIsManager(true);
			
			employees.add(employee);
		}
		/*Sample data ends*/
		
		return employees;
	}

	public Employee getEmployee(String SSN) {

		/*
		 * The students code to fetch data from the database based on "SSN" will be written here
		 * SSN, which is the Employee's SSN who's details have to be fetched, is given as method parameter
		 * The record is required to be encapsulated as a "Employee" class object
		 */

		Employee employee = new Employee();
		
		/*Sample data begins*/
		employee.setEmail("shiyong@cs.sunysb.edu");
		employee.setFirstName("Shiyong");
		employee.setLastName("Lu");
		employee.setAddress("123 Success Street");
		employee.setCity("Stony Brook");
		employee.setStartDate("2006-10-17");
		employee.setState("NY");
		employee.setZipCode(11790);
		employee.setSSN("6314135555");
		employee.setHourlyRate(100);
		employee.setIsManager(true);
		/*Sample data ends*/
		
		return employee;
	}
	
	public Employee getHighestRevenueEmployee() {
		
		/*
		 * The students code to fetch employee data who generated the highest revenue will be written here
		 * The record is required to be encapsulated as a "Employee" class object
		 */
		
		Employee employee = new Employee();
		
		/*Sample data begins*/
		// EmployeeID = SSN
		employee.setSSN("6314135555");
		/*Sample data ends*/
		
		return employee;
	}

	public String getEmployeeID(String username) {
		/*
		 * The students code to fetch data from the database based on "username"(email) will be written here
		 * username, which is the Employee's email address who's Employee ID has to be fetched, is given as method parameter
		 * The Employee ID(SSN) is required to be returned as a String
		 */

		return "111111111";
	}

}
