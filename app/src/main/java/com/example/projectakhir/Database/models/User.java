package com.example.projectakhir.Database.models;

public class User {
    private int image_user;
    private String username,id, Gender;

    public User(){}

    public User(int image_user, String username, String gender) {
        this.image_user = image_user;
        this.username = username;
        Gender = gender;
    }

    public int getImage_user() {
        return image_user;
    }

    public void setImage_user(int image_user) {
        this.image_user = image_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
