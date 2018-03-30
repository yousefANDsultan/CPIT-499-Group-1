package com.example.yousef.seniorproject_cpit499;

/**
 * Created by YOUSEF on 2018-03-28.
 */


public class products {

    private String name;
    private double price;

    public products() {
    }

    public products(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
