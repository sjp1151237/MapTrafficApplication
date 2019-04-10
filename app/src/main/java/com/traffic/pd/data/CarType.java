package com.traffic.pd.data;

import java.io.Serializable;

public class CarType implements Serializable {
    String id;
    String       name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    String weight;
    String        volume;
    String capacity;
    String     logo;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    int num;
}
