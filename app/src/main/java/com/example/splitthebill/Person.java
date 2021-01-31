package com.example.splitthebill;

public class Person {
    private String name, currency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Person(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }
}
