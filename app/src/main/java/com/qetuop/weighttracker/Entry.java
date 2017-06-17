package com.qetuop.weighttracker;

/**
 * Created by brian on 6/17/17.
 */

public class Entry {
    int id;
    int date;
    double weight;
    String comment;

    public Entry() {}

    public Entry(int id, int date, double weight, String comment) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.comment = comment;
    }

    public Entry(int id, int date, double weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.comment = "";
    }

    public Entry( int date, double weight) {
        this.date = date;
        this.weight = weight;
        this.comment = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
