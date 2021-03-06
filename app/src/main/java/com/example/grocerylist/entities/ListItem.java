package com.example.grocerylist.entities;

/**
 * List item is the child of the Grocery list
 * Contains information of the items needs to be purchased
 */
public final class ListItem {

    private String id;
    private String name;
    private double quantity;
    private int measure;
    private boolean isPurchased = false;

    /**
     * Empty constructor
     */
    public ListItem() {
    }

    /**
     * Constructor to populate fields
     * @param id generated by firebase
     * @param name of the item to be purchased
     * @param quantity amount to be purchase
     * @param measure unit of measurement
     * @param isPurchased boolean is set if the item has been purchase
     */
    public ListItem(String id, String name, double quantity, int measure, boolean isPurchased) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
        this.isPurchased = isPurchased;
    }

    /**
     * Constructor to populate fields
     * @param name of the item to be purchased
     * @param quantity amount to be purchase
     * @param measure unit of measurement
     */
    public ListItem(String name, double quantity, int measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
        this.isPurchased = false;
    }

    /**
     * list id generated by firebase
     * @return id of the list
     */
    public String getId() {
        return id;
    }

    /**
     * list id generated by firebase
     * @param id id of the list generated by firebase
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the list name
     * @return name of the list
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of the list name
     * @param name name of the list
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the quantity
     * @return double in quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Setter of the quantity
     * @param quantity of the item to be purchased
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Getter of the measurement
     * @return the position of the array (arrays/unit_measure_imperial or unit_measure_metric)
     * that is depending of the user setting
     */
    public int getMeasure() {
        return measure;
    }

    /**
     * Setter of the measurement
     * @param measure See the getter for more info
     */
    public void setMeasure(int measure) {
        this.measure = measure;
    }

    /**
     * Getter for the isPurchased
     * @return if it is purchased or not
     */
    public boolean isPurchased() {
        return isPurchased;
    }

    /**
     * Setter for the isPurchased
     * @param purchased is a boolean
     */
    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    /**
     * toString for debugging
     * @return
     */
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
