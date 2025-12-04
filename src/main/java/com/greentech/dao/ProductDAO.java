package com.greentech.dao;
import java.util.List;

import com.greentech.model.Product;

public interface ProductDAO {
    boolean addProduct(Product p);
    List<Product> getAllProducts();
    boolean approveProduct(int productId);
    Product getProductById(int id);
    /**
     * Atomically update inventory for a given product.
     * @param id product id
     * @param newQuantity target quantity to set
     * @param minExpected minimum current quantity required for the update to succeed (used to avoid oversell)
     * @return true if update succeeded
     */
    boolean updateInventory(int id, int newQuantity, int minExpected);
}