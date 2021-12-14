package com.example.love_dogs.login;

import android.media.Image;

import java.util.Objects;

public class dog {
    private String name;
    private double age;
    private String race;
    private double weight;
    private Image photo;

    public dog(String name, double age, String race, double weight, Image photo) {
        this.name = name;
        this.age = age;
        this.race = race;
        this.weight = weight;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof dog)) return false;
        dog dog = (dog) o;
        return Double.compare(dog.getAge(), getAge()) == 0 && Double.compare(dog.getWeight(), getWeight()) == 0 && Objects.equals(getName(), dog.getName()) && Objects.equals(getRace(), dog.getRace()) && Objects.equals(getPhoto(), dog.getPhoto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), getRace(), getWeight(), getPhoto());
    }

    @Override
    public String toString() {
        return "dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", race='" + race + '\'' +
                ", weight=" + weight +
                ", photo=" + photo +
                '}';
    }
}
