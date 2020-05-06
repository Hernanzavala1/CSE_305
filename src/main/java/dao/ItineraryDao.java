package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Flight;
import model.Itinerary;

public class ItineraryDao {
	
	public List<Itinerary> getItineraryForReservation(int resrNo) {
			/*
			 * Code to fetch itinerary from resrNo goes here
			 */
		
			List<Itinerary> its = new ArrayList<Itinerary>();
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
				PreparedStatement statement = con.prepareStatement("SELECT DISTINCT I.ResrNo, I.AirlineID, I.FlightNo, DA.Name AS Departing, AA.Name AS Arriving, L.DepTime, L.ArrTime FROM Includes I, Leg L, Airport DA, Airport AA WHERE I.AirlineID = L.AirlineID AND I.FlightNo = L.FlightNo AND L.DepAirportID = DA.Id AND L.ArrAirportID = AA.Id AND I.ResrNo = ?");
				statement.setInt(1,resrNo);
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					Itinerary it = new Itinerary();
					it.setAirlineID(rs.getString("AirlineID"));
					it.setArrival(rs.getString("Arriving"));
					it.setDeparture(rs.getString("Departing"));
					Timestamp arrTime = rs.getTimestamp("ArrTime");
					String arr = arrTime.toString();
					it.setArrTime(arr);
					Timestamp depTime = rs.getTimestamp("DepTime");
					String depp = depTime.toString();
					it.setDepTime(depp);
					it.setFlightNo(rs.getInt("FlightNo"));
					it.setResrNo(resrNo);
					
					its.add(it);	
				}
			}
			catch(Exception e) {
				System.out.println(e);
				return null;
			}
			return its;
		}
}
