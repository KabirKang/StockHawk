package com.udacity.stockhawk.data;

/**
 * Created by kabir on 2/5/2017.
 */

public class StockPoint {
    float date;
    float price;

    public StockPoint(String data) {
        String[] dataTuple = data.split(",");
        if (dataTuple.length == 2) {
            this.date = Float.valueOf(dataTuple[0]);
            this.price = Float.valueOf(dataTuple[1]);
        }
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDate() {
        return date;
    }

    public void setDate(float date) {
        this.date = date;
    }
}
