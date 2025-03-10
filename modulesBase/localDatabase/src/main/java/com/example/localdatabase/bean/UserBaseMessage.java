package com.example.localdatabase.bean;

public class UserBaseMessage {
    String userName;
    String userPictureURL;
    String userEmail;
    String userToken;
    String userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPictureURL() {
        return userPictureURL;
    }

    public void setUserPictureURL(String userPictureURL) {
        this.userPictureURL = userPictureURL;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserBaseMessage(String userName, String userPictureURL, String userEmail, String userToken, String userId) {
        this.userName = userName;
        this.userPictureURL = userPictureURL;
        this.userEmail = userEmail;
        this.userToken = userToken;
        this.userId = userId;
    }
}
