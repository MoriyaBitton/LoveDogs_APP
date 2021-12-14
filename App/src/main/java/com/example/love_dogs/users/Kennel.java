package com.example.love_dogs.users;

import android.location.Location;

import java.util.HashMap;
import java.util.Objects;

public class Kennel {
    private String name;
    private String uid;
    private HashMap<String,Object> dogsMap;
    private Location address;

    public Kennel(String name, String uid, HashMap<String, Object> dogsMap, Location address) {
        this.name = name;
        this.uid = uid;
        this.dogsMap = dogsMap;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Kennel{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", dogsMap=" + dogsMap +
                ", address=" + address +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kennel)) return false;
        Kennel kennel = (Kennel) o;
        return Objects.equals(getName(), kennel.getName()) && Objects.equals(getUid(), kennel.getUid()) && Objects.equals(getDogsMap(), kennel.getDogsMap()) && Objects.equals(getAddress(), kennel.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUid(), getDogsMap(), getAddress());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, Object> getDogsMap() {
        return dogsMap;
    }

    public void setDogsMap(HashMap<String, Object> dogsMap) {
        this.dogsMap = dogsMap;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }
}
