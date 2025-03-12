package com.example.accountsecurity.bean;

public class ModifyNameAndIcon {
    String avatar_url;
    String id;
    String username;

    public ModifyNameAndIcon(String avatar_url, String id, String username) {
        this.avatar_url = avatar_url;
        this.id = id;
        this.username = username;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
