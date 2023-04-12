package com.example.pirog.Model;

public class Order {
    private String oid, date, time, totalAmount, uid;

    public Order() {
        // Empty constructor required for Firebase
    }

    public Order(String oid, String date, String time, String totalAmount, String uid) {
        this.oid = oid;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
        this.uid = uid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
