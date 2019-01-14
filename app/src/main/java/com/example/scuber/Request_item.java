package com.example.scuber;

public class Request_item {

    private String from;
    private String to;
    private String state;

    private int time_hour;
    private int time_min;

    private String _id;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


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

    public void setTime_mine(int time_min) {
        this.time_min = time_min;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public Request_item(String from, String to, Integer time_hour, Integer time_min, String state, String _id) {
        this.from = from;
        this.to = to;
        this.time_hour = time_hour;
        this.time_min = time_min;
        this.state = state;
        this._id = _id;
    }

}

