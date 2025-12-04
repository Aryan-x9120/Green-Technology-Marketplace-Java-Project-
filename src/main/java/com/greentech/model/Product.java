package com.greentech.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String status;
    private int quantity;

    public Product(int id, String name, String description, double price, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    // Convenience constructor including inventory quantity
    public Product(int id, String name, String description, double price, String status, int quantity) {
        this(id, name, description, price, status);
        this.quantity = quantity;
    }

    // Quantity accessor for inventory management
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Getters and Setters (Required for the code to work)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}