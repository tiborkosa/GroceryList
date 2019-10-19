package com.example.grocerylist.entities;

import com.example.grocerylist.util.MyDateFormat;

import java.util.Calendar;

public final class GroceryList {

    private String id;
    private String listName;
    private String createDate = MyDateFormat.getDate(Calendar.getInstance());
    private String dueDate;
    private int priority;

    public GroceryList() {}

    public GroceryList(String id, String listName, String createDate, String dueDate, int priority) {
        this.id = id;
        this.listName = listName;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public GroceryList(String listName, String dueDate, int priority) {
        this.listName = listName;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "GroceryList{" +
                "id='" + id + '\'' +
                ", listName='" + listName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", priority=" + priority +
                '}';
    }
}
