<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Login</title></head>
<body>
    <h2>Login</h2>
    <form id="loginForm" action="AuthServlet" method="post" onsubmit="return validateLogin()">
        Email: <input type="email" id="email" name="email" required><br>
        Password: <input type="password" id="password" name="password" required><br>
        <button type="submit">Login</button>
    </form>

    <script>
        function validateLogin(){
            const email = document.getElementById('email').value.trim();
            const pass = document.getElementById('password').value;
            if (!email) { alert('Email is required'); return false; }
            if (!pass || pass.length < 6) { alert('Password must be at least 6 characters'); return false; }
            return true;
        }
    </script>
</body>
</html>