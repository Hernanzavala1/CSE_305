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
			statement.setString(6, employee.getZipCode());
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
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE SSN = ?");
			statement.setString(1, employee.getSSN());
			ResultSet rs = statement.executeQuery();
			rs.next();
			int id = rs.getInt("Id");
				
			statement = con.prepareStatement("UPDATE Person SET FirstName= ?,  LastName = ? ,  Address = ? , City = ? , State =? , ZipCode =? ,  Email=? WHERE Id = ?;");
			statement.setString(1, employee.getFirstName());
			statement.setString(2, employee.getLastName());
			statement.setString(3, employee.getAddress());
			statement.setString(4, employee.getCity());
			statement.setString(5, employee.getState());
			statement.setString(6, employee.getZipCode());
			//statement.setString(7, customer.getPassword());
			boolean isValid = isValidEmail(employee.getEmail(), id);
			if(isValid) {
				statement.setString(7, employee.getEmail());
			}
			else {
				return "failure";
			}
			statement.setInt(8, id);
			statement.executeUpdate();
				
			statement = con.prepareStatement("UPDATE Employee SET IsManager =?, StartDate =?, HourlyRate=?  WHERE Id = ?");
			if(employee.getIsManager()) {
				statement.setInt(1, 1);
			}
			else {
				statement.setInt(1, 0);
			}
			String start = employee.getStartDate();
			Date date = Date.valueOf(start);
			statement.setDate(2,date);
			statement.setDouble(3, employee.getHourlyRate());
			statement.setInt(4, id);
			statement.executeUpdate();
			return "success";
		}
		catch(Exception e) {
			System.out.println(e);
			return "failure";
		}
	}

	public Boolean isValidEmail(String email, int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");

			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Id <> ?; ");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				if(rs.getString("Email").equals(email) ) {
					System.out.println("Email already exists!");
					return false;
				}
			}
			return true;
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public String deleteEmployee(String SSN) {
		/*
		 * SSN, which is the Employee's SSN which has to be deleted, is given as method parameter
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE SSN = ?");
			statement.setString(1, SSN);
			ResultSet rs = statement.executeQuery();
			rs.next();
			int id = rs.getInt("Id");
			statement = con.prepareStatement("DELETE FROM Person WHERE Id = ?");
			statement.setInt(1, id);
			statement.executeUpdate();
			return "success";
		}
		catch(Exception e) {
			System.out.println(e);
			return "failure";
		}

	}

	
	public List<Employee> getEmployees() {

		/*
		 * The students code to fetch data from the database will be written here
		 * Query to return details about all the employees must be implemented
		 * Each record is required to be encapsulated as a "Employee" class object and added to the "employees" List
		 */

		List<Employee> employees = new ArrayList<Employee>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Role = 'Employee' OR Role = 'Manager';");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("Id");
				Employee employee = new Employee();
				employee.setEmail(rs.getString("Email"));
				employee.setFirstName(rs.getString("FirstName"));
				employee.setLastName(rs.getString("LastName"));
				employee.setAddress(rs.getString("Address"));
				employee.setCity(rs.getString("City"));
				employee.setStartDate(getStartDate(id));
				employee.setState(rs.getString("State"));
				employee.setZipCode(rs.getString("ZipCode"));
				employee.setSSN(getSSN(id));
				employee.setHourlyRate(getHourlyRate(id));
				if(getIsManager(id) == 1) {
					employee.setIsManager(true);
				}
				else {
					employee.setIsManager(false);
				}
				employees.add(employee);
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		return employees;
	}
	
	public String getSSN(int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE Id = ?;");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getString("SSN");
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public String getStartDate(int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE Id = ?;");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			Date start = rs.getDate("StartDate");
			SimpleDateFormat conv = new SimpleDateFormat("yyyy-MM-dd");
			return conv.format(start);
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public int getHourlyRate(int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE Id = ?;");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getInt("HourlyRate");
		}
		catch(Exception e) {
			System.out.println(e);
			return -1;
		}
	}
	
	public int getIsManager(int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE Id = ?;");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getInt("IsManager");
		}
		catch(Exception e) {
			System.out.println(e);
			return -1;
		}
	}

	public Employee getEmployee(String SSN) {

		/*
		 * The students code to fetch data from the database based on "SSN" will be written here
		 * SSN, which is the Employee's SSN who's details have to be fetched, is given as method parameter
		 * The record is required to be encapsulated as a "Employee" class object
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Employee WHERE SSN = ? ;");
			statement.setString(1, SSN);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()){
				System.out.println("Employee Does Not Exist!");
				return null;
			}
			else{
				int id = rs.getInt("Id");
				int hourlyRate = rs.getInt("HourlyRate");
				int isManager = rs.getInt("IsManager");
				Date start = rs.getDate("StartDate");
				SimpleDateFormat conv = new SimpleDateFormat("yyyy-MM-dd");
				String strStart = conv.format(start);
				statement = con.prepareStatement("SELECT * FROM Person WHERE Id = ? ;");
				statement.setInt(1, id);
				rs = statement.executeQuery();
				rs.next();
				
				Employee employee = new Employee();
				
				employee.setEmail(rs.getString("Email"));
				employee.setFirstName(rs.getString("FirstName"));
				employee.setLastName(rs.getString("LastName"));
				employee.setAddress(rs.getString("Address"));
				employee.setCity(rs.getString("City"));
				employee.setStartDate(strStart);
				employee.setState(rs.getString("State"));
				employee.setZipCode(rs.getString("ZipCode"));
				employee.setSSN(SSN);
				employee.setHourlyRate(hourlyRate);
				if(isManager == 1) {
					employee.setIsManager(true);
				}
				else {
					employee.setIsManager(false);
				}
				
				return employee;
			}
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			return null;
		}
	}
	
	public Employee getHighestRevenueEmployee() {
		
		/*
		 * The students code to fetch employee data who generated the highest revenue will be written here
		 * The record is required to be encapsulated as a "Employee" class object
		 */
		
		Employee employee = new Employee();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("CREATE VIEW CRRevenue(SSN, TotalRevenue) AS SELECT RepSSN, SUM(TotalFare * 0.1) FROM Reservation WHERE RepSSN IS NOT Null GROUP BY RepSSN;");
			statement.executeUpdate();
			statement = con.prepareStatement("SELECT SSN FROM CRRevenue WHERE TotalRevenue >= (SELECT MAX(TotalRevenue) FROM CRRevenue)");
			statement.executeQuery();
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				System.out.println("No employee generated revenue");
				statement = con.prepareStatement("DROP VIEW CRRevenue;");
				statement.executeUpdate();
				return employee;
				
			}
			else{
				employee.setSSN(rs.getString("SSN"));
				statement = con.prepareStatement("DROP VIEW CRRevenue;");
				statement.executeUpdate();
				return employee;
			}
		}
		catch(Exception e){
			System.out.print(e);
			return null;
		}
	}

	public String getEmployeeID(String username) {
		/*
		 * The students code to fetch data from the database based on "username"(email) will be written here
		 * username, which is the Employee's email address who's Employee ID has to be fetched, is given as method parameter
		 * The Employee ID(SSN) is required to be returned as a String
		 */
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Role = 'Employee' OR Role = 'Manager' AND Email = ?;");
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()){
				System.out.println("Employee Does Not Exist!");
				return null;
			}
			else{
				int id = rs.getInt("Id");
				statement = con.prepareStatement("SELECT * FROM Employee WHERE Id = ?;");
				statement.setInt(1,id);
				rs = statement.executeQuery();
				rs.next();
				return rs.getString("SSN");
			}
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			return null;
		}
	}

}
