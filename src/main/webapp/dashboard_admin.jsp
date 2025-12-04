<%@ page import="java.util.*, com.greentech.dao.*, com.greentech.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <h1>Admin Dashboard</h1>
    
    <!-- 1. Product Approvals -->
    <h3>Pending Product Approvals</h3>
    <table>
        <tr><th>Name</th><th>Price</th><th>Action</th></tr>
        <%
            ProductDAO productDao = new ProductDAOImpl();
            List<Product> pList = productDao.getAllProducts();
            for(Product p : pList) {
                if("PENDING".equals(p.getStatus())) {
        %>
        <tr>
            <td><%= p.getName() %></td>
            <td><%= p.getPrice() %></td>
            <td>
                <form action="ProductServlet" method="post">
                    <input type="hidden" name="id" value="<%= p.getId() %>">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit" class="btn">Approve</button>
                </form>
            </td>
        </tr>
        <% }} %>
    </table>

    <!-- 2. User Management -->
    <h3>User Management</h3>
    <table>
        <tr><th>ID</th><th>Name</th><th>Role</th><th>Action</th></tr>
        <%
            UserDAO userDao = new UserDAO();
            List<User> uList = userDao.getAllUsers();
            for(User u : uList) {
        %>
        <tr>
            <td><%= u.getId() %></td>
            <td><%= u.getName() %></td>
            <td><%= u.getRole() %></td>
            <td>
                <form action="AdminServlet" method="post">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <input type="hidden" name="action" value="deleteUser">
                    <button type="submit" class="btn" style="background-color:red;">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>

    <h3>Create New User</h3>
    <div class="card">
        <form id="createUserForm" action="AdminServlet" method="post" onsubmit="return validateCreateUser()">
            <input type="hidden" name="action" value="createUser">
            Name: <input type="text" id="u_name" name="name" required>
            Email: <input type="email" id="u_email" name="email" required>
            Password: <input type="password" id="u_password" name="password" required>
            Role: 
            <select id="u_role" name="role">
                <option value="CUSTOMER">Customer</option>
                <option value="VENDOR">Vendor</option>
                <option value="ADMIN">Admin</option>
            </select>
            <br><br>
            <button type="submit" class="btn">Create User</button>
        </form>

        <script>
            function validateCreateUser(){
                const name = document.getElementById('u_name').value.trim();
                const email = document.getElementById('u_email').value.trim();
                const pass = document.getElementById('u_password').value;
                if (!name) { alert('Name is required'); return false; }
                if (!email) { alert('Email is required'); return false; }
                if (!pass || pass.length < 8) { alert('Password must be at least 8 characters'); return false; }
                return true;
            }
        </script>
    </div>
</body>
</html>