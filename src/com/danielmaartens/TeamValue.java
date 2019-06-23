package com.danielmaartens;

public class TeamValue {

    private String name;
    private Integer value;

    public TeamValue(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }
}
