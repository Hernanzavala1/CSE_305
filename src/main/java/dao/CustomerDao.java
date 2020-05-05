package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Customer;


public class CustomerDao {
	/*
	 * This class handles all the database operations related to the customer table
	 */
	
	/**
	 * @param String searchKeyword
	 * @return ArrayList<Customer> object
	 */
	public List<Customer> getCustomers() {
		/*
		 * This method fetches one or more customers and returns it as an ArrayList
		 */
		
		List<Customer> customers = new ArrayList<Customer>();

		/*
		 * The students code to fetch data from the database will be written here
		 * Each record is required to be encapsulated as a "Customer" class object and added to the "customers" List
		 */
		
		/*Sample data begins*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Role = 'Customer';");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("Id");
				Customer c = new Customer();
				c.setAccountNo(rs.getInt("Id"));
				c.setAddress(rs.getString("Address"));
				c.setLastName(rs.getString("LastName"));
				c.setFirstName(rs.getString("FirstName"));
				c.setCity(rs.getString("City"));
				c.setState(rs.getString("State"));
				c.setEmail(rs.getString("Email"));
				c.setZipCode(rs.getString("ZipCode"));
				c.setCreditCard(getCreditCardNo(id));
				c.setRating(getRating(id));
				customers.add(c);
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
		/*Sample data ends*/
		
		return customers;
	}

	public String getCreditCardNo(int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Customer WHERE Id = ?;");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getString("CreditCardNo");
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public int getRating(int id) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Customer WHERE Id = ?;");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getInt("Rating");
		}
		catch(Exception e) {
			System.out.println(e);
			return -1;
		}
	}
	
	
	public Customer getHighestRevenueCustomer() {
		/*
		 * This method fetches the customer who generated the highest total revenue and returns it
		 * The students code to fetch data from the database will be written here
		 * The customer record is required to be encapsulated as a "Customer" class object
		 */


		/*Sample data begins*/
		
		Customer customer = new Customer();
		// Customer ID = Account Number
		customer.setAccountNo(111);
		customer.setLastName("Lu");
		customer.setFirstName("Shiyong");
		/*Sample data ends*/
	
		return customer;
		
	}

	public List<Customer> getCustomerMailingList() {

		/*
		 * This method fetches the all customer mailing details and returns it
		 * The students code to fetch data from the database will be written here
		 * Each customer record is required to be encapsulated as a "Customer" class object and added to the "customers" List
		 */

		
		List<Customer> customers = new ArrayList<Customer>();
		
		/*data begins*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Role = 'Customer';");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Customer c = new Customer();
				c.setAddress(rs.getString("Address"));
				c.setLastName(rs.getString("LastName"));
				c.setFirstName(rs.getString("FirstName"));
				c.setCity(rs.getString("City"));
				c.setState(rs.getString("State"));
				c.setEmail(rs.getString("Email"));
				c.setZipCode(rs.getString("ZipCode"));
				customers.add(c);
			}
			return customers;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public Customer getCustomer(int accountNo) {

		/*
		 * This method fetches the customer details and returns it
		 * accountNo, which is the Customer's accountNo who's details have to be fetched, is given as method parameter
		 * The students code to fetch data from the database will be written here
		 * The customer record is required to be encapsulated as a "Customer" class object
		 */
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Customer WHERE AccountNo = ? ;");
			statement.setInt(1, accountNo);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()){
				System.out.println("Customer Does Not Exist!");
				return null;
			}
			else{
				int id = rs.getInt("Id");
				String creditCardNo = rs.getString("CreditCardNo");
				int rating = rs.getInt("Rating");
				statement = con.prepareStatement("SELECT * FROM Person WHERE Id = ? ;");
				statement.setInt(1, id);
				rs = statement.executeQuery();
				rs.next();
				
				Customer customer = new Customer();
				customer.setAccountNo(accountNo);
				customer.setAddress(rs.getString("Address"));
				customer.setLastName(rs.getString("LastName"));
				customer.setFirstName(rs.getString("FirstName"));
				customer.setCity(rs.getString("City"));
				customer.setState(rs.getString("State"));
				customer.setEmail(rs.getString("Email"));
				customer.setZipCode(rs.getString("ZipCode"));
				customer.setCreditCard(creditCardNo);
				customer.setRating(rating);
				return customer;
			}
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			return null;
		}
	}
	
	public String deleteCustomer(int accountNo) {

		/*
		 * This method deletes a customer returns "success" string on success, else returns "failure"
		 * The students code to delete the data from the database will be written here
		 * accountNo, which is the Customer's accountNo who's details have to be deleted, is given as method parameter
		 */

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			
			PreparedStatement statement = con.prepareStatement("DELETE FROM Person WHERE Id = ?");
			statement.setInt(1, accountNo);
			statement.executeUpdate();
			return "success";
		}
		catch(Exception e) {
			System.out.println(e);
			return "failure";
		}
	}


	public int getCustomerID(String emailaddress) {
		/*
		* This method returns the Customer's ID based on the provided email address
		* The students code to fetch data from the database will be written here
		* username, which is the email address of the customer, who's ID has to be returned, is given as method parameter
		* The Customer's ID(accountNo) is required to be returned as a String
		*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE Email = ? AND Role = 'Customer' ;");
			statement.setString(1, emailaddress);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()){
				System.out.println("Customer Does Not Exist!");
				return -1;
			}
			else{
				return rs.getInt("Id");
			}
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			return -1;
		}
	}

	public String addCustomer(Customer customer) {

		/*
		 * All the values of the add customer form are encapsulated in the customer object.
		 * These can be accessed by getter methods (see Customer class in model package).
		 * e.g. firstName can be accessed by customer.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database insertion of the customer details and return "success" or "failure" based on result of the database insertion.
		 */
		
		/*data begins*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("INSERT INTO Person(FirstName, LastName, Address, City, State, ZipCode, Password, Email, Role) VALUES(?,?,?,?,?,?,?,?,?);");
			statement.setString(1, customer.getFirstName());
			statement.setString(2, customer.getLastName());
			statement.setString(3, customer.getAddress());
			statement.setString(4, customer.getCity());
			statement.setString(5, customer.getState());
			statement.setString(6, customer.getZipCode());
			statement.setString(7, customer.getPassword());
			statement.setString(8, customer.getEmail());
			statement.setString(9, "Customer");
			statement.executeUpdate();
			
			statement = con.prepareStatement("SELECT * FROM Person WHERE Email = ?");
			statement.setString(1, customer.getEmail());
			ResultSet rs = statement.executeQuery();
			rs.next();
			int id = rs.getInt("Id");
			
			statement = con.prepareStatement("INSERT INTO Customer(Id, AccountNo, CreditCardNo, CreationDate, Rating) VALUES(?,?,?,?,?);");
			statement.setInt(1, id);
			statement.setInt(2, id);
			statement.setString(3, customer.getCreditCard());
			long d = System.currentTimeMillis();
			Date date = new Date(d);
			statement.setDate(4, date);
			statement.setInt(5, customer.getRating());
			statement.executeUpdate();
			
			return "success";
		}
		catch(Exception e) {
			System.out.println(e);
			return "failure";
		}
		/*data ends*/

	}



	public String editCustomer(Customer customer) {
		/*
		* All the values of the edit customer form are encapsulated in the customer object.
		* These can be accessed by getter methods (see Customer class in model package).
		* e.g. firstName can be accessed by customer.getFirstName() method.
		* The sample code returns "success" by default.
		* You need to handle the database update and return "success" or "failure" based on result of the database update.
		*/
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Customer WHERE AccountNo = ?");
			statement.setInt(1, customer.getAccountNo());
			ResultSet rs = statement.executeQuery();
			rs.next();
			int id = rs.getInt("Id");
				
			statement = con.prepareStatement("UPDATE Person SET FirstName= ?,  LastName = ? ,  Address = ? , City = ? , State =? , ZipCode =? ,  Email=? WHERE Id = ?;");
			statement.setString(1, customer.getFirstName());
			statement.setString(2, customer.getLastName());
			statement.setString(3, customer.getAddress());
			statement.setString(4, customer.getCity());
			statement.setString(5, customer.getState());
			statement.setString(6, customer.getZipCode());
			//statement.setString(7, customer.getPassword());
			boolean isValid = isValidEmail(customer.getEmail(), id);
			if(isValid) {
				statement.setString(7, customer.getEmail());
			}
			else {
				return "failure";
			}
			statement.setInt(8, id);
			statement.executeUpdate();
				
			statement = con.prepareStatement("UPDATE Customer SET AccountNo =?, CreditCardNo =?, Rating=?  WHERE Id = ?");
			statement.setInt(1, customer.getAccountNo() );
			statement.setString(2, customer.getCreditCard());
			statement.setInt(3 , customer.getRating());
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

}
