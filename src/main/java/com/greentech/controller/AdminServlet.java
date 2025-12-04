package com.greentech.controller;

import com.greentech.dao.UserDAO;
import com.greentech.model.User;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("deleteUser".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            userDAO.deleteUser(userId);
            // Redirect back to dashboard to see changes
            response.sendRedirect("dashboard_admin.jsp?msg=UserDeleted");
        } 
        else if ("createUser".equals(action)) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String pass = request.getParameter("password");
            String role = request.getParameter("role");

            if (name == null || name.isBlank() || email == null || email.isBlank() || pass == null || pass.isBlank()) {
                response.sendRedirect("dashboard_admin.jsp?msg=InvalidInput");
                return;
            }

            // Hash password before storing
            String hashed = BCrypt.hashpw(pass, BCrypt.gensalt(12));
            User newUser = new User(0, name.trim(), email.trim(), hashed, role == null ? "CUSTOMER" : role.trim());
            boolean ok = userDAO.addUser(newUser);
            if (ok) response.sendRedirect("dashboard_admin.jsp?msg=UserCreated");
            else response.sendRedirect("dashboard_admin.jsp?msg=Error");
        }
    }
}