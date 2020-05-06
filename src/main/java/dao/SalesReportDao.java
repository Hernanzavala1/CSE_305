package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import model.Flight;
import model.SalesReport;

public class SalesReportDao {
	
	public List<SalesReport> getSalesReport(String month, String year) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get sales report for a particular month must be implemented using month and year passed
		 */
		
		
		List<SalesReport> sales = new ArrayList<SalesReport>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql4.cs.stonybrook.edu:3306/jelthomas", "jelthomas", "111360747");
			PreparedStatement statement = con.prepareStatement("SELECT R.ResrNo, R.ResrDate, R.TotalFare, R.BookingFee, R.RepSSN, P.FirstName, P.LastName FROM Reservation R, Customer C, Person P WHERE R.ResrDate >= ? AND R.ResrDate < ? AND R.AccountNo = C.AccountNo AND C.Id = P.Id"); 
			
			String sDate = year + "-" + month + "-" + "01";
			Date startDate =Date.valueOf(sDate);
			java.util.Date sTemp = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);

			Calendar temp = new GregorianCalendar();
	        temp.setTime(sTemp);
			temp.add(Calendar.MONTH, 1);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			String strDate = dateFormat.format(temp.getTime());
			Date endDate =Date.valueOf(strDate);
	        statement.setDate(1, startDate);
			statement.setDate(2, endDate);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				SalesReport sale = new SalesReport();
				Date dateObj = rs.getDate("ResrDate");
				String date = dateObj.toString();
				sale.setResrNo(rs.getInt("ResrNo"));
				sale.setResrDate(date);
				sale.setTotalFare(rs.getDouble("TotalFare"));
				sale.setBookingFee(rs.getDouble("BookingFee"));
				sale.setRepSSN(rs.getString("RepSSN"));
				sale.setFirstName(rs.getString("FirstName"));
				sale.setLastName(rs.getString("LastName"));
					
				sales.add(sale);	
			}
		
			return sales;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	}

}
