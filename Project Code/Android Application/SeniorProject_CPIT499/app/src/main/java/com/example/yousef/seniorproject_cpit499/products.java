package com.example.yousef.seniorproject_cpit499;

/**
 * Created by YOUSEF on 2018-03-28.
 */


public class products extends ID {

    private String name;
    private double price;
    private int quantity;

    public products() {
    }

    public products(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
