package com.example.pirog.Model;

public class Users {
    private String id, name, phone, password, image;

    public Users()
    {

    }

    public Users(String id, String name, String phone, String password, String image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
    }

    public String getId()
    {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
