package com.example.splitthebill;

public class User {
    private String username, email;

    public User( String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}