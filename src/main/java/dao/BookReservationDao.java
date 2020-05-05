package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import model.BookReservation;

public class BookReservationDao {

	@SuppressWarnings("resource")
	public String bookOneWayRoundTripReservation(BookReservation bookRes) throws ParseException {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to book reservations based on bookRes object passed
		 * repSSN will be set depending on who booked the reservation
		 * Use getters to fetch the data from the object
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */
	
		String tripType = bookRes.getTypeOfTrip();
		if(tripType.equals("oneway")) {
			if(!validateAirlineId(bookRes.getAirlineID())) {
				System.out.println("AirlineID is not valid");
				return "failure";
			}
			if(!validateAirportMulti(bookRes.getDepartureAirport(), bookRes.getArrivalAirport())) {
				return "failure";
			}
			if(!validateDate(bookRes.getDepartureDate(), bookRes.getFlightNum1())) {
				System.out.println("Flight is not operating on that day");
				return "failure";
			}
			if(!bookRes.getSeatClass().trim().equals("First") && !bookRes.getSeatClass().trim().equals("Economy") && !bookRes.getSeatClass().trim().equals("Basic")) {
				System.out.println("Seat class must either be: First, Economy, or Basic");
				return "failure";
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT * FROM Fare WHERE AirlineID = ? AND FlightNo = ? AND Class = ?;");
				statement.setString(1, bookRes.getAirlineID());
				statement.setInt(2, bookRes.getFlightNum1());
				statement.setString(3, bookRes.getSeatClass());
				ResultSet rs = statement.executeQuery();
				if(!rs.next()) {
					System.out.println("Fare does not exist for this airline/flight/fareType");
					return "failure";
				}
				double fare = rs.getDouble("Fare");
				double bookingFee = fare * 0.1;
				double totalFare = fare + bookingFee;
				String repSSN = bookRes.getRepSSN();
				
				statement = con.prepareStatement("SELECT * FROM Person WHERE Email = ?;");
				statement.setString(1, bookRes.getPassEmail());
				rs = statement.executeQuery();
				if(!rs.next()) {
					System.out.println("That email does not exist!");
					return "failure";
				}
				int customerID = rs.getInt("Id");
				
				statement = con.prepareStatement("SELECT * FROM Customer WHERE Id = ?;");
				statement.setInt(1, customerID);
				rs = statement.executeQuery();
				rs.next();
				int accountNo = rs.getInt("AccountNo");
				
				int resrNo = randomResrNo();
				if(repSSN.equals("")) {
					statement = con.prepareStatement("INSERT INTO Reservation(ResrNo, ResrDate, BookingFee, TotalFare, AccountNo) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					long d = System.currentTimeMillis();
					Date date = new Date(d);
					statement.setDate(2, date);
					statement.setDouble(3, bookingFee);
					statement.setDouble(4, totalFare);
					statement.setInt(5, accountNo);
					statement.executeUpdate();
					
					statement = con.prepareStatement("SELECT * FROM Leg WHERE AirlineID = ? AND FlightNo = ? AND DepAirportID = ?;");
					statement.setString(1, bookRes.getAirlineID());
					statement.setInt(2, bookRes.getFlightNum1());
					statement.setString(3, bookRes.getDepartureAirport());
					rs = statement.executeQuery();
					rs.next();
					int legNo = rs.getInt("LegNo");
					
					String includesDate = bookRes.getDepartureDate();
					includesDate = includesDate.trim();
					String month = includesDate.substring(0, 2);
					String day = includesDate.substring(3, 5);
					String year = includesDate.substring(6, 10);
					includesDate = year + "-" + month + "-" + day;
					Date includesNewDate=Date.valueOf(includesDate);
					statement = con.prepareStatement("INSERT INTO Includes(ResrNo, AirlineID, FlightNo, LegNo, Date) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					statement.setString(2, bookRes.getAirlineID());
					statement.setInt(3, bookRes.getFlightNum1());
					statement.setInt(4, legNo);
					statement.setDate(5, includesNewDate);
					statement.executeUpdate();
				}
				else {
					statement = con.prepareStatement("INSERT INTO Reservation(ResrNo, ResrDate, BookingFee, TotalFare, RepSSN, AccountNo) VALUES(?,?,?,?,?,?);");
					statement.setInt(1, resrNo);
					long d = System.currentTimeMillis();
					Date date = new Date(d);
					statement.setDate(2, date);
					statement.setDouble(3, bookingFee);
					statement.setDouble(4, totalFare);
					statement.setString(5, repSSN);
					statement.setInt(6, accountNo);
					statement.executeUpdate();
					
					statement = con.prepareStatement("SELECT * FROM Leg WHERE AirlineID = ? AND FlightNo = ? AND DepAirportID = ?;");
					statement.setString(1, bookRes.getAirlineID());
					statement.setInt(2, bookRes.getFlightNum1());
					statement.setString(3, bookRes.getDepartureAirport());
					rs = statement.executeQuery();
					rs.next();
					int legNo = rs.getInt("LegNo");
					
					String includesDate = bookRes.getDepartureDate();
					includesDate = includesDate.trim();
					String month = includesDate.substring(0, 2);
					String day = includesDate.substring(3, 5);
					String year = includesDate.substring(6, 10);
					includesDate = year + "-" + month + "-" + day;
					Date includesNewDate=Date.valueOf(includesDate);
					statement = con.prepareStatement("INSERT INTO Includes(ResrNo, AirlineID, FlightNo, LegNo, Date) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					statement.setString(2, bookRes.getAirlineID());
					statement.setInt(3, bookRes.getFlightNum1());
					statement.setInt(4, legNo);
					statement.setDate(5, includesNewDate);
					statement.executeUpdate();
				}
				statement = con.prepareStatement("SELECT * FROM Passenger WHERE Id = ? AND AccountNo = ?");
				statement.setInt(1, customerID);
				statement.setInt(2, accountNo);
				rs = statement.executeQuery();
				if(!rs.next()) {
					statement = con.prepareStatement("INSERT INTO Passenger(Id, AccountNo) VALUES(?,?);");
					statement.setInt(1, customerID);
					statement.setInt(2, accountNo);
					statement.executeUpdate();
				}
				
				statement = con.prepareStatement("INSERT INTO ReservationPassenger(ResrNo, Id, AccountNo, SeatNo, Class, Meal) VALUES(?,?,?,?,?,?);");
				statement.setInt(1, resrNo);
				statement.setInt(2, customerID);
				statement.setInt(3, accountNo);
				statement.setString(4, bookRes.getSeatNum());
				statement.setString(5, bookRes.getSeatClass());
				statement.setString(6, bookRes.getMealPref());
				statement.executeUpdate();
				
				return "success";
			}
			catch(Exception e) {
				System.out.println(e);
				return "failure";
			}
		}
		else {
			/*ROUND TRIP */
			
			if(!validateAirlineId(bookRes.getAirlineID())) {
				System.out.println("AirlineID is not valid");
				return "failure";
			}
			if(!validateAirportMulti(bookRes.getDepartureAirport(), bookRes.getArrivalAirport())) {
				return "failure";
			}
			if(!validateDateMulti(bookRes.getDepartureDate(), bookRes.getReturnDate(), bookRes.getFlightNum1(), bookRes.getFlightNum2())) {
				System.out.println("Flight is not operating on that day");
				return "failure";
			}
			if(!bookRes.getSeatClass().trim().equals("First") && !bookRes.getSeatClass().trim().equals("Economy") && !bookRes.getSeatClass().trim().equals("Basic")) {
				System.out.println("Seat class must either be: First, Economy, or Basic");
				return "failure";
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT * FROM Fare WHERE AirlineID = ? AND FlightNo = ? AND Class = ?;");
				statement.setString(1, bookRes.getAirlineID());
				statement.setInt(2, bookRes.getFlightNum1());
				statement.setString(3, bookRes.getSeatClass());
				ResultSet rs = statement.executeQuery();
				if(!rs.next()) {
					return "failure";
				}
				double fare = rs.getDouble("Fare");
				double bookingFee = fare * 0.1;
				double totalFare = fare + bookingFee;
				String repSSN = bookRes.getRepSSN();
				
				statement = con.prepareStatement("SELECT * FROM Person WHERE Email = ?;");
				statement.setString(1, bookRes.getPassEmail());
				rs = statement.executeQuery();
				if(!rs.next()) {
					System.out.println("That email does not exist!");
					return "failure";
				}
				int customerID = rs.getInt("Id");
				
				statement = con.prepareStatement("SELECT * FROM Customer WHERE Id = ?;");
				statement.setInt(1, customerID);
				rs = statement.executeQuery();
				rs.next();
				int accountNo = rs.getInt("AccountNo");
				
				int resrNo = randomResrNo();
				if(repSSN.equals("")) {
					statement = con.prepareStatement("INSERT INTO Reservation(ResrNo, ResrDate, BookingFee, TotalFare, AccountNo) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					long d = System.currentTimeMillis();
					Date date = new Date(d);
					statement.setDate(2, date);
					statement.setDouble(3, bookingFee);
					statement.setDouble(4, totalFare);
					statement.setInt(5, accountNo);
					statement.executeUpdate();
					
					statement = con.prepareStatement("SELECT * FROM Leg WHERE AirlineID = ? AND FlightNo = ? AND DepAirportID = ?;");
					statement.setString(1, bookRes.getAirlineID());
					statement.setInt(2, bookRes.getFlightNum1());
					statement.setString(3, bookRes.getDepartureAirport());
					rs = statement.executeQuery();
					rs.next();
					int legNo = rs.getInt("LegNo");
					
					String includesDate = bookRes.getDepartureDate();
					includesDate = includesDate.trim();
					String month = includesDate.substring(0, 2);
					String day = includesDate.substring(3, 5);
					String year = includesDate.substring(6, 10);
					includesDate = year + "-" + month + "-" + day;
					Date includesNewDate=Date.valueOf(includesDate);
					statement = con.prepareStatement("INSERT INTO Includes(ResrNo, AirlineID, FlightNo, LegNo, Date) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					statement.setString(2, bookRes.getAirlineID());
					statement.setInt(3, bookRes.getFlightNum1());
					statement.setInt(4, legNo);
					statement.setDate(5, includesNewDate);
					statement.executeUpdate();
					
					statement = con.prepareStatement("SELECT * FROM Leg WHERE AirlineID = ? AND FlightNo = ? AND DepAirportID = ?;");
					statement.setString(1, bookRes.getAirlineID());
					statement.setInt(2, bookRes.getFlightNum2());
					statement.setString(3, bookRes.getArrivalAirport());
					rs = statement.executeQuery();
					rs.next();
					int legNo2 = rs.getInt("LegNo");
					
					String includesDate2 = bookRes.getReturnDate();
					includesDate2 = includesDate2.trim();
					String month2 = includesDate2.substring(0, 2);
					String day2 = includesDate2.substring(3, 5);
					String year2 = includesDate2.substring(6, 10);
					includesDate2 = year2 + "-" + month2 + "-" + day2;
					Date includesNewDate2=Date.valueOf(includesDate2);
					statement = con.prepareStatement("INSERT INTO Includes(ResrNo, AirlineID, FlightNo, LegNo, Date) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					statement.setString(2, bookRes.getAirlineID());
					statement.setInt(3, bookRes.getFlightNum2());
					statement.setInt(4, legNo2);
					statement.setDate(5, includesNewDate2);
					statement.executeUpdate();
				}
				else {
					statement = con.prepareStatement("INSERT INTO Reservation(ResrNo, ResrDate, BookingFee, TotalFare, RepSSN, AccountNo) VALUES(?,?,?,?,?,?);");
					statement.setInt(1, resrNo);
					long d = System.currentTimeMillis();
					Date date = new Date(d);
					statement.setDate(2, date);
					statement.setDouble(3, bookingFee);
					statement.setDouble(4, totalFare);
					statement.setString(5, repSSN);
					statement.setInt(6, accountNo);
					statement.executeUpdate();
					
					statement = con.prepareStatement("SELECT * FROM Leg WHERE AirlineID = ? AND FlightNo = ? AND DepAirportID = ?;");
					statement.setString(1, bookRes.getAirlineID());
					statement.setInt(2, bookRes.getFlightNum1());
					statement.setString(3, bookRes.getDepartureAirport());
					rs = statement.executeQuery();
					rs.next();
					int legNo = rs.getInt("LegNo");
					
					String includesDate = bookRes.getDepartureDate();
					includesDate = includesDate.trim();
					String month = includesDate.substring(0, 2);
					String day = includesDate.substring(3, 5);
					String year = includesDate.substring(6, 10);
					includesDate = year + "-" + month + "-" + day;
					Date includesNewDate=Date.valueOf(includesDate);
					statement = con.prepareStatement("INSERT INTO Includes(ResrNo, AirlineID, FlightNo, LegNo, Date) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					statement.setString(2, bookRes.getAirlineID());
					statement.setInt(3, bookRes.getFlightNum1());
					statement.setInt(4, legNo);
					statement.setDate(5, includesNewDate);
					statement.executeUpdate();
					
					statement = con.prepareStatement("SELECT * FROM Leg WHERE AirlineID = ? AND FlightNo = ? AND DepAirportID = ?;");
					statement.setString(1, bookRes.getAirlineID());
					statement.setInt(2, bookRes.getFlightNum2());
					statement.setString(3, bookRes.getArrivalAirport());
					rs = statement.executeQuery();
					rs.next();
					int legNo2 = rs.getInt("LegNo");
					
					String includesDate2 = bookRes.getReturnDate();
					includesDate2 = includesDate2.trim();
					String month2 = includesDate2.substring(0, 2);
					String day2 = includesDate2.substring(3, 5);
					String year2 = includesDate2.substring(6, 10);
					includesDate2 = year2 + "-" + month2 + "-" + day2;
					Date includesNewDate2=Date.valueOf(includesDate2);
					statement = con.prepareStatement("INSERT INTO Includes(ResrNo, AirlineID, FlightNo, LegNo, Date) VALUES(?,?,?,?,?);");
					statement.setInt(1, resrNo);
					statement.setString(2, bookRes.getAirlineID());
					statement.setInt(3, bookRes.getFlightNum2());
					statement.setInt(4, legNo2);
					statement.setDate(5, includesNewDate2);
					statement.executeUpdate();
				}
				statement = con.prepareStatement("SELECT * FROM Passenger WHERE Id = ? AND AccountNo = ?");
				statement.setInt(1, customerID);
				statement.setInt(2, accountNo);
				rs = statement.executeQuery();
				if(!rs.next()) {
					statement = con.prepareStatement("INSERT INTO Passenger(Id, AccountNo) VALUES(?,?);");
					statement.setInt(1, customerID);
					statement.setInt(2, accountNo);
					statement.executeUpdate();
				}
				
				statement = con.prepareStatement("INSERT INTO ReservationPassenger(ResrNo, Id, AccountNo, SeatNo, Class, Meal) VALUES(?,?,?,?,?,?);");
				statement.setInt(1, resrNo);
				statement.setInt(2, customerID);
				statement.setInt(3, accountNo);
				statement.setString(4, bookRes.getSeatNum());
				statement.setString(5, bookRes.getSeatClass());
				statement.setString(6, bookRes.getMealPref());
				statement.executeUpdate();
				
				return "success";
			}
			catch(Exception e) {
				System.out.println(e);
				return "failure";
			}
		}
		
	}
	
	public int randomResrNo() {
		try {
			boolean continueLoop = true;
			int result = -1;
			while(continueLoop) {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				Random r = new Random();
		
				result = r.nextInt(999999999-100) + 100;
				PreparedStatement statement = con.prepareStatement("SELECT * FROM Reservation WHERE ResrNo = ?;");
				statement.setInt(1, result);
				ResultSet rs = statement.executeQuery();
				if(!rs.next()) {
					return result;
				}
			}
			return result;
			
		}
		catch(Exception e) {
			System.out.println(e);
			return -1;
		}
	}
	
	
	public boolean validateAirlineId(String airlineID) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Airline WHERE Id = ?;");
			statement.setString(1, airlineID);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				return false;
			}
			else {
				return true;
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean validateAirportMulti(String arrival, String departure) {
		if(arrival.equals(departure)) {
			System.out.println("Arrival Airport can not be the same as the Departure Airport");
			return false;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Airport WHERE Id = ? OR Id = ?;");
			statement.setString(1, arrival);
			statement.setString(2, departure);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				System.out.println("One or both of the AirportIDs does not exist");
				return false;
			}
			else {
				return true;
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean validateDate(String departureDate, int flight1) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Flight WHERE FlightNo = ?");
			statement.setInt(1, flight1);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				return false;
			}
			else {
				java.util.Date DeTemp=new SimpleDateFormat("MM/dd/yyyy").parse(departureDate);  
			
				Calendar calendar = new GregorianCalendar();
		        calendar.setTime(DeTemp);
		        int newDay = calendar.get(Calendar.DAY_OF_WEEK);
				String days = rs.getString("DaysOperating");
				for(int i = 0; i < days.length(); i++) {
					if(i == newDay-1 && days.charAt(i) != '1') {
						return false;
					}
					if(i > newDay) {
						break;
					}
				}
				return true;
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean validateDateMulti(String departureDate, String returnDate, int flight1, int flight2) throws ParseException {
		java.util.Date DTemp=new SimpleDateFormat("MM/dd/yyyy").parse(departureDate);  
		java.util.Date RTemp=new SimpleDateFormat("MM/dd/yyyy").parse(returnDate);

		Calendar temp = new GregorianCalendar();
        temp.setTime(DTemp);
        Calendar temp2 = new GregorianCalendar();
        temp2.setTime(RTemp);
		if(temp.compareTo(temp2) > 0) {
			System.out.println("Return date cannot be before departure date");
			return false;
		}
		try {
			boolean departureIsValid = false;
			boolean returnIsValid = false;
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Flight WHERE FlightNo = ?");
			statement.setInt(1, flight1);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				return false;
			}
			else {
				java.util.Date utilDateD2=new SimpleDateFormat("MM/dd/yyyy").parse(departureDate);  
				java.util.Date utilDateR2=new SimpleDateFormat("MM/dd/yyyy").parse(returnDate);
				int min = rs.getInt("MinLengthOfStay");
				int max = rs.getInt("MaxLengthOfStay");
				Calendar cal = new GregorianCalendar();
		        cal.setTime(utilDateD2);
		        Calendar cal2 = new GregorianCalendar();
		        cal2.setTime(utilDateR2);
		        cal.add(Calendar.DATE, min);
		        if(cal.getTime().compareTo(cal2.getTime()) > 0) {
					System.out.println("Return date cannot be before min length of stay");
					return false;
				}
		        
		        cal.setTime(utilDateD2);
		        cal.add(Calendar.DATE, max);
		        if(cal.getTime().compareTo(cal2.getTime()) < 0) {
					System.out.println("Return date cannot be after max length of stay");
					return false;
				}
		        
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(utilDateD2);
		        int day = calendar.get(Calendar.DAY_OF_WEEK);
				String days = rs.getString("DaysOperating");
				for(int i = 0; i < days.length(); i++) {
					if(i == day-1 && days.charAt(i) != '1') {
						return false;
					}
					if(i > day) {
						break;
					}
				}
				departureIsValid = true;
			}
		
			
			statement = con.prepareStatement("SELECT * FROM Flight WHERE FlightNo = ?");
			statement.setInt(1, flight2);
			rs = statement.executeQuery();
			if(!rs.next()) {
				return false;
			}
			else {
				java.util.Date utilDateR3=new SimpleDateFormat("MM/dd/yyyy").parse(returnDate);
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(utilDateR3);
		        int day = calendar.get(Calendar.DAY_OF_WEEK);
				String days = rs.getString("DaysOperating");
				for(int i = 0; i < days.length(); i++) {
					if(i == day-1 && days.charAt(i) != '1') {
						return false;
					}
					if(i > day) {
						break;
					}
				}
				returnIsValid = true;
			}
			return returnIsValid && departureIsValid;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public String bookMultiCityReservation(BookReservation bookRes) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to book reservations based on bookRes object passed
		 * repSSN will be set depending on who booked the reservation
		 * Use getters to fetch the data from the object
		 * DepartureAirport1, ArrivalAirport1, DepartureAirport2, ArrivalAirport2, Trip1Date, Trip2Date are the attributes to use here
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */
					
		return "success";
		
	}
}
