package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Flight;
import model.FlightReservations;

public class FlightDao {
	
	public List<Flight> getAllFlights() {
		/* Get list of all flights, code goes here
		 */
		List<Flight> flights = new ArrayList<Flight>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Flight;");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Flight flight = new Flight();
				flight.setAirlineID(rs.getString("AirlineID"));
				flight.setFlightNo(rs.getInt("FlightNo"));
				flight.setNumOfSeats(rs.getInt("NoOfSeats"));
				flight.setDaysOperating(rs.getString("DaysOperating"));
				flight.setMinLengthOfStay(rs.getInt("MinLengthOfStay"));
				flight.setMaxLengthOfStay(rs.getInt("MaxLengthOfStay"));
				flights.add(flight);
			}
			return flights;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		/*Sample data ends*/
	}
	public List<Flight> mostActiveFlights() {
		
		/* Get list of most active flights, code goes here
		 */
		
		List<Flight> flights = new ArrayList<Flight>();
		
		for (int i = 0; i < 5; i++) {
			Flight flight = new Flight();
			flight.setAirlineID("AA");
			flight.setFlightNo(111);
			flight.setNumReservations(30);
			flights.add(flight);			
		}
		/*Sample data ends*/
		
		return flights;
	}
	
	public List<Flight> getFlightsForAirport(String airport) {
		
		/*
		 * Code here to get flights given an airport
		 */
		
		List<Flight> flights = new ArrayList<Flight>();
		
		for (int i = 0; i < 5; i++) {
			Flight flight = new Flight();
			flight.setAirlineID("AA");
			flight.setFlightNo(111);
			flight.setNumOfSeats(100);
			flight.setDaysOperating("1010100");
			flight.setMinLengthOfStay(1);
			flight.setMaxLengthOfStay(30);
			flights.add(flight);			
		}
		/*Sample data ends*/
		
		return flights;
	}
	public List<Flight> getOnTimeFlights() {
		
		/*
		 * Code here to get on time flights
		 */
		
		List<Flight> flights = new ArrayList<Flight>();
		
		for (int i = 0; i < 5; i++) {
			Flight flight = new Flight();
			flight.setAirlineID("AA");
			flight.setFlightNo(111);
			flight.setNumOfSeats(100);
			flight.setDaysOperating("1010100");
			flight.setMinLengthOfStay(1);
			flight.setMaxLengthOfStay(30);
			flights.add(flight);			
		}
		/*Sample data ends*/
		
		return flights;
	}
	public List<Flight> getDelayedFlights() {
		
		/*
		 * Code here to get delayed flights
		 */
		
		List<Flight> flights = new ArrayList<Flight>();
		
		for (int i = 0; i < 5; i++) {
			Flight flight = new Flight();
			flight.setAirlineID("AA");
			flight.setFlightNo(111);
			flight.setNumOfSeats(100);
			flight.setDaysOperating("1010100");
			flight.setMinLengthOfStay(1);
			flight.setMaxLengthOfStay(30);
			flights.add(flight);			
		}
		/*Sample data ends*/
		
		return flights;
	}
	
	public List<Flight> getCustomerFlightSuggestions(int accountNo) {
		
		/* Get list of suggested flights depending on customer's accountNo passed
		 */
		
		List<Flight> flights = new ArrayList<Flight>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("CREATE VIEW FlightReservation(AirlineID, FlightNo, ResrCount) AS SELECT I.AirlineID, I.FlightNo, COUNT(DISTINCT I.ResrNo) FROM Includes I GROUP BY I.AirlineID, I.FlightNo;"); 
			statement.executeUpdate();
			statement = con.prepareStatement("SELECT * FROM FlightReservation FR WHERE NOT EXISTS (SELECT * FROM Reservation R, Includes I WHERE R.ResrNo = I.ResrNo AND FR.AirlineID = I.AirlineID AND FR.FlightNo = I.FlightNo AND R.AccountNo = ?) ORDER BY FR.ResrCount DESC");
			statement.setInt(1, accountNo);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Flight flight = new Flight();
				flight.setAirlineID(rs.getString("AirlineID"));
				flight.setFlightNo(rs.getInt("FlightNo"));
				flight.setNumReservations(rs.getInt("ResrCount"));
				flights.add(flight);	
			}
			statement = con.prepareStatement("DROP View FlightReservation;");
			statement.executeUpdate();
			return flights;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public List<Flight> getBestSellingFlights() {
		
		/* Get list of best selling flights
		 */
		
		List<Flight> flights = new ArrayList<Flight>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("CREATE VIEW FlightReservation(AirlineID, FlightNo, ResrCount) AS SELECT I.AirlineID, I.FlightNo, COUNT(DISTINCT I.ResrNo) FROM Includes I GROUP BY I.AirlineID, I.FlightNo;");
			statement.executeUpdate();
			statement = con.prepareStatement("SELECT * FROM FlightReservation ORDER BY ResrCount DESC;");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Flight flight = new Flight();
				flight.setAirlineID(rs.getString("AirlineID"));
				flight.setFlightNo(rs.getInt("FlightNo"));
				flight.setNumReservations(rs.getInt("ResrCount"));
				flights.add(flight);		
			}
			statement = con.prepareStatement("DROP View FlightReservation;");
			statement.executeUpdate();
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
		return flights;
	}
}