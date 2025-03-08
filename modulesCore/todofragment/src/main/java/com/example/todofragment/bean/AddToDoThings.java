package com.example.todofragment.bean;

public class AddToDoThings {
    String title;
    String description;
    String status;
    String user_id;
    String updated_at;

    public AddToDoThings(String title, String description, String status, String user_id, String updated_at) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.user_id = user_id;
        this.updated_at = updated_at;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
