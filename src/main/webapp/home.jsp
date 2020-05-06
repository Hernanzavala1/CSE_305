<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
	This is the Home page for Customer Representative
	This page contains various buttons to navigate the online auction house
	This page contains customer representative specific accessible buttons
-->

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="viewport" content="width:device-width, initial-scale=1">
		<link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet" />
		<title>Customer Home</title>
	</head>
	<body>
	<nav>
		<h1 style = "text-align: center;"> <u>Welcome to the Online Travel Reservation System! </u></h1>
	</nav>
	
		<div style = "margin-top: 30px;" class="container">
			<h2>Customer Options:</h2>
			<%
			String email = (String)session.getAttribute("email");
			String role = (String)session.getAttribute("role");
			
			//redirect to appropriate home page if already logged in
			if(email != null) {
				if(role.equals("manager")) {
					response.sendRedirect("managerHome.jsp");
				}
				else if(role.equals("customerRepresentative")) {
					response.sendRedirect("customerRepresentativeHome.jsp");
				}
			}
			else {
				// redirect to log in if not alreaddy logged in
				response.sendRedirect("index.jsp");
			}
			%>
			
			<div style = "margin-top: 50px;" class="row">
				<div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 style = "text-align: center;" class="card-title">Reservations</h5>
    					<div style = "text-align: center;" class="container">
							<form action="getCustomerCurrentReservations">
								<input type="submit" value="Current Reservations" class="btn btn-success"/>
							</form>
							<form action="getCustomerAllReservations" class="pt-1">
								<input type="submit" value="All Reservations" class="btn btn-success"/>
							</form>
						</div>
					  </div>
					</div>
				</div>
				<div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 style = "text-align: center;" class="card-title">Book Reservations</h5>
    					<div style = "text-align: center;" class="container">
							<form action="bookFlightReservationsCustomer">
								<input type="submit" value="Book Reservation" class="btn btn-success"/>
							</form>
						</div>
					  </div>
					</div>
				</div>
				<div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 style = "text-align: center;" class="card-title">Reverse Auctions</h5>
    					<div style = "text-align: center;" class="container">
							<form action="getReverseAuctions">
								<input type="submit" value="Reverse Auctions" class="btn btn-success"/>
							</form>
						</div>
					  </div>
					</div>
				</div>
				<!-- <div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 class="card-title">History of all orders</h5>
    					<div class="container">
							<form action="getOrderHistory">
								<input type="submit" value="All History" class="btn btn-primary"/>
							</form>
						</div>
					  </div>
					</div>
				</div> -->
				<!-- <div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 class="card-title">Items sold by Seller</h5>
    					<div class="container">
							<form action="getSellers">
								<input type="submit" value="Seller Info" class="btn btn-primary"/>
							</form>
						</div>
					  </div>
					</div>
				</div> -->
			<!--  	<div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 class="card-title">Search Movies</h5>
    					<div class="container">
							<form action="searchMovies">
								<input type="submit" value="Search Movies" class="btn btn-success"/>
							</form>
						</div>
					  </div>
					</div>
				</div> -->
				
		</div>
		<br>
		
		<div style = "text-align: center;" class="row">
			<div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 style = "text-align: center;" class="card-title">View Best Seller Flights</h5>
    					<div class="container">
							<form action="getBestsellersForCustomer">
								<input type="submit" value="View Best Sellers" class="btn btn-success"/>
							</form>
						</div>
					  </div>
					</div>
				</div>
				<br>
				<div class="col">
					<div class="card">
					  <div class="card-body">
					    <h5 style = "text-align: center;" class="card-title">Personalized Flight Suggestions</h5>
    					<div style = "text-align: center;" class="container">
							<form style = "display: inline-block;" action="personalizedSuggestions">
								<input type="submit" value="View Personalized Suggestions" class="btn btn-success"/>
							</form>
						</div>
					  </div>
					</div>
				</div>
			</div>
		
		
		<div style = "margin-top: 10px;" class="container">
			<form action="logout">
				<input type="submit" value="Logout" class="btn btn-danger"/>
			</form>
		</div>
		
		<script src="webjars/jquery/3.3.1/jquery.min.js"></script>
		<script src="webjars/bootstrap/4.1.3/bootstrap.min.js"></script>
	</body>
</html>