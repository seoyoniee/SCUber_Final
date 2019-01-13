package com.example.scuber.taker;

public class Request_item {

    private String from;
    private String to;
    private int time_hour;
    private int time_min;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTime_hour() {
        return time_hour;
    }

    public void setTime_hour(int time_hour) {
        this.time_hour = time_hour;
    }

    public int getTime_min() {
        return time_min;
    }

    public void setTime_min(int time_min) {
        this.time_min = time_min;
    }

    public Request_item(String from, String to, Integer time_hour, Integer time_min) {
        this.from = from;
        this.to = to;
        this.time_hour = time_hour;
        this.time_min = time_min;
    }

}

