package com.example.grocerylist.entities;

public final class User {

    private String id;
    private String name;
    private String email;
    private String image;
    private boolean isPaid;

    public User() {}

    public User(String id, String name, String email, String image, boolean isPaid) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.isPaid = isPaid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}
