package com.greentech.service;

import com.greentech.dao.ProductDAO;
import com.greentech.dao.ProductDAOImpl;
import com.greentech.model.Product;

public class ProductService {

    private ProductDAO productDAO = new ProductDAOImpl();

    // synchronized: Only one thread can execute this method at a time per instance
    public synchronized boolean purchaseProduct(int productId, int quantityToBuy) {
        // 1. Validate inputs
        if (quantityToBuy <= 0) return false;

        // 2. Fetch current product details
        Product product = productDAO.getProductById(productId);

        // 3. Business Logic: Check if product exists and has enough stock
        if (product != null && product.getQuantity() >= quantityToBuy) {
            int current = product.getQuantity();
            int newQuantity = current - quantityToBuy;

            // 4. Atomically update the inventory using DAO guard (minExpected = quantityToBuy)
            boolean updated = productDAO.updateInventory(productId, newQuantity, quantityToBuy);
            return updated;
        }

        // Return false if out of stock or product not found
        return false;
    }
}