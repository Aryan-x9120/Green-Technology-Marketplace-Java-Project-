package com.greentech.dao;

import com.greentech.model.Product;
import com.greentech.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

    @Override
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products (name, description, price, status, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getStatus() == null ? "PENDING" : p.getStatus());
            ps.setInt(5, p.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { logger.error("Failed to add product: {}", p, e); return false; }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM products");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("status")));
            }
        } catch (SQLException e) { logger.error("Failed to fetch all products", e); }
        return list;
    }

    @Override
    public boolean approveProduct(int productId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE products SET status='APPROVED' WHERE id=?")) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { logger.error("Failed to approve product {}", productId, e); return false; }
    }
     @Override
    public Product getProductById(int id) {
        Product p = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Product(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("description"), 
                    rs.getDouble("price"), 
                    rs.getString("status")
                );
                // populate quantity if column exists
                try { p.setQuantity(rs.getInt("quantity")); } catch (SQLException ignore) { }
            }
        } catch (SQLException e) { logger.error("Failed to fetch product by id {}", id, e); }
        return p;
    }

    @Override
    public boolean updateInventory(int id, int newQuantity, int minExpected) {
        // Perform atomic update: set quantity = ? WHERE id = ? AND quantity >= ?
        String sql = "UPDATE products SET quantity = ? WHERE id = ? AND quantity >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, id);
            ps.setInt(3, minExpected);
            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) { logger.error("Failed to update inventory for product {}", id, e); return false; }
    }
}