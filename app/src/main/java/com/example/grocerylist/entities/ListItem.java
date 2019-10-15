package com.example.grocerylist.entities;

public final class ListItem {

    private String id;
    private String name;
    private double quantity;
    private int measure;
    private boolean isPurchased = false;

    public ListItem() {
    }

    public ListItem(String id, String name, double quantity, int measure, boolean isPurchased) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
        this.isPurchased = isPurchased;
    }

    public ListItem(String name, double quantity, int measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
        this.isPurchased = false;
    }

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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", isPurchased=" + isPurchased +
                '}';
    }
}
