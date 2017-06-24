package com.qetuop.weighttracker;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by brian on 6/17/17.
 */

public class Entry {
    private int id;
    private long date;
    private double weight;
    private String comment;

    public Entry() {}

    public Entry(int id, long date, double weight, String comment) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.comment = comment;
    }

    public Entry(int id, long date, double weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.comment = "";
    }

    public Entry( long date, double weight) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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

    @Override
    public String toString() {
        DateFormat df = DateFormat.getDateTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("EST"));

        return "Entry{" +
                "id=" + id +
                ", date=" + df.format(new Date(getDate())) + " (" +getDate()+  ")" +
                ", weight=" + weight +
                ", comment='" + comment + '\'' +
                '}';
    }
}
