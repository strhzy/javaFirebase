package com.example.firebase;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Appointment implements Serializable {
    private String id;
    private String client_id;
    private String client_name;
    private String service_id;
    private String service_name;
    private String date;
    private Time time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return client_id;
    }

    public void setClientId(String client_id) {
        this.client_id = client_id;
    }

    public String getClientName() {
        return client_name;
    }

    public void setClientName(String client_name) {
        this.client_name = client_name;
    }

    public String getServiceId() {
        return service_id;
    }

    public void setServiceId(String service_id) {
        this.service_id = service_id;
    }

    public String getServiceName() {
        return service_name;
    }

    public void setServiceName(String service_name) {
        this.service_name = service_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
