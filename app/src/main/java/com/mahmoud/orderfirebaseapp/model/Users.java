package com.mahmoud.orderfirebaseapp.model;

public class Users {
    private String name ;
    private String Password ;

    public Users() {
    }

    public Users(String name, String password) {
        this.name = name;
        Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
