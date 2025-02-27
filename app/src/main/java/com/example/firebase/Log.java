package com.example.firebase;

import java.util.Date;

public class Log {
    private String tag;
    private String message;
    private Date date;

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
