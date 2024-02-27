package com.ados;

import java.util.Objects;

public abstract class Animal {
    protected String id;
    protected String name;
    protected String type;
    protected Integer numOfFoots;

    abstract String speak();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumOfFoots() {
        return numOfFoots;
    }

    public void setNumOfFoots(Integer numOfFoots) {
        this.numOfFoots = numOfFoots;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + type + "," + numOfFoots + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id.equals(animal.id) && name.equals(animal.name) && type.equals(animal.type) && numOfFoots.equals(animal.numOfFoots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, numOfFoots);
    }
}
