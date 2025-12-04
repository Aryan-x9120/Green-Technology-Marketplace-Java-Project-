package com.greentech.controller;

import com.greentech.dao.ProductDAO;
import com.greentech.dao.ProductDAOImpl;
import com.greentech.model.Product;
import com.greentech.service.ProductService; // NEW: Import the Service Layer
import com.greentech.util.EmailTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    // Initialize the Service Layer (handles business logic & synchronization)
    private ProductService productService = new ProductService();
    
    // Initialize DAO (handles direct data access)
    private ProductDAO productDAO = new ProductDAOImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // logic to add product - server-side validation + safe parsing
            String name = request.getParameter("name");
            String desc = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String qtyStr = request.getParameter("quantity");

            if (name == null || name.isBlank() || priceStr == null || priceStr.isBlank()) {
                response.sendRedirect("dashboard_vendor.jsp?msg=InvalidInput");
                return;
            }

            double price;
            int quantity = 0;
            try {
                price = Double.parseDouble(priceStr);
                if (qtyStr != null && !qtyStr.isBlank()) quantity = Integer.parseInt(qtyStr);
            } catch (NumberFormatException nfe) {
                response.sendRedirect("dashboard_vendor.jsp?msg=InvalidNumber");
                return;
            }

            Product p = new Product(0, name.trim(), desc == null ? "" : desc.trim(), price, "PENDING", quantity);

            boolean added = productDAO.addProduct(p);
            if (added) response.sendRedirect("dashboard_vendor.jsp?msg=Added");
            else response.sendRedirect("dashboard_vendor.jsp?msg=Error");
        }
        else if ("approve".equals(action)) {
            // Admin approving a product
            int id = Integer.parseInt(request.getParameter("id"));
            productDAO.approveProduct(id);
            response.sendRedirect("dashboard_admin.jsp");
        }
        else if ("buy".equals(action)) {
            /* 
             * --- SOLUTION DESIGN & CORE JAVA SECTION ---
             * We use the Service Layer here. The purchaseProduct method is SYNCHRONIZED.
             * This ensures two users cannot buy the last item at the exact same time.
             */
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                int quantityToBuy = 1; // Defaulting to 1 for this example

                // Call the Service Layer
                boolean purchaseSuccess = productService.purchaseProduct(id, quantityToBuy);

                if (purchaseSuccess) {
                    // Only send email if the database update was successful
                    EmailTask.sendEmailAsync("customer@example.com");
                    response.sendRedirect("dashboard_customer.jsp?msg=OrderSuccess");
                } else {
                    // Handle case where item is Out of Stock
                    response.sendRedirect("dashboard_customer.jsp?msg=OutOfStock");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("dashboard_customer.jsp?msg=Error");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all products to display on the page
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("productList", products);
        request.getRequestDispatcher("browse_products.jsp").forward(request, response);
    }
}