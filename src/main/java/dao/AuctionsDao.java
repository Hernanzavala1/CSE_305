package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Auctions;
import model.Flight;

public class AuctionsDao {
	
	public List<Auctions> getLatestBid(int AccountNo, String AirlineID, int FlightNo, String SeatClass) {

		/*
		 * This method fetches the latest auction details and returns it
		 * using method parameters given, find the latest bid
		 * The students code to fetch data from the database will be written here
		 * The Auctions record is required to be encapsulated as a "Auctions" class object
		 */
		
		List<Auctions> auctions = new ArrayList<Auctions>();
		/*Sample data begins*/
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Auctions WHERE AccountNo = ? AND AirlineID = ? AND FlightNo = ? AND Class = ? ORDER BY `Date` DESC LIMIT 0,1;");
			statement.setInt(1, AccountNo);
			statement.setString(2, AirlineID);
			statement.setInt(3, FlightNo);
			statement.setString(4, SeatClass);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()){
				System.out.println("Customer has not placed a bid");
				return auctions;
			}
			Auctions auction = new Auctions();
			auction.setAccountNo(rs.getInt("AccountNo"));
			auction.setAirlineID(rs.getString("AirlineID"));
			auction.setFlightNo(rs.getInt("FlightNo"));
			auction.setSeatClass(rs.getString("Class"));
			auction.setAccepted(false);
			
			Timestamp ts = rs.getTimestamp("Date");
			String s = new SimpleDateFormat("MM/dd/yyyy").format(ts);
			auction.setDate(s);
			auction.setNYOP(rs.getInt("NYOP"));
	
			auctions.add(auction);
			
			return auctions;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public List<Auctions> getAllBids(int AccountNo, String AirlineID, int FlightNo, String SeatClass) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get all bids given the parameters
		 */
		
		List<Auctions> auctions = new ArrayList<Auctions>();
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Auctions WHERE AccountNo = ? AND AirlineID = ? AND FlightNo = ? AND Class =? ORDER BY 'Date' DESC");
			statement.setInt(1, AccountNo);
			statement.setString(2, AirlineID);
			statement.setInt(3, FlightNo);
			statement.setString(4, SeatClass);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Auctions auction = new Auctions();
				auction.setAccountNo(rs.getInt("AccountNo"));
				auction.setAirlineID(rs.getString("AirlineID"));
				auction.setFlightNo(rs.getInt("FlightNo"));
				auction.setSeatClass(rs.getString("Class"));
				auction.setAccepted(false);
				
				Timestamp ts = rs.getTimestamp("Date");
				String s = new SimpleDateFormat("MM/dd/yyyy").format(ts);
				auction.setDate(s);
				auction.setNYOP(rs.getInt("NYOP"));
		
				auctions.add(auction);
			}
			return auctions;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
