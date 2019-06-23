package com.danielmaartens;

public class TeamValue {

    private String name;
    private Integer value;
    private Integer position;

    public TeamValue(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }


    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPosition() {
        return this.position;
    }
}
