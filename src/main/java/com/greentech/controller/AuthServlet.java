package com.greentech.controller;

import com.greentech.model.User;
import com.greentech.util.DBConnection;
import com.greentech.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        if (email == null || email.isBlank() || pass == null || pass.isBlank()) {
            response.sendRedirect("login.jsp?error=InvalidInput");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email=?")) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String storedHash = rs.getString("password");
                    // Detect bcrypt hash (starts with $2a$ or $2b$ or $2y$ typically)
                    boolean looksLikeBcrypt = storedHash != null && storedHash.startsWith("$2");

                    if (storedHash != null && looksLikeBcrypt && BCrypt.checkpw(pass, storedHash)) {
                        User user = new User(userId, rs.getString("name"), rs.getString("email"), storedHash, rs.getString("role"));
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);

                        if ("ADMIN".equals(user.getRole())) response.sendRedirect("dashboard_admin.jsp");
                        else if ("VENDOR".equals(user.getRole())) response.sendRedirect("dashboard_vendor.jsp");
                        else response.sendRedirect("dashboard_customer.jsp");
                        return;
                    }

                    // If stored password doesn't look like bcrypt, assume legacy plaintext.
                    if (storedHash != null && !looksLikeBcrypt) {
                        if (storedHash.equals(pass)) {
                            // Successful plaintext match - migrate to bcrypt
                            String newHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
                            try {
                                UserDAO userDao = new UserDAO();
                                userDao.updatePassword(userId, newHash);
                            } catch (Exception ignore) { /* non-fatal: proceed with login even if migration fails */ }

                            User user = new User(userId, rs.getString("name"), rs.getString("email"), newHash, rs.getString("role"));
                            HttpSession session = request.getSession();
                            session.setAttribute("user", user);

                            if ("ADMIN".equals(user.getRole())) response.sendRedirect("dashboard_admin.jsp");
                            else if ("VENDOR".equals(user.getRole())) response.sendRedirect("dashboard_vendor.jsp");
                            else response.sendRedirect("dashboard_customer.jsp");
                            return;
                        } else {
                            response.sendRedirect("login.jsp?error=InvalidCredentials");
                            return;
                        }
                    }

                    response.sendRedirect("login.jsp?error=InvalidCredentials");
                    return;
                } else {
                    response.sendRedirect("login.jsp?error=InvalidCredentials");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=ServerError");
        }
    }
}