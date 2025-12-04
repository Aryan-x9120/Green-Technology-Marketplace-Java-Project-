<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.greentech.model.User" %>
<%
    // Session Check: Prevent access without login
    User user = (User) session.getAttribute("user");
    if (user == null || !"CUSTOMER".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Dashboard</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <h1>Welcome, <%= user.getName() %>!</h1>
        <p>This is the Green Technology Marketplace.</p>
        
        <div class="card">
            <h3>Start Shopping</h3>
            <p>Browse our collection of eco-friendly technology.</p>
            <!-- Calls the ProductServlet to load data before showing the browse page -->
            <a href="ProductServlet" class="btn">Browse Products</a>
        </div>

        <div class="card">
            <h3>My Account</h3>
            <a href="login.jsp" class="btn" style="background-color:red;">Logout</a>
        </div>
    </div>
</body>
</html>