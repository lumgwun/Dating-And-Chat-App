package com.lahoriagency.cikolive.Classes;

public class PurchaseDiamond {

    private int count;
    private String price;
    public PurchaseDiamond() {
        super();
    }
    public PurchaseDiamond(int count, String price) {
        this.count=count;
        this.price=price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
