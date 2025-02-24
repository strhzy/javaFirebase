package com.example.firebase;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String email;
    private String role;

    public User(String id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public User() {}

    public void setId(String id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getRole(){
        return role;
    }
}