package com.example.yousef.seniorproject_cpit499;

/**
 * Created by YOUSEF on 2018-04-18.
 */

public class orders extends ID{
    private double total;
    private String status;

    public orders() {
    }

    public orders(double orderPrice, String status) {
        this.total = orderPrice;
        this.status = status;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double orderPrice) {
        this.total = orderPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
