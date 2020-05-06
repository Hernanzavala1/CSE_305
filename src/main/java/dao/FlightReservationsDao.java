package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.FlightReservations;

public class FlightReservationsDao {
	
	public List<FlightReservations> getReservations(int FlightNum, String airlineID, String CustomerName) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get flight reservations based on FlightNum OR CustomerName passed
		 * Only one of the two strings will be set, either (FlightNum = 0 and airlineID="") or CustomerName = "" depending on query
		 */
		
		List<FlightReservations> reservations = new ArrayList<FlightReservations>();
		if(CustomerName.equals("")) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT DISTINCT R.ResrNo, R.ResrDate, R.TotalFare, R.BookingFee, R.RepSSN, P.FirstName, P.LastName FROM Reservation R, Customer C, Includes I, Person P WHERE R.AccountNo = C.AccountNo AND C.Id = P.Id AND I.ResrNo = R.ResrNo AND I.AirlineID= ? AND I.FlightNo = ?");
				statement.setString(1, airlineID);
				statement.setInt(2, FlightNum);
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					FlightReservations reservation = new FlightReservations();
					Date dateObj = rs.getDate("ResrDate");
			        String date = dateObj.toString();
					reservation.setResrNo(rs.getInt("ResrNo"));
					reservation.setResrDate(date);
					reservation.setTotalFare(rs.getDouble("TotalFare")); 
					reservation.setBookingFee(rs.getDouble("BookingFee"));
					reservation.setRepSSN(rs.getString("RepSSN"));
					reservation.setFirstName(rs.getString("FirstName"));
					reservation.setLastName(rs.getString("LastName"));
			
					reservations.add(reservation);
				}
			}
			catch(Exception e) {
				System.out.println(e);
				return null;
			}
		}
		else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT DISTINCT R.ResrNo, R.ResrDate, R.TotalFare, R.BookingFee, R.RepSSN, P.FirstName, P.LastName FROM Reservation R, Customer C, Person P WHERE R.AccountNo = C.AccountNo AND C.Id = P.Id AND P.FirstName = ? AND P.LastName = ?");
				String first = CustomerName.split(" ")[0];
				String last = CustomerName.split(" ")[1];
				statement.setString(1, first);
				statement.setString(2, last);
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					FlightReservations reservation = new FlightReservations();
					Date dateObj = rs.getDate("ResrDate");
			        String date = dateObj.toString();
					reservation.setResrNo(rs.getInt("ResrNo"));
					reservation.setResrDate(date);
					reservation.setTotalFare(rs.getDouble("TotalFare")); 
					reservation.setBookingFee(rs.getDouble("BookingFee"));
					reservation.setRepSSN(rs.getString("RepSSN"));
					reservation.setFirstName(rs.getString("FirstName"));
					reservation.setLastName(rs.getString("LastName"));
			
					reservations.add(reservation);
				}
			}
			catch(Exception e) {
				System.out.println(e);
				return null;
			}
		}				
		return reservations;
	}
	
	public List<FlightReservations> getRevenueSummary(int FlightNum, String airlineID, String CustomerName,String destCity) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get flight reservations based on FlightNum OR CustomerName passed
		 * Only one of the two strings will be set, either (FlightNum = 0 and airlineID = "") or CustomerName = ""  
		 * or destCity = "" depending on query
		 */
		
		List<FlightReservations> reservations = new ArrayList<FlightReservations>();
		
		if(!CustomerName.equals("")) {
			//Customer
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT * FROM Person WHERE FirstName = ? AND LastName = ?");
				String first = CustomerName.split(" ")[0];
				String last = CustomerName.split(" ")[1];
				statement.setString(1, first);
				statement.setString(2, last);
				ResultSet rs = statement.executeQuery();
				rs.next();
				int id = rs.getInt("Id");
				
				statement = con.prepareStatement("SELECT * FROM Customer WHERE Id = ?");
				statement.setInt(1, id);
				rs = statement.executeQuery();
				rs.next();
				int accountNo = rs.getInt("AccountNo");
				
				statement = con.prepareStatement("SELECT DISTINCT R.ResrNo, R.TotalFare * 0.1 AS Revenue FROM Reservation R WHERE R.AccountNo = ?");
				statement.setInt(1, accountNo);
				rs = statement.executeQuery();
				while(rs.next()) {
					FlightReservations reservation = new FlightReservations();
					reservation.setResrNo(rs.getInt("ResrNo"));
					reservation.setRevenue(rs.getDouble("Revenue"));
			
					reservations.add(reservation);
				}
			}
			catch(Exception e) {
				System.out.println(e);
				return null;
			}
		}
		else if(!destCity.equals("")) {
			//dest
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("CREATE VIEW ResrFlightLastLeg(ResrNo, AirlineID, FlightNo, LegNo) AS SELECT I.ResrNo, I.AirlineID, I.FlightNo, MAX(I.LegNo) FROM Includes I GROUP BY I.ResrNo, I.AirlineID, I.FlightNo;");
				statement.executeUpdate();
				statement = con.prepareStatement("SELECT DISTINCT R.ResrNo, R.TotalFare * 0.1 AS Revenue FROM Reservation R, Leg L, ResrFlightLastLeg LL, Airport A WHERE R.ResrNo = LL.ResrNo AND L.AirlineID = LL.AirlineID AND L.FlightNo = LL.FlightNo AND L.LegNo = LL.LegNo AND L.ArrAirportID = A.ID AND A.City = ?;");
				statement.setString(1, destCity);
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					FlightReservations reservation = new FlightReservations();
					reservation.setResrNo(rs.getInt("ResrNo"));
					reservation.setRevenue(rs.getDouble("Revenue"));
			
					reservations.add(reservation);
				}
				statement = con.prepareStatement("DROP VIEW ResrFlightLastLeg;");
				statement.executeUpdate();
			}
			catch(Exception e) {
				System.out.println(e);
				return null;
			}
		}
		else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT DISTINCT R.ResrNo, R.TotalFare * 0.1 AS Revenue FROM Reservation R, Includes I WHERE I.ResrNo = R.ResrNo AND I.AirlineID=? AND I.FlightNo = ?;");
				statement.setString(1, airlineID);
				statement.setInt(2, FlightNum);
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					FlightReservations reservation = new FlightReservations();
					reservation.setResrNo(rs.getInt("ResrNo"));
					reservation.setRevenue(rs.getDouble("Revenue"));
			
					reservations.add(reservation);
				}
			}
			catch(Exception e) {
				System.out.println(e);
				return null;
			}
		}
		return reservations;
	}
	
	
	public List<FlightReservations> getPassengerList(int FlightNum, String AirlineID) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get passenger list given flight number and Airline ID
		 */
		
		List<FlightReservations> reservations = new ArrayList<FlightReservations>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT DISTINCT P.Id, P.FirstName, P.LastName FROM Reservation R, Includes I, ReservationPassenger RP, Person P WHERE I.AirlineID= ? AND I.FlightNo = ? AND I.ResrNo = R.ResrNo AND R.ResrNo = RP.ResrNo AND RP.Id = P.Id;");
			statement.setString(1, AirlineID);
			statement.setInt(2, FlightNum);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				FlightReservations reservation = new FlightReservations();
				reservation.setPassengerID(rs.getInt("Id"));
				reservation.setFirstName(rs.getString("FirstName"));
				reservation.setLastName(rs.getString("LastName"));
		
				reservations.add(reservation);
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		return reservations;
		
	}
	
	public List<FlightReservations> getCurrentReservations(int accountNo) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get current flight reservations based on accountno 
		 */
		
		List<FlightReservations> reservations = new ArrayList<FlightReservations>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Reservation R WHERE EXISTS (SELECT * FROM Includes I, Leg L WHERE R.ResrNo = I.ResrNo AND I.AirlineID = L.AirlineID AND I.FlightNo = L.FlightNo AND L.DepTime >= NOW()) AND R.AccountNo = ?");
			statement.setInt(1, accountNo);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				FlightReservations reservation = new FlightReservations();
				reservation.setResrNo(rs.getInt("ResrNo"));
				Date dateObj = rs.getDate("ResrDate");
		        String date = dateObj.toString();
				reservation.setResrDate(date);
				reservation.setTotalFare(rs.getDouble("TotalFare")); 
				reservation.setBookingFee(rs.getDouble("BookingFee"));
				reservation.setRepSSN(rs.getString("RepSSN"));
				reservation.setAccountNo(accountNo);
		
				reservations.add(reservation);
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}

		return reservations;
		
	}

	public List<FlightReservations> getAllReservations(int accountNo) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get all flight reservations based on accountno 
		 */
		
		List<FlightReservations> reservations = new ArrayList<FlightReservations>();
			
		/*Sample data begins*/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Reservation WHERE AccountNo = ?;");
			statement.setInt(1, accountNo);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				FlightReservations reservation = new FlightReservations();
				reservation.setResrNo(rs.getInt("ResrNo"));
				Date dateObj = rs.getDate("ResrDate");
		        String date = dateObj.toString();
				reservation.setResrDate(date);
				reservation.setTotalFare(rs.getDouble("TotalFare")); 
				reservation.setBookingFee(rs.getDouble("BookingFee"));
				reservation.setRepSSN(rs.getString("RepSSN"));
				reservation.setAccountNo(accountNo);
		
				reservations.add(reservation);
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		/*Sample data ends*/
						
		return reservations;
		
	}
	
	public String cancelReservation(int resrNo) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get cancel reservations based on resrNo
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("DELETE FROM Includes WHERE ResrNo = ?;");
			statement.setInt(1, resrNo);
			statement.executeUpdate();
			
			statement = con.prepareStatement("DELETE FROM ReservationPassenger WHERE ResrNo = ?;");
			statement.setInt(1, resrNo);
			statement.executeUpdate();
			
			statement = con.prepareStatement("DELETE FROM Reservation WHERE ResrNo = ?;");
			statement.setInt(1, resrNo);
			statement.executeUpdate();
		}
		catch(Exception e) {
			System.out.println(e);
			return "failure";
		}
		return "success";
		
	}


}
