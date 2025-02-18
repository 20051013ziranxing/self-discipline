package com.example.eventbus;

public class UserBaseMessage {
    private String userName;
    private String userEmail;
    private String userIcon;

    public UserBaseMessage(String userName, String userEmail, String userIcon) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
