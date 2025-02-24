package com.example.firebase;

import java.io.Serializable;

public class Service implements Serializable {
    private String id;
    private String name;

    public void setId(String new_id){
        id = new_id;
    }
    public void setName(String new_name){
        name = new_name;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
